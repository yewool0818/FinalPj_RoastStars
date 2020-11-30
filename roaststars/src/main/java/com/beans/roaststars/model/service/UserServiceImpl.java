package com.beans.roaststars.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beans.roaststars.model.mapper.UserMapper;
import com.beans.roaststars.model.vo.AuthorityVO;
import com.beans.roaststars.model.vo.UserVO;

@Service
public class UserServiceImpl implements UserService{
	@Resource
	private UserMapper userMapper;
	/*
	 *  비밀번호 암호화처리를 위한 객체를 주입받는다
	 *  spring-security.xml 에서 bean 설정이 되어 있음 	
	 */
	@Resource
	private BCryptPasswordEncoder passwordEncoder;
	
	// 회원가입시 반드시 권한까지 부여되도록 트랜잭션 처리한다
	@Transactional
	@Override
	public void registerUser(UserVO vo) {
		// 비밀번호를 bcrypt 알고리즘으로 암호화하여 DB에 저장한다
		String encodedPwd = passwordEncoder.encode(vo.getPassword());
		vo.setPassword(encodedPwd);
		userMapper.registerUser(vo);
		AuthorityVO authority = new AuthorityVO(vo.getId(), "ROLE_MEMBER");
		userMapper.registerRole(authority);
	}
	
	@Override
	public void withdrawalUser(String id) {
	}
	@Override
	public String idCheck(String id) {
		int count = userMapper.idcheck(id);
		return (count == 0) ? "ok" : "fail";
	}
	@Override
	public String nickCheck(String nickname) {
		int count = userMapper.nickcheck(nickname);
		return (count == 0) ? "ok" : "fail";
	}
	@Override
	public UserVO findUserById(String id) {
		return  userMapper.findUserById(id);
	}
	
	@Override
	public void grantAuthorization(String id, String authority) {
	}
	
	@Override
	public List<AuthorityVO> selectAuthorityByUsername(String username) {
		return userMapper.selectAuthorityByUsername(username);
	}
	
}
