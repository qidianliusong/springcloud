package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.GeneralAccountMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/generalAccount")
@Api("总账户查询")
public class GeneralAccountController {

    @Resource
    private GeneralAccountMng generalAccountMng;

    @ApiOperation(value = "获取总账", notes = "获取总账信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getGeneralAccount", method = RequestMethod.GET)
    public BaseResult<GeneralAccount> getGeneralAccount() {
        BaseResult<GeneralAccount> baseResult = new BaseResult<>();
        baseResult.setData(generalAccountMng.getGeneralAccount());
        return baseResult;
    }

}
