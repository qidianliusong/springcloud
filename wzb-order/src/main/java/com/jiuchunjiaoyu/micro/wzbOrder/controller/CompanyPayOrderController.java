package com.jiuchunjiaoyu.micro.wzbOrder.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzbOrder.entity.CompanyPayOrder;
import com.jiuchunjiaoyu.micro.wzbOrder.manager.CompanyPayOrderMng;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/wzbOrder/companyPay")
public class CompanyPayOrderController extends CommonBaseController{

    @Resource
    private CompanyPayOrderMng commpanyPayOrderMng;

    @ApiOperation(value = "新建或者修改订单信息", notes = "新建或者修改订单信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public BaseResult<CompanyPayOrder> saveOrUpdate(@RequestBody CompanyPayOrder commpanyPayOrder){
        BaseResult<CompanyPayOrder> baseResult = new BaseResult<>();
        baseResult.setData(commpanyPayOrderMng.saveOrUpdate(commpanyPayOrder));
        return baseResult;
    }

    @ApiOperation(value = "根据id查询订单", notes = "根据id查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单id", name = "commpanyPayOrderId", required = true, dataType = "Long", paramType = "query") })
    @RequestMapping(value = "findById", method = RequestMethod.GET)
    public BaseResult<CompanyPayOrder> findById(Long commpanyPayOrderId){
        BaseResult<CompanyPayOrder> baseResult = new BaseResult<>();
        baseResult.setData(commpanyPayOrderMng.findById(commpanyPayOrderId));
        return baseResult;
    }

    @ApiOperation(value = "根据商户订单号查询订单", notes = "根据商户订单号查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商户订单号", name = "outTradeNo", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "findByPartnerTradeNo", method = RequestMethod.GET)
    public BaseResult<CompanyPayOrder> findByPartnerTradeNo(String partnerTradeNo){
        BaseResult<CompanyPayOrder> baseResult = new BaseResult<>();
        baseResult.setData(commpanyPayOrderMng.findByPartnerTradeNo(partnerTradeNo));
        return baseResult;
    }

}
