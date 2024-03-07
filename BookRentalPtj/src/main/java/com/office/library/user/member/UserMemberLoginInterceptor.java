package com.office.library.user.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserMemberLoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		HttpSession session=request.getSession(false);
		if(session!=null) { 
			Object object =session.getAttribute("loginedUserMemberVo"); //세션에서 사용자 정보(로그인)을 받아오는 것에 성공 했다면, (=로그인 했다면)
			if(object!=null) { // 오브젝트에 받아서 오브젝트도 있으면 (=진짜 로그인 맞으면)그냥 원래 하려던 것 진행
				return true;
			}
		}
		response.sendRedirect(request.getContextPath()+"/user/member/loginForm");
		return false; //로그인 안 되어 있으면 로그인창으로. 
	}
}
