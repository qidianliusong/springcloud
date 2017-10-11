package com.jiuchunjiaoyu.micro.wzb.background.controller;

import com.jiuchunjiaoyu.micro.wzb.background.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 防止重复提交控制器
 */
@Api(value = "获取防止重复提交的token")
@RestController
@RequestMapping("/wzb/validate")
public class TokenController extends CommonBaseController {

    @ApiOperation(value = "获取validateToken", notes = "获取validateToken")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "token", name = "token", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public BaseResult<String> getToken(HttpSession session) {
        BaseResult<String> baseResult = new BaseResult<>();
        String token = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute(WzbConstant.VALIDATE_TOKEN_NAME, token);
        baseResult.setData(token);
        return baseResult;
    }

}
