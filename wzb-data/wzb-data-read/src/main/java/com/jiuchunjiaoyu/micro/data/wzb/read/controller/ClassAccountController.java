package com.jiuchunjiaoyu.micro.data.wzb.read.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.ClassAccountMng;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/query/classAccount")
@Api("班级账户查询")
public class ClassAccountController {

    @Resource
    private ClassAccountMng classAccountMng;

    @ApiOperation(value = "根据班级id获取班级账户", notes = "根据班级id获取班级账户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/getByClassId", method = RequestMethod.GET)
    public BaseResult<ClassAccount> queryClassAccountByClassId(@RequestParam(required = true) Long classId) {
        BaseResult<ClassAccount> baseResult = new BaseResult<>();
        baseResult.setData(classAccountMng.findByClassId(classId));
        return baseResult;
    }

    @ApiOperation(value = "判断班级账户是否存在", notes = "判断班级账户时候存在")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "班级id", name = "classId", required = true, dataType = "Long", paramType = "query")
    })
    @RequestMapping(value = "/existsByClassId", method = RequestMethod.GET)
    public BaseResult<Boolean> classAccountExists(@RequestParam(required = true) Long classId) {
        BaseResult<Boolean> baseResult = new BaseResult<>();
        baseResult.setData(classAccountMng.existsByClassId(classId));
        return baseResult;
    }

    @ApiOperation(value = "获取班级账户分页信息", notes = "获取班级账户分页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学校id", name = "schoolId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "班级名称（模糊查询）", name = "className", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "页码", name = "pageNo", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(value = "pageSize", name = "pageSize", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/getClassAccountPage", method = RequestMethod.GET)
    public BaseResult<Page<ClassAccount>> getClassAccountPage(@RequestParam(required = true) long schoolId, String className, @RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        BaseResult<Page<ClassAccount>> baseResult = new BaseResult<>();
        baseResult.setData(classAccountMng.getClassAccountPage(schoolId, className, pageNo, pageSize));
        return baseResult;
    }
}
