package com.gukmo.board.hw.repository;

import java.util.List;
import java.util.Map;

import com.gukmo.board.model.BoardVO;


public interface InterBoardDAO {

	/**
	 * study 게시판을 보여주기 위한 BoardVO 리스트 가져오기
	 * @param paraMap(검색어,시작rownum,끝rownum)
	 * @return BoardVO리스트
	 */
	List<BoardVO> getStudies(Map<String, String> paraMap);

	
	/**
	 * 총 게시물 건수(totalCount) 구하기
	 * @param paraMap(검색어)
	 * @return (총 게시물 건수) 를 반환한다. totalCount
	 */
	int getTotalStudiesCount(Map<String, String> paraMap);

	
}
