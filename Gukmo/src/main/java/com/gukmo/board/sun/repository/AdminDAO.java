package com.gukmo.board.sun.repository;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAO implements InterAdminDAO {

	
	@Resource
	private SqlSessionTemplate gukmo_sql;

	
	// 기간별 새로운 게시물 수 
	@Override
	public List<Map<String, String>> newBoardCnt(Map<String, String> paraMap) {
		List<Map<String, String>> newBoardCnt_List = gukmo_sql.selectList("sun.newBoardCnt", paraMap); 
		return newBoardCnt_List;
	}


	// 특정 회원 활동내역 리스트 가져오기 
	@Override
	public List<Map<String, String>> activityList(Map<String, String> paraMap) {
		List<Map<String, String>> activityList = gukmo_sql.selectList("sun.activityList", paraMap); 
		return activityList;
	}


	// 특정 회원 일자별 활동내역 카운트 가져오기 
	@Override
	public List<Map<String, String>> activityCntList(Map<String, String> paraMap) {
		List<Map<String, String>> activityCntList = gukmo_sql.selectList("sun.activityCntList", paraMap); 
		return activityCntList;
	}


	// 특정 회원 연도별, 월별 활동내역 카운트 가져오기 
	@Override
	public List<Map<String, String>> activityCntListYearMonth(Map<String, String> paraMap) {
		List<Map<String, String>> activityCntList = gukmo_sql.selectList("sun.activityCntListYearMonth", paraMap); 
		return activityCntList;
	}


	// 특정 회원 검색어 카운트
	@Override
	public List<Map<String, String>> searchCntList(String userid) {
		List<Map<String, String>> searchCntList = gukmo_sql.selectList("sun.searchCntList", userid); 
		return searchCntList;
	}
}
