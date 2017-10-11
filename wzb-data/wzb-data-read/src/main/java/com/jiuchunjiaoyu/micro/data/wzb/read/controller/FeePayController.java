package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.jiuchunjiaoyu.micro.data.wzb.read.constant.WzbDataReadErr;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeePayMng;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/query/feePay")
@Api("缴费查询")
public class FeePayController {

    @Resource
    private FeePayMng feePayMng;

    @ApiOperation(value = "根据缴费详情id获取缴费信息列表", notes = "根据缴费详情id获取缴费信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "支付状态:0为未支付,1为已支付,不传递该值为查询全部", name = "payStatus", required = false, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<List<FeePay>> findByClassId(@RequestParam(required = true) Long feeDetailId, Integer payStatus) {
        BaseResult<List<FeePay>> baseResult = new BaseResult<>();
        if (payStatus == null) {
            baseResult.setData(feePayMng.findByFeeDetailId(feeDetailId));
        } else if (payStatus.equals(0)) {
            baseResult.setData(feePayMng.findByFeeDetailId(feeDetailId, 0, 1, 3));
        } else {
            baseResult.setData(feePayMng.findByFeeDetailId(feeDetailId, 2));
        }
        return baseResult;
    }

    @ApiOperation(value = "获取缴费信息", notes = "根据收费详情id和孩子id获取缴费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "孩子id", name = "childId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public BaseResult<FeePay> getFeePay(@RequestParam(required = true) Long feeDetailId,
                                        @RequestParam(required = true) Long childId) {
        BaseResult<FeePay> baseResult = new BaseResult<>();
        FeePay feePay = feePayMng.findByFeeDetailIdAndChildId(feeDetailId, childId);
        if(feePay == null){
            baseResult.setCode(WzbDataReadErr.entity_not_exist.getCode());
            baseResult.setMessage("不存在的缴费信息");
            return baseResult;
        }
        baseResult.setData(feePay);
        return baseResult;
    }

    @ApiOperation(value = "根据id获取缴费信息", notes = "根据id获取缴费信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "收费详情id", name = "feeDetailId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public BaseResult<FeePay> getFeePay(@RequestParam(required = true) Long feePayId) {
        BaseResult<FeePay> baseResult = new BaseResult<>();
        baseResult.setData(feePayMng.findOne(feePayId));
        return baseResult;
    }

    @ApiOperation(value = "获取等待支付状态的缴费信息", notes = "获取等待支付状态的缴费信息")
    @RequestMapping(value = "/getPreFeePays", method = RequestMethod.GET)
    public BaseResult<List<FeePay>> getPreFeePays(Date startTime) {
        BaseResult<List<FeePay>> baseResult = new BaseResult<>();
        if (startTime == null) {
            startTime = new Date();
        }
        baseResult.setData(feePayMng.getPreFeePays(startTime));
        return baseResult;
    }
}
