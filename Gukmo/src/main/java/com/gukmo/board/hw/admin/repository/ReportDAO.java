package com.gukmo.board.hw.admin.repository;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.gukmo.board.model.ReportVO;

@Repository
public class ReportDAO implements InterReportDAO{
	@Resource
	private SqlSessionTemplate gukmo_sql;

	@Override
	public int getTotalCount_report(Map<String, String> paraMap) {
		int n = gukmo_sql.selectOne("ksm.getTotalCount_report", paraMap);
		return n;
	}

	// 신고리스트 불러오기
	@Override
	public List<ReportVO> reportList(Map<String, String> paraMap) {
		List<ReportVO> reportList = gukmo_sql.selectList("ksm.reportList" ,paraMap);
		return reportList;
	}

	// 신고내역 상세보기
	@Override
	public ReportVO reportDetail(Map<String, String> paraMap) {
		ReportVO reportDetail = gukmo_sql.selectOne("ksm.getReportDetail", paraMap);
		return reportDetail;
	}

	/**
	 * 피신고자 닉네임으로 회원아이디 얻기
	 */
	@Override
	public String getReportedId(String reportedNickname) {
		String ReportedId = gukmo_sql.selectOne("ksm.getReportedId", reportedNickname);
		return ReportedId;
	}

	
	
	
	/**
	 * 신고내역 접수처리하기(게시글)
	 */
	@Override
	public int receiptReportBoard(String report_num) {
		int result = gukmo_sql.update("chw.receiptReportBoard", report_num);
		return result;
	}

	/**
	 * 신고내역 접수처리하기(댓글)
	 */
	@Override
	public int receiptReportComment(String report_num) {
		int result = gukmo_sql.update("chw.receiptReportComment", report_num);
		return result;
	}

	/**
	 * 피신고자가 이미 정지회원인지 체크하기
	 */
	@Override
	public String memberStatusCheck(String reportedNickname) {
		String userid = gukmo_sql.selectOne("ksm.getReportedId", reportedNickname);
		String status = gukmo_sql.selectOne("chw.memberStatusCheck", userid);
		return status;
	}

	/**
	 * 정지내역에 등록하기
	 */
	@Override
	public int penaltyNew(Map<String, String> paraMap) {
		int result = gukmo_sql.insert("chw.penaltyNew", paraMap);
		return result;
	}

	/**
	 * 회원 정지로 바꿔주기
	 */
	@Override
	public int memberStatusChange(Map<String, String> paraMap) {
		String reportedNickname = paraMap.get("nickname");
		String userid = gukmo_sql.selectOne("ksm.getReportedId", reportedNickname);
		paraMap.put("userid",userid);
		int result = gukmo_sql.update("chw.memberStatusChange", paraMap);
		return result;
	}
}