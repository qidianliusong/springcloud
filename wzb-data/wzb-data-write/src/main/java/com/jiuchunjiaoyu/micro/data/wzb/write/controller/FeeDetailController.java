package com.jiuchunjiaoyu.micro.data.wzb.write.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeDetailMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费详情控制器
 */
@RestController
@RequestMapping("/operate/feeDetail")
@Api(value = "收费详情操作")
public class FeeDetailController extends CommonBaseController {
    @Resource
    private FeeDetailMng feeDetailMng;

    @ApiOperation(value = "新增收费", notes = "新增收费详情")
    @ApiImplicitParams({})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResult<FeeDetail> saveFeeDetail(@Validated @RequestBody FeeDetail feeDetail,
                                               BindingResult bindingResult) {
        BaseResult<FeeDetail> result = new BaseResult<>();
        if (!validateArgs(bindingResult, result)) {
            return result;
        }
        if (feeDetail.getFeeDetailId() != null)
            feeDetail.setFeeDetailId(null);
        Date now = new Date();
        feeDetail.setCreateTime(now);
        feeDetail.setUpdateTime(now);
        if (feeDetail.getTotalAmount() == null)
            feeDetail.setTotalAmount(new BigDecimal(0));
        FeeDetail saveFeeDetail = feeDetailMng.saveFeeDetail(feeDetail);
        result.setData(saveFeeDetail);
        return result;
    }

    @ApiOperation(value = "收费详情修改", notes = "收费详情修改")
    @ApiImplicitParams({})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResult<FeeDetail> updateFeeDetail(
            @Validated({FeeDetail.Update.class, Default.class}) @RequestBody FeeDetail feeDetail,
            BindingResult bindingResult) {
        BaseResult<FeeDetail> result = new BaseResult<>();
        if (!validateArgs(bindingResult, result)) {
            return result;
        }
        if (!feeDetailMng.exists(feeDetail.getFeeDetailId())) {
            result.setCode(SystemCode.validateFail.getCode());
            result.setMessage("不存在的feeDetailId");
            return result;
        }
        feeDetail.setUpdateTime(new Date());
        if (feeDetail.getTotalAmount() == null)
            feeDetail.setTotalAmount(new BigDecimal(0));
        FeeDetail updateFeeDetail = feeDetailMng.update(feeDetail);
        result.setData(updateFeeDetail);
        return result;
    }

    @ApiOperation(value = "收费详情删除", notes = "收费详情删除")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public BaseResult<Void> deleteFeeDetail(@RequestParam(required = true) Long feeDetailId) {
        BaseResult<Void> baseResult = new BaseResult<>();
        feeDetailMng.delete(feeDetailId);
        return baseResult;
    }

    @ApiOperation(value = "收费详情开启或关闭", notes = "收费详情开启或关闭")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public BaseResult<Void> changeStatus(Long feeDetailId) {
        BaseResult<Void> baseResult = new BaseResult<>();
        FeeDetail feeDetail = feeDetailMng.findOne(feeDetailId);
        if (feeDetail == null) {
            baseResult.setCode(WzbDataWriteErr.fee_detail_not_exists.getCode());
            baseResult.setMessage(WzbDataWriteErr.fee_detail_not_exists.getMessage());
            return baseResult;
        }
        feeDetail.setStatus(feeDetail.getStatus() == FeeDetail.STATUS_OPEN ? FeeDetail.STATUS_CLOSE : FeeDetail.STATUS_OPEN);
        feeDetailMng.save(feeDetail);
        return baseResult;
    }

}
