package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxAuthDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.GzhPayFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 * 微信授权控制器
 */
@RestController
@RequestMapping("/wzb/wxAuth")
public class WxAuthController extends CommonBaseController {

    private static Logger logger = LoggerFactory.getLogger(WxAuthController.class);
    @Resource
    private GzhPayFeign gzhPayFeign;

    @ApiOperation(value = "获取openId", notes = "后台获取openId")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "微信授权返回的code", name = "code", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getOpenId", method = RequestMethod.GET)
    public BaseResult<Void> getOpenId(String code, HttpSession session) throws WzbAggregateException {
        BaseResult<Void> baseResult = new BaseResult<>();
        BaseResult<WxAuthDTO> wxAuthDTOBaseResult = gzhPayFeign.getOpenId(code);
        FeignUtil.validateFeignResult(wxAuthDTOBaseResult, "gzhPayFeign.getOpenId", logger);
        session.setAttribute(WzbConstant.WX_AUTH_NAME, wxAuthDTOBaseResult.getData());
        return baseResult;
    }

    @ApiOperation(value = "获取跳转地址", notes = "返回值中data数据为前端需要跳转的url")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "微信授权返回的code", name = "redirectUrl", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getRedirectUrl", method = RequestMethod.GET)
    public BaseResult<String> getRedirectUrl(@RequestParam(defaultValue = "https://dev-wzb.jcweixiaoyuan.cn/") String redirectUrl, HttpSession session) {
        BaseResult<String> baseResult = new BaseResult<>();
        WxAuthDTO wxAuthDTO = (WxAuthDTO) session.getAttribute(WzbConstant.WX_AUTH_NAME);
        if (wxAuthDTO == null || StringUtils.isBlank(wxAuthDTO.getOpenid())) {
            baseResult.setData("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WzbConstant.GZH_APP_ID + "&redirect_uri=https%3a%2f%2fdev-wzb.jcweixiaoyuan.cn%2fredirect&response_type=code&scope=snsapi_base#wechat_redirect");
            session.setAttribute(WzbConstant.REDIRECT_URL_IN_SESSION,redirectUrl);
            return baseResult;
        }
        baseResult.setData(redirectUrl);
        return baseResult;
    }


    @ApiOperation(value = "用于微信验证token", notes = "用于微信验证token")
    @RequestMapping(value = "validateToken", method = RequestMethod.GET)
    public String validateToken(String signature, String timestamp, String nonce, String echostr) {

        String[] str = {WzbConstant.TOKEN_FOR_WX, timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];

        return echostr;
    }

}
