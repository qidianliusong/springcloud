package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.annotation.TokenAnnotation;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.manager.GzhPayMng;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.GsonUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 代缴
 */
@RestController
@RequestMapping("/wzb/feeAgent")
public class FeeAgentController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeAgentController.class);


    @Resource
    private WzbDataWriteFeign writeFeign;

    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private GzhPayMng gzhPayMng;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @ApiOperation(value = "生成代缴记录", notes = "生成代缴记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "防止重复提交token", name = "validateToken", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "initFeeAgent", method = RequestMethod.POST)
    @TokenAnnotation
    public BaseResult<FeeAgentDTO> initFeeAgent(@Validated @RequestBody FeeAgentDTO feeAgentDTO,
                                                BindingResult bindingResult, String token, HttpSession session) throws WzbAggregateException {
        BaseResult<FeeAgentDTO> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult)) {
            return baseResult;
        }


        BaseResult<FeeDetailDTO> feeDetailDTOBaseResult = readFeign.getFeeDetailById(feeAgentDTO.getFeeDetailId());

        FeignUtil.validateFeignResult(feeDetailDTOBaseResult, "readFeign.getFeeDetailById", logger);
        FeeDetailDTO feeDetailDTO = feeDetailDTOBaseResult.getData();

        if (feeDetailDTO.getStatus() != FeeDetailDTO.STATUS_OPEN) {
            logger.warn("非开启状态不能缴费");
            baseResult.setCode(WzbAggregateErrEnum.feedetail_status_err.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.feedetail_status_err.getMessage());
            return baseResult;
        }

        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        // 判断用户权限
        if (currentUser == null || !isCommittee(feeDetailDTO.getClassId(), currentUser.getId(), token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }
        feeAgentDTO.setUserId(currentUser.getId());
        String kinsfolk = currentUser.getKinsfolk();
        if (StringUtils.isBlank(kinsfolk)){
            kinsfolk = getKinsfolk(feeDetailDTO.getClassId(), currentUser.getId(), token);
            currentUser.setKinsfolk(kinsfolk);
            session.setAttribute(WzbConstant.SESSION_USER_NAME,currentUser);
        }
        feeAgentDTO.setUserName(StringUtils.isBlank(kinsfolk) ? currentUser.getRealname() : kinsfolk);
        BaseResult<FeeAgentDTO> initFeeAgent = writeFeign.initFeeAgent(feeAgentDTO);

        FeignUtil.validateFeignResult(initFeeAgent, "writeFeign.initFeeAgent", logger);

        baseResult.setData(initFeeAgent.getData());
        return baseResult;
    }


    @ApiOperation(value = "代缴预支付", notes = "代缴预支付")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代缴记录id", name = "feeAgentId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "prePay", method = RequestMethod.POST)
    public BaseResult<PrePayDTO> prePay(Long feeAgentId, String token, HttpServletRequest request, HttpSession session) throws WzbAggregateException {
        BaseResult<PrePayDTO> baseResult = new BaseResult<>();

        WxAuthDTO wxAuthDTO = (WxAuthDTO) session.getAttribute(WzbConstant.WX_AUTH_NAME);
        if (wxAuthDTO == null || wxAuthDTO.getOpenid() == null) {
            baseResult.setCode(WzbAggregateErrEnum.wx_openid_is_null.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.wx_openid_is_null.getMessage());
            return baseResult;
        }

        BaseResult<FeeAgentDTO> feeAgentDTOBaseResult = readFeign.findFeeAgentById(feeAgentId);

        FeignUtil.validateFeignResult(feeAgentDTOBaseResult, "readFeign.findFeeAgentById", logger);

        FeeAgentDTO feeAgentDTO = feeAgentDTOBaseResult.getData();

        if (feeAgentDTO.getStatus() == FeeAgentDTO.STATUS_PAY || feeAgentDTO.getStatus() == FeeAgentDTO.STATUS_PRE_PAY) {
            baseResult.setCode(WzbAggregateErrEnum.pay_repect_err.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.pay_repect_err.getMessage());
            return baseResult;
        }
        BaseResult<FeeDetailDTO> feeDetailDTOBaseResult = readFeign.getFeeDetailById(feeAgentDTO.getFeeDetailId());

        FeignUtil.validateFeignResult(feeDetailDTOBaseResult, "readFeign.getFeeDetailById", logger);
        FeeDetailDTO feeDetailDTO = feeDetailDTOBaseResult.getData();

        if (feeDetailDTO.getStatus() != FeeDetailDTO.STATUS_OPEN) {
            logger.warn("非开启状态不能缴费");
            baseResult.setCode(WzbAggregateErrEnum.feedetail_status_err.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.feedetail_status_err.getMessage());
            return baseResult;
        }

        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        // 判断用户权限
        if (currentUser == null || !isCommittee(feeDetailDTO.getClassId(), currentUser.getId(), token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_permission_denied.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_permission_denied.getMessage());
            return baseResult;
        }

        PrePayDTO prePayDTO = gzhPayMng.agentPrePay(feeAgentDTO.getTotalAmount(), wxAuthDTO.getOpenid(), getIp(request));

        feeAgentDTO.setStatus(FeeAgentDTO.STATUS_PRE_PAY);
        feeAgentDTO.setPayTime(new Date());
        feeAgentDTO.setOutTradeNo(prePayDTO.getWzbOrderDTO().getOutTradeNo());

        BaseResult<Void> updateFeeAgent = writeFeign.updateFeeAgent(feeAgentDTO);
        FeignUtil.validateFeignResult(updateFeeAgent, "writeFeign.updateFeeAgent", logger);

        baseResult.setData(prePayDTO);
        return baseResult;
    }

    @ApiOperation(value = "代缴付费成功调用", notes = "代缴付费成功调用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "代缴记录id", name = "feeAgentId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "afterPay", method = RequestMethod.POST)
    public BaseResult<Void> afterPay(@RequestParam(required = true) Long feeAgentId) throws WzbAggregateException {
        BaseResult<Void> baseResult = new BaseResult<>();
        BaseResult<FeeAgentDTO> feeAgentResult = readFeign.findFeeAgentById(feeAgentId);

        if (SystemCode.success.getCode() != feeAgentResult.getCode()) {
            baseResult.setCode(feeAgentResult.getCode());
            baseResult.setMessage("根据id获取代缴信息出错，失败原因：" + feeAgentResult.getMessage());
            return baseResult;
        }

        FeeAgentDTO feeAgentDTO = feeAgentResult.getData();
        handleFeeAgentPay(feeAgentDTO);
        return baseResult;
    }

    private void handleFeeAgentPay(FeeAgentDTO feeAgentDTO) throws WzbAggregateException {

        if (feeAgentDTO == null || feeAgentDTO.getStatus() != FeeAgentDTO.STATUS_PRE_PAY)
            return;

        if (StringUtils.isEmpty(feeAgentDTO.getOutTradeNo())) {
            feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
            writeFeign.updateFeeAgent(feeAgentDTO);
        }

        QueryResultDTO queryResultDTO = gzhPayMng.query(feeAgentDTO.getOutTradeNo());
        WxPayQueryResponseDTO wxPayQueryResponseDTO = queryResultDTO.getWxPayQueryResponseDTO();

        if (!"SUCCESS".equals(wxPayQueryResponseDTO.getReturn_code())
                || !"SUCCESS".equals(wxPayQueryResponseDTO.getResult_code())) {
            if ("ORDERNOTEXIST".equals(wxPayQueryResponseDTO.getErr_code())) {
                feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
                writeFeign.updateFeeAgent(feeAgentDTO);
            }
            return;
        }
        switch (wxPayQueryResponseDTO.getTrade_state()) {
            case WzbConstant.WX_TRADE_SUCCESS:
                tradeSuccess(feeAgentDTO, wxPayQueryResponseDTO);
                break;
            case WzbConstant.WX_TRADE_USERPAYING:
                break;
            case WzbConstant.WX_TRADE_NOTPAY:
                if (System.currentTimeMillis() < queryResultDTO.getWzbOrderDTO().getExpireTime().getTime())
                    break;
            case WzbConstant.WX_TRADE_CLOSED:
            case WzbConstant.WX_TRADE_PAYERROR:
            case WzbConstant.WX_TRADE_REFUND:
            case WzbConstant.WX_TRADE_REVOKED:
                //此处支付异常
                feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
                writeFeign.updateFeeAgent(feeAgentDTO);
                break;
            default:
                break;
        }

    }

    /**
     * 支付成功
     *
     * @param feeAgentDTO
     * @param wxPayQueryResponseDTO
     */
    private void tradeSuccess(FeeAgentDTO feeAgentDTO, WxPayQueryResponseDTO wxPayQueryResponseDTO) throws WzbAggregateException {
        // 此处支付成功
        Integer totalFee = wxPayQueryResponseDTO.getTotal_fee();
        if (feeAgentDTO.getTotalAmount().multiply(new BigDecimal(100)).intValue() != totalFee) {
            // 标记为支付异常
            feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
            writeFeign.updateFeeAgent(feeAgentDTO);
        } else {
            BaseResult<Void> afterFeeAgentResult = writeFeign.afterFeeAgent(null, feeAgentDTO.getFeeAgentId());
            FeignUtil.validateFeignResult(afterFeeAgentResult, "writeFeign.afterFeeAgent", logger);

            //发送mq
            PayMessageDTO payMessageDTO = new PayMessageDTO();
            payMessageDTO.setAmount(feeAgentDTO.getTotalAmount());
            payMessageDTO.setId(feeAgentDTO.getFeeAgentId());
            payMessageDTO.setTradeNo(feeAgentDTO.getOutTradeNo());
            payMessageDTO.setType(PayMessageDTO.TYPE_FEE_AGENT);
            rabbitTemplate.convertAndSend("wzb.topic","wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));
        }
    }

    @ApiOperation(value = "代缴分页信息", notes = "代缴分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query")})
    @RequestMapping(value = "getPageInfo", method = RequestMethod.GET)
    public BaseResult<PageDTO<FeeAgentDTO>> getPageInfo(@RequestParam(required = true) Long classId,
                                                        @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        BaseResult<PageDTO<FeeAgentDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<FeeAgentDTO>> feeAgentPageInfo = readFeign.getFeeAgentPageInfo(classId, pageNo - 1, pageSize);
        if (!validateFeignResult(baseResult, feeAgentPageInfo, "readFeign.getFeeAgentPageInfo", logger)) {
            return baseResult;
        }
        baseResult.setData(feeAgentPageInfo.getData());
        return baseResult;
    }

    @ApiOperation(value = "根据收费详情id查询代缴信息", notes = "根据收费详情id查询代缴信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    public BaseResult<List<FeeAgentDTO>> getList(@RequestParam(required = true) Long feeDetailId) {
        BaseResult<List<FeeAgentDTO>> baseResult = new BaseResult<>();
        BaseResult<List<FeeAgentDTO>> feeAgentlist = readFeign.getFeeAgentlist(feeDetailId);
        if (!validateFeignResult(baseResult, feeAgentlist, "readFeign.getFeeAgentlist", logger)) {
            return baseResult;
        }
        baseResult.setData(feeAgentlist.getData());
        return baseResult;
    }
}
