package org.s.api.admin.controller;

import org.s.api.admin.controller.annotation.PermessionLimit;
import org.s.api.admin.core.model.ReturnT;
import org.s.api.admin.core.model.XxlApiUser;
import org.s.api.admin.core.util.tool.StringTool;
import org.s.api.admin.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {
	@Value("${org.s.apiadmin.guestuser:guest}")
	String guestName;
	
	@Value("${org.s.apiadmin.guestpass:guest}")
	String guestPass;

	@Resource
	private LoginService loginService;

	@RequestMapping("/")
	@PermessionLimit(limit=false)
	public String index(Model model, HttpServletRequest request) {
		XxlApiUser loginUser = loginService.ifLogin(request);
		if (loginUser == null) {
			return "redirect:/toLogin";
		}
		return "redirect:/project";
	}
	
	@RequestMapping("/toLogin")
	@PermessionLimit(limit=false)
	public String toLogin(Model model, HttpServletRequest request) {
		XxlApiUser loginUser = loginService.ifLogin(request);
		if (loginUser != null) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String ifRemember, String userName, String password){
		// param
		boolean ifRem = false;
		if (StringTool.isNotBlank(ifRemember) && "on".equals(ifRemember)) {
			ifRem = true;
		}

		// do login
		ReturnT<String> loginRet = loginService.login(response,userName, password, ifRem);
		return loginRet;
	}
	
	@RequestMapping(value="guestlogin", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> guestlogin(HttpServletRequest request, HttpServletResponse response, String ifRemember, String userName, String password){


		// do login
		ReturnT<String> loginRet = loginService.login(response, guestName, guestPass, false);
		return loginRet;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		loginService.logout(request, response);
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/help")
	public String help() {
		return "help";
	}
	
}
