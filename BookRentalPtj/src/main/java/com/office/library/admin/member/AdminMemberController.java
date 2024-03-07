package com.office.library.admin.member;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller 
@RequestMapping("/admin/member")
public class AdminMemberController {
	
	@Autowired
	AdminMemberService adminMemberService;
	
	//회원가입
	//@RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)
	@GetMapping("/createAccountForm")
	public String createAccountForm() {
		System.out.println("[AdminMemberController] createAccountForm()");
		
		String nextPage = "admin/member/create_account_form";
		
		return nextPage;
		
	}
	
	//회원가입
	//@RequestMapping(value = "/createAccountConfirm", method = RequestMethod.POST)
	@PostMapping("/createAccountConfirm")
	public String createAccountConfirm(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberController] createAccountConfirm()");
		
		String nextPage = "admin/member/create_account_ok";
		
		int result = adminMemberService.createAccountConfirm(adminMemberVo);
		
		if (result <= 0)
			nextPage = "admin/member/create_account_ng";
		
		return nextPage;
		
	}
	
	//로그인
	@GetMapping("/loginForm")
	public String loginForm() {
		System.out.println("[AdminMemberController] loginForm()");
		
		String nextPage = "admin/member/login_form";
		
		return nextPage;
	}
	
	
	//로그인 확인
	@PostMapping("/loginConfirm")
	public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
		System.out.println("[AdminMemberController] loginConfirm()");
		
		String nextPage = "admin/member/login_ok";
		
		AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);
		
		if(loginedAdminMemberVo == null) {
			nextPage = "admin/member/login_ng";
		}
		else { //로그인 성공 할 경우 세션 설정
			session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo); //세션명loginedAdimnMemberVo 으로 loginedAdimnMemberVo 저장
			session.setMaxInactiveInterval(60*30); //세션 유지 시간을 60초 *30 해서 30분 유지
		}
		
		return nextPage;
	}
	
	//로그아웃 확인
	@RequestMapping(value="/logoutConfirm",method=RequestMethod.GET)
	public String logoutConfirm(HttpSession session) {
		System.out.println("[AdminMemberController] logoutConfirm()");
		
		String nextPage = "redirect:/admin";
		session.invalidate(); //세션 지우기 
		return nextPage;
	}
	
	//관리자 목록
	@RequestMapping(value="/listupAdmin",method=RequestMethod.GET)
	public String listupAdmin(Model model) {
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		
		String nextPage = "admin/member/listup_admins";
		
		List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();
		
		model.addAttribute("adminMemberVos",adminMemberVos); //모델에 데이터 추가
		
		return nextPage;
	}
	
	//관리자 승인
	@RequestMapping(value="/setAdminApproval",method=RequestMethod.GET)
	public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
		System.out.println("[AdminMemberController] setAdminApproval()");
		
		String nextPage = "redirect:/admin/member/listupAdmin";
		adminMemberService.setAdminApproval(a_m_no);
		return nextPage;
	}
	
	//회원 정보 수정
	@RequestMapping(value="/modifyAccountForm",method=RequestMethod.GET)
	public String modifyAccountForm(HttpSession session) {
		System.out.println("[AdminMemberController] modifyAccountForm()");
		
		String nextPage = "/admin/member/modify_account_form";
		AdminMemberVo loginedAdminMemberVo =(AdminMemberVo) session.getAttribute("loginedAdminMemberVo"); //세션으로 부터 로그인 된 정보를 받아온다.
		if(loginedAdminMemberVo==null) { //로그인 된 것이 없을 경우
			nextPage = "redirect:/admin/member/loginForm"; //로그인창으로 
		}
		return nextPage;
	}
	
	//회원 정보 수정 확인
	@PostMapping(value="/modifyAccountConfirm")
	public String modifyAccountConfirm(AdminMemberVo adminMemberVo,HttpSession session) {
		System.out.println("[AdminMemberController] modifyAccountConfirm()");
		
		String nextPage = "/admin/member/modify_account_ok";
		int result=adminMemberService.modifyAccountConfirm(adminMemberVo); //서비스로 수정을 하라고 하고 그 결과를 받아옴 
		
		if(result>0) { //수정 성공시 세션 설정. 
			AdminMemberVo loginedAdminMemberVo=adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no());
			session.setAttribute("loginedAdminMemberVo",loginedAdminMemberVo);
			session.setMaxInactiveInterval(60*30);
		}
		else {
			nextPage = "redirect:/admin/member/modify_account_ng"; // 오류시 오류 페이지 반환
		}
		return nextPage;
	}
	
	//비번 찿기
	@GetMapping(value="/findPasswordForm")
	public String findPasswordForm() {
		System.out.println("[AdminMemberController] findPasswordForm()");
		
		String nextPage = "/admin/member/find_password_form";
		
		return nextPage;
	}
	
	//비번 찾기 확인
		@PostMapping(value="/findPasswordConfirm")
		public String findPasswordConfirm(AdminMemberVo adminMemberVo) {
			System.out.println("[AdminMemberController] findPasswordConfirm()");
			
			String nextPage = "/admin/member/find_password_ok";
			int result=adminMemberService.findPasswordConfirm(adminMemberVo); //서비스로 비번 찾기를 하라고 하고 그 결과를 받아옴 
			
			if(result<=0) { //받아오기 실퍄시
				nextPage = "/admin/member/find_password_ng";
			}
			
			return nextPage;
		}
}