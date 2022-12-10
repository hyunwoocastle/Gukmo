package com.gukmo.board.hgb.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gukmo.board.hgb.repository.InterBoardDAO;
import com.gukmo.board.model.BoardVO;
import com.gukmo.board.model.CommentVO;
import com.gukmo.board.model.HashtagVO;
import com.gukmo.board.common.FileManager;


@Service
public class BoardService implements InterBoardService{
	
	@Autowired
	private InterBoardDAO dao;
	
	@Autowired   // Type 에 따라 알아서 Bean 을 주입해준다.
	private FileManager fileManager;

	
	
	/**
	 * 하나의 글 불러오기
	 * @param 글번호 board_num
	 * @return BoardVO
	 */
	@Override
	public BoardVO getBoardDetail(int board_num) {
		
		//상세카테고리 알아오기
		String detail_category = dao.getCategory(board_num);				
		
		Map<String,String> paraMap = new HashMap<>();
		
		paraMap.put("board_num", board_num+"");
		paraMap.put("detail_category",detail_category);
		
		BoardVO board = dao.getBoardDetail(paraMap);
		
		
		
		
		return board;
	}


	// === 글삭제 하기 === //
	@Override
	public int del(Map<String, String> paraMap) {
		
		int n = dao.del(paraMap);
		
		return n;
	}




	
	
	/**
	 * 좋아요 처리하기
	 * @param paraMap(글번호,userid)
	 */
	@Override
	public String likeProcess(Map<String, String> paraMap) {
		int likeCnt = dao.likeCheck(paraMap);	//좋아요 체크하기
		String likeResult = "";
		int result = 0;
		if(likeCnt > 0) {	//좋아요를 눌렀다면
			result = dao.likeDelete(paraMap); //좋아요 테이블에 delete하기
			likeResult = "delete";
		} else {	//좋아요를 누르지 않았다면
			result = dao.likeInsert(paraMap);	//좋아요 테이블에 insert하기
			likeResult = "insert";
		}
		
		if(result != 1) {	//delete나 insert 성공시
			likeResult = "error";
		}
		
		return likeResult;
		
	}
	
	
	
	/**
	 * 댓글 좋아요 처리하기
	 * @param paraMap(글번호,userid)
	 */
	@Override
	public String comment_likeProcess(Map<String, String> paraMap) {
		int comment_likeCnt = dao.comment_likeCheck(paraMap);	//좋아요 체크하기
		String comment_likeResult = "";
		int result = 0;
		if(comment_likeCnt > 0) {	//좋아요를 눌렀다면
			result = dao.comment_likeDelete(paraMap); //좋아요 테이블에 delete하기
			comment_likeResult = "delete";
		} else {	//좋아요를 누르지 않았다면
			result = dao.comment_likeInsert(paraMap);	//좋아요 테이블에 insert하기
			comment_likeResult = "insert";
		}
		
		if(result != 1) {	//delete나 insert 성공시
			comment_likeResult = "error";
		}
		
		return comment_likeResult;
	}


	// 글 상세페이지 진입시 로그인한 회원의 좋아요여부 체크하기
	@Override
	public int likethis(Map<String, String> paraMap) {
		
		int likethis = dao.likethis(paraMap);
		
		return likethis;
	}
	
	// 글 상세페이지 진입시 로그인한 회원의 좋아요여부 체크하기
	@Override
	public int comment_likethis(Map<String, String> paraMap) {
		
		int comment_likethis = dao.comment_likethis(paraMap);
		
		return comment_likethis;
	}

	// 댓글 쓰기
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int addComment(Map<String, String> paraMap) {
		int result1=0, result2=0, result3=0;
		
		// tbl_comment 테이블에 추가, tbl_board 의 comment_cnt +1, 해당 회원의 포인트 10점 증가
		
		// 댓글 테이블에 insert
		result1 = dao.addComment(paraMap);
		
		// tbl_board의 comment_cnt 컬럼에  +1
		if(result1 == 1 ) {
			result2 = dao.addComment_cnt(paraMap);
		}
		
		// 해당 회원의 포인트 10점 증가
		if(result2 == 1) {
			result3 = dao.addPoint(paraMap);
		}
		
		if(result1 * result2 * result3 == 1) {
			return 1;
		}
		else {
			return 2;
		}
		
	}
	
	
	
	// *대댓글* tbl_comment 테이블에 추가, tbl_board 의 comment_cnt +1, 해당 회원의 포인트 10점 증가
		@Override
		public int addComment_of_Comment(Map<String, String> paraMap) {
			
			int result1=0, result2=0, result3=0;
			
			// tbl_comment 테이블에 추가, tbl_board 의 comment_cnt +1, 해당 회원의 포인트 10점 증가
			
			// *대댓글* 댓글 테이블에 insert
			result1 = dao.addComment_of_Comment(paraMap);
			
			// tbl_board의 comment_cnt 컬럼에  +1
			if(result1 == 1 ) {
				result2 = dao.addComment_cnt(paraMap);
			}
			
			// 해당 회원의 포인트 10점 증가
			if(result2 == 1) {
				result3 = dao.addPoint(paraMap);
			}
			
			if(result1 * result2 * result3 == 1) {
				return 1;
			}
			else {
				return 2;
			}
			
		}


		//기본 댓글 리스트 불러오기(기본 : 그냥 댓글, 특수 : 대댓글)
		@Override
		public List<CommentVO> getBasic_commentList(Map<String, String> paraMap) {
			List<CommentVO> basic_commentList = dao.getBasic_commentList(paraMap);
			return basic_commentList;
		}

		//기본 댓글 리스트 불러오기(기본 : 그냥 댓글, 특수 : 대댓글)
		@Override
		public List<CommentVO> getSpecial_commentList(Map<String, String> paraMap) {
			List<CommentVO> special_commentList = dao.getSpecial_commentList(paraMap);
			return special_commentList;
		}


		// 댓글 삭제 및 그 대댓도 삭제
		@Override
		public int commentDelete(Map<String, String> paraMap) {
			int n = dao.commentDelete(paraMap);
			return n;
		}


		// 댓글 수정
		@Override
		public int commentEdit(Map<String, String> paraMap) {
			int n = dao.commentEdit(paraMap);
			return n;
		}


	


	


	
	



	
	
	
}
