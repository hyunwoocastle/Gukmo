package com.gukmo.board.hasol.service;

import java.util.List;
import java.util.Map;

import com.gukmo.board.model.AdVO;
import com.gukmo.board.model.BoardVO;
import com.gukmo.board.model.HashtagVO;

public interface InterBoardService{

	// 배너 노출을 위한 select 
	// List<AdVO> getMainBannerList();
	
	// 검색 - 총 게시물 건 수를 알아오는 용도
	int getTotalCnt(Map<String, String> paraMap);

	// 검색 - 총 게시물 리스트 알아오는 용도
	List<BoardVO> getSearchList(Map<String, String> paraMap);

	// 주간 해시태그 검색 용도
	List<HashtagVO> getTopHashList();




}
