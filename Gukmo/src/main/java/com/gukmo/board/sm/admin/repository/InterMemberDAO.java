package com.gukmo.board.sm.admin.repository;

import java.util.List;
import java.util.Map;

import com.gukmo.board.model.MemberVO;

public interface InterMemberDAO {
	
	/**
	 * 회원 관리 페이지의 총 페이지 수 알아오기
	 */
	int getTotalCount(Map<String, String> paraMap);

	/**
	 * 회원 관리 페이지에 보여줄 회원 목록 리스트 뽑아오기
	 */
	List<MemberVO> memberList(Map<String, String> paraMap);
	
	/**
	 * 교육기관회원 관리 페이지의 총 페이지 수 알아오기
	 */
	int getTotalCount_academy(Map<String, String> paraMap);

	/**
	 * 교육기관회원 관리 페이지에 보여줄 회원 목록 리스트 뽑아오기
	 */
	List<MemberVO> academymemberList(Map<String, String> paraMap);

}
