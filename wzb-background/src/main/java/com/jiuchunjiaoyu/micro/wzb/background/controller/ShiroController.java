package com.jiuchunjiaoyu.micro.wzb.background.controller;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.wzb.background.constant.WzbBackgroundErr;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class ShiroController {

    @RequestMapping(value = "/noSession", method = RequestMethod.GET)
    public BaseResult<Void> noSession() {
        BaseResult<Void> baseResult = new BaseResult<>();
        baseResult.setCode(WzbBackgroundErr.session_time_out.getCode());
        baseResult.setMessage("未登录或会话已过期");
        return baseResult;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public BaseResult<Void> logout(){
        SecurityUtils.getSubject().logout();
        return new BaseResult<>();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResult<Void> login(String userName,String password){
        BaseResult<Void> baseResult = new BaseResult<>();
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
        try{
            if (!currentUser.isAuthenticated()){//使用shiro来验证
                token.setRememberMe(true);
                currentUser.login(token);//验证角色和权限
            }
        }catch (Exception e){
            baseResult.setCode(WzbBackgroundErr.login_fail.getCode());
            baseResult.setMessage(WzbBackgroundErr.login_fail.getMessage());
            return baseResult;
        }

        return baseResult;

    }


}
