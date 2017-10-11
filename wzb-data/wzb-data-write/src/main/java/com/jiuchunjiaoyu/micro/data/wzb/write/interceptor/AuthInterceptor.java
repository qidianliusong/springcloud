package com.jiuchunjiaoyu.micro.data.wzb.write.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.result.SystemCode;
import com.jiuchunjiaoyu.micro.base.common.component.util.InternalServerAuthUtil;
import com.jiuchunjiaoyu.micro.data.wzb.write.config.AuthConfig;

/**
 * 安全验证拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {

	private static Gson gson = new GsonBuilder().create();

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

		String authorization = request.getHeader("Authorization");
		BaseResult<Void> baseResult = new BaseResult<>();
		baseResult.setCode(SystemCode.validateFail.getCode());
		baseResult.setMessage("token验证失败");
		response.setCharacterEncoding("UTF-8");

		if(authorization == null){
            response.setContentType("application/json");
			response.getWriter().write(gson.toJson(baseResult));
			return false;
		}
		String token = authorization.replace("songAuth:", "");
		String clientId = InternalServerAuthUtil.getClientIdFromToken(token);
		if (AuthConfig.validateClient(clientId)
				&& InternalServerAuthUtil.validateToken(AuthConfig.secret, token, AuthConfig.tokenExpireTime)) {
			return true;
		}
        response.setContentType("application/json");
		response.getWriter().write(gson.toJson(baseResult));
		return false;
	}

}
