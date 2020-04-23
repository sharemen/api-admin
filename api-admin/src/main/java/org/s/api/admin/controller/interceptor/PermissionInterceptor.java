package org.s.api.admin.controller.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.s.api.admin.controller.annotation.PermessionLimit;
import org.s.api.admin.core.model.XxlApiUser;
import org.s.api.admin.core.util.tool.StringTool;
import org.s.api.admin.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 权限拦截
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Value("${org.s.apiadmin.guestuser:guest}")
	String guestName;
	
	@Resource
	private LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		// if need login
		boolean needLogin = true;
		boolean needAdminuser = false;
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		if (permission!=null) {
			needLogin = permission.limit();
			needAdminuser = permission.superUser();
		}

		// if pass
		if (needLogin) {
			XxlApiUser loginUser = loginService.ifLogin(request);
			
			if (loginUser == null) {
				//保留登录后的自动跳转回当前要访问的url
				String requestUrl= request.getRequestURI()+"?"+request.getQueryString();
				response.sendRedirect(request.getContextPath() + "/toLogin?backurl="+StringTool.escape(requestUrl));	//request.getRequestDispatcher("/toLogin").forward(request, response);
				return false;
			}
			if (needAdminuser && loginUser.getType()!=1) {
				throw new RuntimeException("权限拦截");
			}
			    request.setAttribute(LoginService.LOGIN_IDENTITY, loginUser);
			    request.setAttribute(LoginService.LOGIN_GUEST, false);
			//如果是访客用户限制操作权限
			if(guestName.equals(loginUser.getUserName())) {
				request.setAttribute(LoginService.LOGIN_GUEST,true);
			}
		}

		return super.preHandle(request, response, handler);
	}
	
}
