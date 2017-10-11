package com.jiuchunjiaoyu.micro.wzb.background.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.background.annotation.TokenAnnotation;
import com.jiuchunjiaoyu.micro.wzb.background.constant.WzbConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class ValidateTokenInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(ValidateTokenInterceptor.class);

    private static Gson gson = new GsonBuilder().create();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        TokenAnnotation tokenAnnotation = method.getAnnotation(TokenAnnotation.class);
        if (tokenAnnotation == null)
            return true;
        String token = request.getParameter(WzbConstant.VALIDATE_TOKEN_NAME);
        response.setCharacterEncoding("UTF-8");
        BaseResult<Void> baseResult = new BaseResult<>();
        if (StringUtils.isEmpty(token) || !token.equals(request.getSession().getAttribute(WzbConstant.VALIDATE_TOKEN_NAME))) {
//            baseResult.setCode(WzbAggregateErrEnum.validate_token_not_validate.getCode());
//            baseResult.setMessage(WzbAggregateErrEnum.validate_token_not_validate.getMessage());
            response.getWriter().write(gson.toJson(baseResult));
            return false;
        }
        request.getSession().removeAttribute(WzbConstant.VALIDATE_TOKEN_NAME);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}
