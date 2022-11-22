package com.gukmo.board.hw.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gukmo.board.model.ActivityVO;
import com.gukmo.board.model.MemberVO;

public interface InterMemberService {

	/**
	 * 일반회원 가입하기
	 * @param 사용자가 입력한 정보가 들어있는 MemberVO 객체
	 */
	void saveNormalMember(MemberVO input_member) throws Throwable;


	/**
	 * 계정 삭제하기
	 * @param attribute
	 */
	int memberDelete(String userid);

	
	
	/**
	 * 로그인되어있는 유저의 활동내역 리스트 얻기
	 * @param userid
	 * @return 활동내역 리스트
	 */
	List<ActivityVO> getActivities(String userid);


	/**
	 * 유저의 활동내역 총 갯수를 알아오기
	 * @param 검색어,유저아이디
	 * @return 활동내역 총 갯수
	 */
	int getTotalActivities(Map<String, String> paraMap);


	/**
	 * 계정찾기 메일 보내기
	 * @param 사용자의 이메일
	 * @return 이메일 성공여부가 담겨있는 JSON형식 String
	 */
	String sendEmailByMyId(String email,HttpServletRequest request);


	/**
	 * 이메일 값으로 회원 아이디 알아내기
	 * @param email
	 * @return
	 */
	String getMyID(String email);




}
