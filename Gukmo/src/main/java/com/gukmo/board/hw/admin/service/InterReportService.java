package com.gukmo.board.hw.admin.service;

import java.util.List;
import java.util.Map;

import com.gukmo.board.model.ReportVO;

public interface InterReportService {
	
	

	/**
	 * 신고내역 접수하기(게시글)
	 */
	boolean receiptReportBoard(String report_num);

	/**
	 * 신고내역 접수하기(댓글)
	 */
	boolean receiptReportComment(String report_num);

	/**
	 * 피신고자가 이미 정지회원인지 체크하기
	 */
	String memberStatusCheck(String nickname);

	/**
	 * 피신고자가 이미 정지등록하기
	 */
	boolean penaltyRegister(Map<String, String> paraMap);

	/**
	 * 신고내역 총 수 얻기
	 */
	int getTotalCntReport(Map<String, String> paraMap);
	
	/**
	 * 페이징 된 신고내역 리스트 얻기
	 */
	List<Map<String, String>> getReportList(Map<String, String> paraMap);

}
