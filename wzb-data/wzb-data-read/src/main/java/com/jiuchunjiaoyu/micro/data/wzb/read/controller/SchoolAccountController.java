package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.SchoolAccountMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/schoolAccount")
@Api("学校账户查询")
public class SchoolAccountController {

    @Resource
    private SchoolAccountMng schoolAccountMng;

    @ApiOperation(value = "根据班级id获取学校账户信息", notes = "根据班级id获取学校账户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/findBySchoolId", method = RequestMethod.GET)
    public BaseResult<SchoolAccount> findBySchoolId(@RequestParam Long schoolId) {
        BaseResult<SchoolAccount> baseResult = new BaseResult<>();
        baseResult.setData(schoolAccountMng.findBySchoolId(schoolId));
        return baseResult;
    }

    @ApiOperation(value = "判断班级账户是否存在", notes = "判断班级账户时候存在")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/existsBySchoolId", method = RequestMethod.GET)
    public BaseResult<Boolean> existsBySchoolId(@RequestParam Long schoolId) {
        BaseResult<Boolean> baseResult = new BaseResult<>();
        baseResult.setData(schoolAccountMng.existsBySchoolId(schoolId));
        return baseResult;
    }
}
