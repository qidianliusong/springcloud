package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.annotation.TokenAnnotation;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.*;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.page.PageDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.manager.CompanyPayMng;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.GsonUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.util.RandomUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

/**
 * 取款记录控制器
 */
@RestController
@RequestMapping("/wzb/feeDrawRecord")
public class FeeDrawRecordController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeeDrawRecordController.class);

    private static final String DATE_FORMAT_FOR_RANDOM = "yyyyMMddHHmmssSSS";
    private static final int RANDOM_LEN = 10;
    @Resource
    private WzbDataReadFeign readFeign;

    @Resource
    private WzbDataWriteFeign writeFeign;

    @Resource
    private CompanyPayMng companyPayMng;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @ApiOperation(value = "获取取款信息分页数据", notes = "获取取款信息分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    public BaseResult<PageDTO<FeeDrawRecordDTO>> getPage(@RequestParam(required = true) Long classId,
                                                         @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        BaseResult<PageDTO<FeeDrawRecordDTO>> baseResult = new BaseResult<>();
        BaseResult<PageDTO<FeeDrawRecordDTO>> feeDrawRecordPage = readFeign.getFeeDrawRecordPage(classId, pageNo - 1, pageSize);
        if (!validateFeignResult(baseResult, feeDrawRecordPage, "readFeign.getFeeDrawRecordPage", logger)) {
            return baseResult;
        }
        baseResult.setData(feeDrawRecordPage.getData());
        return baseResult;
    }

    @TokenAnnotation
    @ApiOperation(value = "取款", notes = "取款")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "防止重复提交token", name = "validateToken", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public BaseResult<Void> draw(@Validated FeeDrawRecordDTO feeDrawRecordDTO, BindingResult bindingResult, HttpSession session, HttpServletRequest request) throws WzbAggregateException {
        BaseResult<Void> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult))
            return baseResult;
        WxAuthDTO wxAuthDTO = (WxAuthDTO) session.getAttribute(WzbConstant.WX_AUTH_NAME);
        if (wxAuthDTO == null) {
            baseResult.setCode(WzbAggregateErrEnum.wx_openid_is_null.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.wx_openid_is_null.getMessage());
            return baseResult;
        }
        BaseResult<ClassAccountDTO> classAccountDTOBaseResult = readFeign.getClassAccountByClassId(feeDrawRecordDTO.getClassId());
        FeignUtil.validateFeignResult(classAccountDTOBaseResult, "readFeign.getClassAccountByClassId", logger);
        ClassAccountDTO classAccountDTO = classAccountDTOBaseResult.getData();
        if (classAccountDTO.getAmount().compareTo(feeDrawRecordDTO.getAmount()) < 0) {
            baseResult.setCode(WzbAggregateErrEnum.amount_not_enough.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.amount_not_enough.getMessage());
            return baseResult;
        }

        BaseResult<Integer> prePayFeeDrawRecordsCountBaseResult = readFeign.getprePayFeeDrawRecordsCount(getDateBefor(1),feeDrawRecordDTO.getClassId());
        FeignUtil.validateFeignResult(prePayFeeDrawRecordsCountBaseResult, "readFeign.getprePayFeeDrawRecordsCount", logger);

        if (prePayFeeDrawRecordsCountBaseResult.getData() > 0) {
            logger.warn("班级存在正在进行的取款操作,classId="+feeDrawRecordDTO.getClassId());
            baseResult.setCode(WzbAggregateErrEnum.pay_repect_err.getCode());
            baseResult.setMessage("该班级存在正在进行的取款操作");
            return baseResult;
        }

        String partnerTradeNo = RandomUtil.createRandomStrByTime(DATE_FORMAT_FOR_RANDOM, RANDOM_LEN);
        UserDTO userDTO = (UserDTO) session.getAttribute(WzbConstant.SESSION_USER_NAME);
        feeDrawRecordDTO.setUserId(userDTO.getId());
        feeDrawRecordDTO.setUserName(userDTO.getKinsfolk());
        feeDrawRecordDTO.setPartnerTradeNo(partnerTradeNo);
        BaseResult<FeeDrawRecordDTO> feeDrawRecordDTOBaseResult = writeFeign.saveOrUpdateFeeDrawRecord(feeDrawRecordDTO);
        FeignUtil.validateFeignResult(feeDrawRecordDTOBaseResult, "writeFeign.saveOrUpdateFeeDrawRecord", logger);
        FeeDrawRecordDTO feeDrawRecordDTOResult = feeDrawRecordDTOBaseResult.getData();

        CompanyPayOrderDTO companyPayOrderDTO = new CompanyPayOrderDTO();
        companyPayOrderDTO.setAmount(feeDrawRecordDTO.getAmount().multiply(new BigDecimal(100)).intValue());
        companyPayOrderDTO.setReUserName(userDTO.getRealname());
        companyPayOrderDTO.setPartnerTradeNo(partnerTradeNo);
        companyPayOrderDTO.setOpenid(wxAuthDTO.getOpenid());
        try {
            companyPayOrderDTO.setSpbillCreateIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            logger.warn("获取本机ip失败");
            companyPayOrderDTO.setSpbillCreateIp("1.1.1.1");
        }

        CompanyPayResponseDTO companyPayResponseDTO = companyPayMng.draw(companyPayOrderDTO);

        if (!"SUCCESS".equals(companyPayResponseDTO.getReturnCode())) {
            baseResult.setCode(WzbAggregateErrEnum.company_pay_err.getCode());
            baseResult.setMessage(companyPayResponseDTO.getReturnMsg());
            return baseResult;
        }

        if (!"SUCCESS".equals(companyPayResponseDTO.getResultCode())) {
            baseResult.setCode(WzbAggregateErrEnum.company_pay_err.getCode());
            baseResult.setMessage(companyPayResponseDTO.getErrCodeDes());
            return baseResult;
        }

        try {
            writeFeign.afterDraw(feeDrawRecordDTOResult.getFeeDrawRecordId());
        } catch (Exception e) {
            logger.error("支付完成修改取款状态出错", e);
        }

        //发送mq
        PayMessageDTO payMessageDTO = new PayMessageDTO();
        payMessageDTO.setAmount(feeDrawRecordDTO.getAmount());
        payMessageDTO.setId(feeDrawRecordDTO.getFeeDrawRecordId());
        payMessageDTO.setTradeNo(feeDrawRecordDTO.getPartnerTradeNo());
        payMessageDTO.setType(PayMessageDTO.TYPE_DRAW);
        rabbitTemplate.convertAndSend("wzb.topic", "wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));

        return baseResult;
    }

    private Date getDateBefor(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -days);
        return c.getTime();
    }

}
