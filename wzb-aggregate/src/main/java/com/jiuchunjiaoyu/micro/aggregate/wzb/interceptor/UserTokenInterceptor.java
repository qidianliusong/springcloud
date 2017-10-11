package com.jiuchunjiaoyu.micro.aggregate.wzb.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.PhpResult;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result.ResultData;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.UserFeign;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(UserTokenInterceptor.class);

    private static Gson gson = new GsonBuilder().create();
    @Lazy
    @Autowired
    private UserFeign userFeign;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getParameter("token");
        String userIdStr = request.getParameter("userId");
        response.setCharacterEncoding("UTF-8");
        BaseResult<Void> baseResult = new BaseResult<>();
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userIdStr) || !StringUtils.isNumeric(userIdStr)) {
            baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
            response.getWriter().write(gson.toJson(baseResult));
            return false;
        }
        PhpResult<ResultData<UserDTO>> phpResult = null;
        try {
            phpResult = userFeign.getUserInfo(token, Long.parseLong(userIdStr), 1);

        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
            response.getWriter().write(gson.toJson(baseResult));
            return false;
        }
        if (phpResult == null || SystemCode.success.getCode() != phpResult.getCode() || phpResult.getData() == null || phpResult.getData().getResult() == null) {
            baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
            response.getWriter().write(gson.toJson(baseResult));
            return false;
        }

        UserDTO userDTO = phpResult.getData().getResult();
        UserDTO userInSession = (UserDTO) request.getSession().getAttribute(WzbConstant.SESSION_USER_NAME);
        if ( userInSession == null)
            request.getSession().setAttribute(WzbConstant.SESSION_USER_NAME, userDTO);
        else{
            if(!userInSession.getId().equals(userDTO.getId())){
                request.getSession().setAttribute(WzbConstant.SESSION_USER_NAME, userDTO);
            }
        }

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
