package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgentChild;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeAgentMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeDetailMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeePayMng;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交班费
 */
@RestController
@RequestMapping("/operate/feePay")
public class FeePayController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(FeePayController.class);

    @Resource
    private FeePayMng feePayMng;

    @Resource
    private FeeAgentMng feeAgentMng;

    @Resource
    private FeeDetailMng feeDetailMng;

    @ApiOperation(value = "批量初始化交班费记录", notes = "批量初始化交班费记录")
    @ApiImplicitParams({
            // @ApiImplicitParam(value="收费详情id",name="feeDetailId",required=true,dataType="Long",paramType="query")
    })
    @RequestMapping(value = "batchInsert", method = RequestMethod.POST)
    public BaseResult<Void> batchInsert(@RequestBody LinkedList<FeePay> feePays) {
        BaseResult<Void> baseResult = new BaseResult<>();

        feePayMng.batchSave(feePays);

        return baseResult;
    }

    @ApiOperation(value = "修改缴费信息", notes = "修改缴费信息")
    @ApiImplicitParams({
            // @ApiImplicitParam(value="收费详情id",name="feeDetailId",required=true,dataType="Long",paramType="query")
    })
    @RequestMapping(value = "updateFeePay", method = RequestMethod.POST)
    public BaseResult<FeePay> updateFeePay(@RequestBody FeePay feePay) {
        BaseResult<FeePay> baseResult = new BaseResult<>();
        if (feePay.getFeePayId() == null) {
            baseResult.setCode(WzbDataWriteErr.id_not_be_null.getCode());
            baseResult.setMessage("feePayId不能为空");
            return baseResult;
        }
        feePayMng.save(feePay);
        return baseResult;
    }

    @ApiOperation(value = "交费成功修改相关数据", notes = "交费成功修改相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "小孩id", name = "childId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "交班费记录id", name = "feePayId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "afterPay", method = RequestMethod.POST)
    public BaseResult<Void> pay(Long userId, Long childId, Long feePayId) throws WzbDataException {
        BaseResult<Void> baseResult = new BaseResult<>();
        FeePay feePay = feePayMng.findOne(feePayId);
        if (feePay == null) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("不存在的feePayid");
            return baseResult;
        }

        if (childId == null || !childId.equals(feePay.getChildId())) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("childId和feePayId不匹配");
            return baseResult;
        }

        if (feePay.getStatus().equals(FeePay.STATUS_PAY)) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("已支付完成，不能重复支付");
            return baseResult;
        }

        feePayMng.afterPay(feePayId);

        return baseResult;
    }

    /**
     * 初始化代交班费记录
     */
    @ApiOperation(value = "初始化代缴记录", notes = "初始化代缴记录")
    @RequestMapping(value = "/initFeeAgent", method = RequestMethod.POST)
    public BaseResult<FeeAgent> initFeeAgent(@RequestBody @Validated FeeAgent feeAgent, BindingResult bindingResult) {
        BaseResult<FeeAgent> baseResult = new BaseResult<>();
        if (!validateArgs(bindingResult, baseResult)) {
            return baseResult;
        }
        FeeDetail feeDetail = feeDetailMng.findOne(feeAgent.getFeeDetailId());
        if (feeDetail == null) {
            logger.warn("不存在收费详情id:" + feeAgent.getFeeDetailId());
            baseResult.setCode(WzbDataWriteErr.fee_detail_not_exists.getCode());
            baseResult.setMessage(WzbDataWriteErr.fee_detail_not_exists.getMessage());
            return baseResult;
        }

        if (feeAgent.getFeeAgentId() != null) {
            if (!feeAgentMng.exists(feeAgent.getFeeAgentId())) {
                feeAgent.setFeeAgentId(null);
            }
        } else {
            feeAgent.setFeeAgentId(null);
        }

        feeAgent.setStatus(FeePay.STATUS_NOT_PAY);
        if (feeAgent.getFeeAgentChilds() != null && !feeAgent.getFeeAgentChilds().isEmpty()) {
            for (FeeAgentChild child : feeAgent.getFeeAgentChilds()) {
                if (child != null) {
                    child.setFeeAgentChildId(null);
                    child.setFeeAgent(feeAgent);
                }
            }
            feeAgent.setChildCount(feeAgent.getFeeAgentChilds().size());
            feeAgent.setTotalAmount(feeDetail.getAmount().multiply(new BigDecimal(feeAgent.getChildCount())));
        }
        FeeAgent save = feeAgentMng.save(feeAgent);
        baseResult.setData(save);
        return baseResult;
    }

    @ApiOperation(value = "修改代缴信息", notes = "修改代缴信息")
    @RequestMapping(value = "updateFeeAgent", method = RequestMethod.POST)
    public BaseResult<FeeAgent> updateFeeAgent(@RequestBody FeeAgent feeAgent) {
        BaseResult<FeeAgent> baseResult = new BaseResult<>();
        if (feeAgent.getFeeAgentId() == null) {
            baseResult.setCode(WzbDataWriteErr.fee_agent_id_not_be_null.getCode());
            baseResult.setMessage("id不能为空");
            return baseResult;
        }
        if (!feeAgentMng.exists(feeAgent.getFeeAgentId())) {
            baseResult.setCode(WzbDataWriteErr.fee_agent_id_not_be_null.getCode());
            baseResult.setMessage("不存在的代缴id");
            return baseResult;
        }
        if (feeAgent.getFeeAgentChilds() != null && feeAgent.getFeeAgentChilds().size() > 0) {
            feeAgent.getFeeAgentChilds().stream().forEach(child -> {
                if (child != null && child.getFeeAgent() == null)
                    child.setFeeAgent(feeAgent);
            });
        }
        feeAgentMng.save(feeAgent);
        return baseResult;
    }

    @ApiOperation(value = "代缴后更改代缴信息", notes = "代缴后更改代缴信息")
    @RequestMapping(value = "/afterFeeAgent", method = RequestMethod.POST)
    public BaseResult<Void> afterFeeAgent(@RequestParam(required = false) Long userId,
                                          @RequestParam(required = true) Long feeAgentId) throws WzbDataException {
        BaseResult<Void> baseResult = new BaseResult<>();

        FeeAgent feeAgent = feeAgentMng.findOne(feeAgentId);
        if (feeAgent == null) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("不存在的feeAgentId");
            return baseResult;
        }
        if (feeAgent.getStatus() == FeeAgent.STATUS_PAY) {
            baseResult.setCode(SystemCode.validateFail.getCode());
            baseResult.setMessage("已支付完成，不能重复支付");
            return baseResult;
        }

        feeAgentMng.afterPay(feeAgentId);

        return baseResult;
    }

    @ApiOperation(value = "删除代缴信息", notes = "物理删除代缴信息(慎用)")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代缴信息id", name = "feeAgentId", required = true, dataType = "Long", paramType = "query"),})
    @RequestMapping(value = "/deleteFeeAgent", method = RequestMethod.DELETE)
    public BaseResult<Void> deleteFeeAgent(@RequestParam(required = true) Long feeAgentId) {
        BaseResult<Void> baseResult = new BaseResult<>();
        feeAgentMng.delete(feeAgentId);
        return baseResult;
    }
}
