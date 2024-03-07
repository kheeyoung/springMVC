package com.office.library.admin.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberDao {

	@Autowired
	JdbcTemplate jdbcTemplate; //jdbc 사용용 객체 자동 주입
	
	@Autowired
	PasswordEncoder passwordEncoder; //비번 암호화용 객체 자동 주입
	
	public boolean isAdminMember(String a_m_id) {
		System.out.println("[AdminMemberDao] isAdminMember()");
		
		String sql =  "SELECT COUNT(*) FROM tbl_admin_member " //쿼리문 작성(아이디 중복 확인 용)
				+ "WHERE a_m_id = ?";
	
		int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id);
		
		/*
		if (result > 0)
			return true;
		else
			return false;
		*/
		
		return result > 0 ? true : false; //실행후 참거짓 판별
	
	}
	
	public int insertAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] insertAdminAccount()");
		
		List<String> args = new ArrayList<String>(); //사용자 입력 정보를 리스트로 형 변환
		
		String sql =  "INSERT INTO tbl_admin_member(";
		
			   if (adminMemberVo.getA_m_id().equals("super admin")) {
				   sql += "a_m_approval, ";
				   args.add("1");
			   }
			   //쿼리문 작성 (회원 가입용)
			   sql += "a_m_id, ";
			   args.add(adminMemberVo.getA_m_id());
			   
			   sql += "a_m_pw, ";
			   args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw())); //비번은 암호화 위해 이렇게 인코더 필요
			   
			   sql += "a_m_name, ";
			   args.add(adminMemberVo.getA_m_name());
			   
			   sql += "a_m_gender, ";
			   args.add(adminMemberVo.getA_m_gender());
			   
			   sql += "a_m_part, ";
			   args.add(adminMemberVo.getA_m_part());
			   
			   sql += "a_m_position, ";
			   args.add(adminMemberVo.getA_m_position());
			   
			   sql += "a_m_mail, ";
			   args.add(adminMemberVo.getA_m_mail());
			   
			   sql += "a_m_phone, ";
			   args.add(adminMemberVo.getA_m_phone());
			   
			   sql += "a_m_reg_date, a_m_mod_date) ";
			   
			   if (adminMemberVo.getA_m_id().equals("super admin")) //아이디가 superadmin이면 ?(사용자 입력 정보) 9개(승인 여부 포함) 아님 8개
				   sql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
			   else 
				   sql += "VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
			   
		int result = -1;
		
		try {
			
			result = jdbcTemplate.update(sql, args.toArray());
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return result;
		
	}
	
	public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		
		String sql = "SELECT * FROM tbl_admin_member "+"WHERE a_m_id=? AND a_m_approval>0"; //a_m_approval가 0 이상 = 승인을 받은 회원인가 확인 
		
		List<AdminMemberVo> adminMemberVos=new ArrayList<AdminMemberVo>();
		
		try {
			RowMapper<AdminMemberVo> rowMapper=BeanPropertyRowMapper.newInstance(AdminMemberVo.class); //빈을 이용하여 매핑
			adminMemberVos=jdbcTemplate.query(sql,rowMapper,adminMemberVo.getA_m_id());
			
			if (!passwordEncoder.matches(adminMemberVo.getA_m_pw(),adminMemberVos.get(0).getA_m_pw())) { //비번 비교. 앞선 아이디 비교의 경우, 비번은 암호화 되있기 때문에 같이 못 헀다.
				adminMemberVos.clear(); //비번이 틀릴 경우, 매핑해둔 걸 비운다.
			}
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
		return adminMemberVos.size()>0 ? adminMemberVos.get(0) : null; //매핑한 게 있을 경우 (비번이 맞으면)정보를 반환, 아니면 0을 반환
	}

	
	
	public List<AdminMemberVo> selectAdmins() {
		System.out.println("[AdminMemberDao] selectAdmins()");
		
		String sql =  "SELECT * FROM tbl_admin_member"; //관리자가 조회하는 거니 전체 다 나오게 함.
	
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		
		try {
			
			RowMapper<AdminMemberVo> rowMapper=BeanPropertyRowMapper.newInstance(AdminMemberVo.class); //빈을 이용하여 매핑
			adminMemberVos=jdbcTemplate.query(sql,rowMapper);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return adminMemberVos;
		
	}

	public int updateAdminAccount(int a_m_no) {
		System.out.println("[AdminMemberDao] updateAdminAccount()");
		
		String sql = "UPDATE tbl_admin_member SET "+"a_m_approval=1 "+"WHERE a_m_no=?"; //승인 여부를 1(승인)으로 바꿈
		int result=-1;
		
		try {
			result=jdbcTemplate.update(sql, a_m_no);				
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public int updateAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] adminMemberVo()");
		String sql =  "UPDATE tbl_admin_member SET "+"a_m_name=?,"+"a_m_gender=?,"+"a_m_part=?,"+"a_m_position=?,"+"a_m_mail=?,"+"a_m_phone=?,"+"a_m_mod_date=NOW() "+"WHERE a_m_no=?"; 
		
		int result=-1;
		try {
			result=jdbcTemplate.update(sql,adminMemberVo.getA_m_name(),
					adminMemberVo.getA_m_gender(),
					adminMemberVo.getA_m_part(),
					adminMemberVo.getA_m_position(),
					adminMemberVo.getA_m_mail(),
					adminMemberVo.getA_m_phone(),
					adminMemberVo.getA_m_no());
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		return result;
	}

	public AdminMemberVo selectAdmin(int a_m_no) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		String sql =  "SELECT * FROM tbl_admin_member"+"Where a_m_no=?";
		
		List<AdminMemberVo> adminMemberVos=new ArrayList<AdminMemberVo>();
		
		try {
			adminMemberVos=jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException{
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			},a_m_no);
			}
		catch (Exception e) {
			e.printStackTrace();	
		}
		return adminMemberVos.size()>0 ? adminMemberVos.get(0):null ;
	}

	public AdminMemberVo selectAdmin(String a_m_id, String a_m_name, String a_m_mail) {
		System.out.println("[AdminMemberDao] selectAdmin()");
		String sql =  "SELECT * FROM tbl_admin_member"+" WHERE a_m_id= ? AND a_m_name= ? AND a_m_mail= ?";
		List<AdminMemberVo> adminMemberVos=new ArrayList<AdminMemberVo>();
		try {
			adminMemberVos=jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
				
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException{
					
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			},a_m_id,a_m_name,a_m_mail);
			}
		catch (Exception e) {
			e.printStackTrace();	
		}
		return adminMemberVos.size()>0 ? adminMemberVos.get(0):null ;
	}

	public int updatePassword(String a_m_id, String newPassword) {
		System.out.println("[AdminMemberDao] updatePassword()");
		String sql =  "UPDATE tbl_admin_member SET"+" a_m_pw=?, a_m_mod_date=NOW() "+"WHERE a_m_id=?";
		
		int result=-1;
		try {
			result=jdbcTemplate.update(sql,passwordEncoder.encode(newPassword),a_m_id);
			
		}
		catch (Exception e) {
			e.printStackTrace();	
		}
		return result;
	}
	
}
