package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.manager.GzhPayMng;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.GsonUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 缴费controller
 */
@Api(value = "缴费相关接口")
@RestController
@RequestMapping("/wzb/feePay")
public class FeePayController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeePayController.class);

    @Resource
    private GzhPayMng gzhPayMng;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @Resource
    private WzbDataReadFeign readFeign;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @ApiOperation(value = "根据缴费详情id获取缴费信息列表", notes = "根据缴费详情id获取缴费信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "支付状态:0为未支付,1为已支付,不传递该值为查询全部", name = "payStatus", required = false, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<List<FeePayDTO>> list(@RequestParam(required = true) Long feeDetailId, Integer payStatus) {
        BaseResult<List<FeePayDTO>> baseResult = new BaseResult<>();
        BaseResult<List<FeePayDTO>> feePayList = readFeign.getFeePayList(feeDetailId, payStatus);
        if (!validateFeignResult(baseResult, feePayList, "readFeign.getFeePayList", logger)) {
            return baseResult;
        }
        baseResult.setData(feePayList.getData());
        return baseResult;
    }

    @ApiOperation(value = "交班费", notes = "交班费(预支付)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "小孩id", name = "childId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/prePay", method = RequestMethod.POST)
    public BaseResult<PrePayDTO> prePay(Long feeDetailId,
                                        Long childId, @RequestParam(required = false) String message,
                                        HttpSession session, HttpServletRequest request) throws WzbAggregateException {
        BaseResult<PrePayDTO> baseResult = new BaseResult<>();
        BaseResult<FeeDetailDTO> feeDetailById = readFeign.getFeeDetailById(feeDetailId);
        if (!validateFeignResult(baseResult, feeDetailById, "readFeign.getFeeDetailById", logger)) {
            return baseResult;
        }
        FeeDetailDTO feeDetailDTO = feeDetailById.getData();
        if (feeDetailDTO.getStatus() != FeeDetailDTO.STATUS_OPEN) {
            logger.warn("非开启状态不能缴费");
            baseResult.setCode(WzbAggregateErrEnum.feedetail_status_err.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.feedetail_status_err.getMessage());
            return baseResult;
        }
        WxAuthDTO wxAuthDTO = (WxAuthDTO) session.getAttribute(WzbConstant.WX_AUTH_NAME);
        if (wxAuthDTO == null || wxAuthDTO.getOpenid() == null) {
            baseResult.setCode(WzbAggregateErrEnum.wx_openid_is_null.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.wx_openid_is_null.getMessage());
            return baseResult;
        }
        BaseResult<FeePayDTO> feePay = readFeign.getFeePay(feeDetailId, childId);
        if (!validateFeignResult(baseResult, feePay, "readFeign.getFeePay", logger)) {
            return baseResult;
        }

        FeePayDTO feePayDTO = feePay.getData();

        if (feePayDTO.getStatus() == FeePayDTO.STATUS_PAY || feePayDTO.getStatus() == FeePayDTO.STATUS_PRE_PAY) {
            baseResult.setCode(WzbAggregateErrEnum.pay_repect_err.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.pay_repect_err.getMessage());
            return baseResult;
        }

        PrePayDTO prePayDTO = gzhPayMng.feePrePay(feeDetailDTO.getAmount(), wxAuthDTO.getOpenid(), getIp(request));

        // 获取用户信息
        UserDTO userDTO = (UserDTO) session.getAttribute(WzbConstant.SESSION_USER_NAME);
        feePayDTO.setUserId(userDTO.getId());
        feePayDTO.setUserName(userDTO.getRealname());
        feePayDTO.setMessage(message);
        feePayDTO.setPayTime(new Date());
        feePayDTO.setOutTradeNo(prePayDTO.getWzbOrderDTO().getOutTradeNo());
        feePayDTO.setStatus(FeePayDTO.STATUS_PRE_PAY);
        BaseResult<FeePayDTO> updateFeePay = writeFeign.updateFeePay(feePayDTO);

        if (!validateFeignResult(baseResult, updateFeePay, "writeFeign.updateFeePay", logger)) {
            return baseResult;
        }
        prePayDTO.setFeePayId(feePayDTO.getFeePayId());
        baseResult.setData(prePayDTO);
        return baseResult;
    }

    @ApiOperation(value = "缴费成功后调用", notes = "缴费成功后调用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "缴费记录id", name = "feePayId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/afterPay", method = RequestMethod.POST)
    public BaseResult<Void> afterPay(Long feePayId) throws WzbAggregateException {
        BaseResult<Void> baseResult = new BaseResult<>();
        BaseResult<FeePayDTO> feePayById = readFeign.getFeePayById(feePayId);
        if (!validateFeignResult(baseResult, feePayById, "readFeign.getFeePayById", logger)) {
            return baseResult;
        }
        FeePayDTO feePayDTO = feePayById.getData();
        handleFeeAgentPay(feePayDTO);
        return baseResult;
    }

    private void handleFeeAgentPay(FeePayDTO feePayDTO) throws WzbAggregateException {

        if (feePayDTO == null || feePayDTO.getStatus() != FeePayDTO.STATUS_PRE_PAY)
            return;

        if (StringUtils.isEmpty(feePayDTO.getOutTradeNo())) {
            feePayDTO.setStatus(FeeAgentDTO.STATUS_ERR);
            writeFeign.updateFeePay(feePayDTO);
            return;
        }

        QueryResultDTO queryResultDTO = gzhPayMng.query(feePayDTO.getOutTradeNo());

        WxPayQueryResponseDTO wxPayQueryResponseDTO = queryResultDTO.getWxPayQueryResponseDTO();

        if (!"SUCCESS".equals(wxPayQueryResponseDTO.getReturn_code())
                || !"SUCCESS".equals(wxPayQueryResponseDTO.getResult_code())) {
            if ("ORDERNOTEXIST".equals(wxPayQueryResponseDTO.getErr_code())) {
                feePayDTO.setStatus(FeePayDTO.STATUS_ERR);
                BaseResult<FeePayDTO> update = writeFeign.updateFeePay(feePayDTO);
                FeignUtil.validateFeignResult(update, "writeFeign.updateFeePay", logger);
            }
            return;
        }

        switch (wxPayQueryResponseDTO.getTrade_state()) {
            case WzbConstant.WX_TRADE_SUCCESS:
                tradeSuccess(feePayDTO, wxPayQueryResponseDTO);
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
                feePayDTO.setStatus(FeePayDTO.STATUS_ERR);
                BaseResult<FeePayDTO> update = writeFeign.updateFeePay(feePayDTO);
                FeignUtil.validateFeignResult(update, "writeFeign.updateFeePay", logger);
                break;
            default:
                break;
        }

    }

    /**
     * 支付成功
     */
    private void tradeSuccess(FeePayDTO feePayDTO, WxPayQueryResponseDTO wxPayQueryResponseDTO) throws WzbAggregateException {
        // 此处支付成功
        Integer totalFee = wxPayQueryResponseDTO.getTotal_fee();
        if (feePayDTO.getAmount().multiply(new BigDecimal(100)).intValue() != totalFee) {
            // 标记为支付异常
            feePayDTO.setStatus(FeeAgentDTO.STATUS_ERR);
            writeFeign.updateFeePay(feePayDTO);
        } else {
            BaseResult<Void> afterFeePayResult = writeFeign.afterFeePay(null, feePayDTO.getChildId(), feePayDTO.getFeePayId());
            FeignUtil.validateFeignResult(afterFeePayResult, "writeFeign.afterFeePay", logger);

            //发送mq
            PayMessageDTO payMessageDTO = new PayMessageDTO();
            payMessageDTO.setAmount(feePayDTO.getAmount());
            payMessageDTO.setId(feePayDTO.getFeePayId());
            payMessageDTO.setTradeNo(feePayDTO.getOutTradeNo());
            payMessageDTO.setType(PayMessageDTO.TYPE_FEE_PAY);
            rabbitTemplate.convertAndSend("wzb.topic","wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));
        }
    }
}
