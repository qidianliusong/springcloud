package com.jiuchunjiaoyu.micro.wzbOrder.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzbOrder.entity.WzbOrder;
import com.jiuchunjiaoyu.micro.wzbOrder.manager.WzbOrderMng;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/wzbOrder")
public class WzbOrderController extends CommonBaseController{

    @Resource
    private WzbOrderMng wzbOrderMng;

    @ApiOperation(value = "新建或者修改订单信息", notes = "新建或者修改订单信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public BaseResult<WzbOrder> saveOrUpdate(@RequestBody WzbOrder wzbOrder){
        BaseResult<WzbOrder> baseResult = new BaseResult<>();
        baseResult.setData(wzbOrderMng.saveOrUpdate(wzbOrder));
        return baseResult;
    }

    @ApiOperation(value = "根据id查询订单", notes = "根据id查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "订单id", name = "wzbOrderId", required = true, dataType = "Long", paramType = "query") })
    @RequestMapping(value = "findById", method = RequestMethod.GET)
    public BaseResult<WzbOrder> findById(Long wzbOrderId){
        BaseResult<WzbOrder> baseResult = new BaseResult<>();
        baseResult.setData(wzbOrderMng.findById(wzbOrderId));
        return baseResult;
    }

    @ApiOperation(value = "根据商户订单号查询订单", notes = "根据商户订单号查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "商户订单号", name = "outTradeNo", required = true, dataType = "String", paramType = "query") })
    @RequestMapping(value = "findByOutTradeNo", method = RequestMethod.GET)
    public BaseResult<WzbOrder> findByOutTradeNo(String outTradeNo){
        BaseResult<WzbOrder> baseResult = new BaseResult<>();
        baseResult.setData(wzbOrderMng.findByOutTradeNo(outTradeNo));
        return baseResult;
    }

}
