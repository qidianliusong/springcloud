package com.jiuchunjiaoyu.micro.aggregate.wzb.interceptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiuchunjiaoyu.micro.aggregate.wzb.componet.SessionRedisClient;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbAggregateErrEnum;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.UserDTO;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import org.apache.commons.lang.StringUtils;
import org.phprpc.util.AssocArray;
import org.phprpc.util.PHPSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//@Component
public class UserTokenFromRedisInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(UserTokenFromRedisInterceptor.class);

    private static Gson gson = new GsonBuilder().create();

    private static PHPSerializer phpSerializer = new PHPSerializer();

    static {
        phpSerializer.setCharset("UTF-8");
    }

//    @Resource
    private SessionRedisClient sessionRedisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getParameter("token");
        // String userIdStr = request.getParameter("user_id");
        response.setCharacterEncoding("UTF-8");
        BaseResult<Void> baseResult = new BaseResult<>();
        if (StringUtils.isEmpty(token)) {
            baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
            response.getWriter().write(gson.toJson(baseResult));
            return false;
        }
        String phpObjStr = sessionRedisClient.get("laravel:" + token);
        if (StringUtils.isEmpty(phpObjStr)) {
            baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
            baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
            response.getWriter().write(gson.toJson(baseResult));
            return false;
        }
        // int userId = Integer.parseInt(userIdStr);
        try {
            Map<String, Object> map = (Map<String, Object>) phpSerializer.unserialize(phpObjStr.getBytes("utf8"));
            AssocArray aa = (AssocArray) map.get("attributes");
            if (aa == null) {
                baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
                baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
                response.getWriter().write(gson.toJson(baseResult));
                return false;
            }
            Integer id = (Integer) aa.get("id");
            if (id == null) {
                baseResult.setCode(WzbAggregateErrEnum.user_token_not_validate.getCode());
                baseResult.setMessage(WzbAggregateErrEnum.user_token_not_validate.getMessage());
                response.getWriter().write(gson.toJson(baseResult));
                return false;
            }
            byte[] userNameByte = (byte[]) aa.get("name");
            String userName = new String(userNameByte, "utf-8");
            byte[] realNameByte = (byte[]) aa.get("realname");
            String realName = new String(realNameByte,"utf-8");
            UserDTO userDTO = new UserDTO();
            userDTO.setName(userName);
            userDTO.setRealname(realName);
            userDTO.setId(id.longValue());
            userDTO.setComeFrom((Integer) aa.get("come_from"));
            request.getSession().setAttribute(WzbConstant.SESSION_USER_NAME, userDTO);
            return true;
        } catch (Exception e) {
            logger.error("反序列化php对象出错", e);
            return false;
        }

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
