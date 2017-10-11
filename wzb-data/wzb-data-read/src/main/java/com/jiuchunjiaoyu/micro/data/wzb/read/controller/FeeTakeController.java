package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import javax.annotation.Resource;

import com.jiuchunjiaoyu.micro.data.wzb.read.constant.Constant;
import com.jiuchunjiaoyu.micro.data.wzb.read.constant.WzbDataReadErr;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.CommonBaseController;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeTakeMng;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/query/feeTake")
@Api("记账查询")
public class FeeTakeController extends CommonBaseController {

    @Resource
    private FeeTakeMng feeTakeMng;

    @ApiOperation(value = "根据班级id获取记账分页信息", notes = "根据班级id获取记账分页信息 ")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/getPageInfo", method = RequestMethod.POST)
    public BaseResult<Page<FeeTake>> getPageInfo(@RequestParam(required = true) Long classId,
                                                 @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        if(pageSize > Constant.MAX_PAGE_SIZE)
            pageSize = Constant.MAX_PAGE_SIZE;
        BaseResult<Page<FeeTake>> baseResult = new BaseResult<>();
        baseResult.setData(feeTakeMng.getPageInfo(pageNo, pageSize, classId));
        return baseResult;
    }

    @ApiOperation(value = "根据id获取记账信息", notes = "根据id获取记账信息 ")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "记账id", name = "feeTakeId", required = true, dataType = "Long", paramType = "query")})
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public BaseResult<FeeTake> getById(Long feeTakeId) {
        BaseResult<FeeTake> baseResult = new BaseResult<>();
        FeeTake feeTake = feeTakeMng.findOne(feeTakeId);
        if(feeTake == null){
            baseResult.setCode(WzbDataReadErr.entity_not_exist.getCode());
            baseResult.setMessage("不存在的记账信息，id="+feeTakeId);
            return baseResult;
        }
        baseResult.setData(feeTake);
        return baseResult;
    }
}
