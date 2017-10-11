package com.jiuchunjiaoyu.micro.aggregate.wzb.controller;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxAuthDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.GzhPayFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 获取openId之后跳转
 */
@Controller
@RequestMapping("/wzbRedirect")
public class RedirectController {

    @Value("${wzb.domain.url}")
    private String wzbDomainUrl;
    @Value("${wx.auth.domain.url:https://dev-wzb.jcweixiaoyuan.cn}")
    private String wxAuthDomainUrl;

    @Value("${authCode.redirect.url:}")
    private String redirectDomainUrl;

    private static Logger logger = LoggerFactory.getLogger(RedirectController.class);
    @Resource
    private GzhPayFeign gzhPayFeign;

    /**
     * 跳转到指定地址
     */
    @RequestMapping(value = "/callBack", method = RequestMethod.GET)
    public String redirect(@RequestParam String code, @RequestParam String targetUrl, HttpSession session) {
        try {
            if (StringUtils.isNotBlank(redirectDomainUrl) && targetUrl.startsWith(redirectDomainUrl)) {
                String encodeTargetUrl = URLEncoder.encode(targetUrl, "utf8");
                StringBuilder redirectUrlBuilder = new StringBuilder(redirectDomainUrl).append("/wzbRedirect/callBack?targetUrl=")
                        .append(encodeTargetUrl).append("&code=").append(code);
                return "redirect:" + redirectUrlBuilder.toString();
            }
            BaseResult<WxAuthDTO> wxAuthDTOBaseResult = gzhPayFeign.getOpenId(code);
            FeignUtil.validateFeignResult(wxAuthDTOBaseResult, "gzhPayFeign.getOpenId", logger);
            session.setAttribute(WzbConstant.WX_AUTH_NAME, wxAuthDTOBaseResult.getData());
        } catch (Exception e) {
            logger.error("获取微信openId失败", e);
        }
        return "redirect:" + targetUrl;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String redirect(@RequestParam(value = "target_url") String targetUrl, HttpSession session) {
        WxAuthDTO wxAuthDTO = (WxAuthDTO) session.getAttribute(WzbConstant.WX_AUTH_NAME);
        if (wxAuthDTO == null || StringUtils.isBlank(wxAuthDTO.getOpenid())) {
            String redirectUri = "";
            try {
                String encodeTargetUrl = URLEncoder.encode(targetUrl, "utf8");
                String redirectUrl = wxAuthDomainUrl + "/wzbRedirect/callBack?targetUrl=" + encodeTargetUrl;
                redirectUri = URLEncoder.encode(redirectUrl, "utf8");
            } catch (UnsupportedEncodingException e) {
                logger.error("对targetUrl进行url编码出错");
                return null;
            }

            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WzbConstant.GZH_APP_ID + "&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_base#wechat_redirect";
            return "redirect:" + url;
        }
        return "redirect:" + targetUrl;
    }

}
