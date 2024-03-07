package com.office.library.admin.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminMemberVo {

	int a_m_no;			// 관리자 번호
	int a_m_approval;		// 최고 관리자 승인 여부
	String a_m_id;			// 관리자 아이디
	String a_m_pw;			// 관리자 비밀번호
	String a_m_name;		// 관리자 이름
	String a_m_gender;		// 관리자 성별 구분
	String a_m_part;		// 관리자 근무 부서
	String a_m_position;		// 관리자 업무
	String a_m_mail;		// 관리자 메일
	String a_m_phone;		// 관리자 연락처 
	String a_m_reg_date;		// 관리자 등록일
	String a_m_mod_date;		// 관리자 수정일
	
}
