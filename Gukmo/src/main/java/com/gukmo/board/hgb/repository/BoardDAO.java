package com.gukmo.board.hgb.repository;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gukmo.board.model.BoardVO;
import com.gukmo.board.model.CommentVO;


@Repository
public class BoardDAO implements InterBoardDAO{
	
	
	@Resource
	private SqlSessionTemplate gukmo_sql;	



	/**
	 * 하나의 글 불러오기
	 * @param 글번호 board_num,상세카테고리 detaiL_category가 들어있는 Map
	 * @return BoardVO
	 */
	@Override
	public BoardVO getBoardDetail(Map<String,String> paraMap) {
		BoardVO board = gukmo_sql.selectOne("hgb.getBoardDetail",paraMap);
		return board;
	}




	/**
	 * 상세카테고리 알아오기
	 * @param 글번호 board_num
	 * @return 하나의 글번호에 대한상세카테고리
	 */	
	@Override
	public String getCategory(int board_num) {
		String detail_category = gukmo_sql.selectOne("hgb.getCategory",board_num);
		return detail_category;
	}



	// === 글삭제 하기 === //
	@Override
	public int del(Map<String, String> paraMap) {
		int n = gukmo_sql.delete("hgb.del", paraMap);
		return n;
	}



	/**
	 * 좋아요 체크하기
	 * @param paraMap(글번호,userid)
	 * @return 좋아요 갯수
	 */
	@Override
	public int likeCheck(Map<String, String> paraMap) {
		int likeCnt = gukmo_sql.selectOne("hgb.likeCheck", paraMap);
		return likeCnt;
	}
	
	/**
	 * 좋아요 체크하기
	 * @param paraMap(댓글번호,userid)
	 * @return 좋아요 갯수
	 */
	@Override
	public int comment_likeCheck(Map<String, String> paraMap) {
		int likeCnt = gukmo_sql.selectOne("hgb.comment_likeCheck", paraMap);
		return likeCnt;
	}



	/**
	 * 좋아요 테이블에 delete하기
	 * @param paraMap(글번호,userid)
	 * @return 성공 1 실패 0
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int likeDelete(Map<String, String> paraMap) {
		int result1 = gukmo_sql.delete("hgb.likeDelete", paraMap);
		
		//좋아요 개수 알아오기
		int like_cnt = gukmo_sql.selectOne("hgb.getLike_cnt", paraMap);
		like_cnt = like_cnt - 1;
		
		paraMap.put("like_cnt",like_cnt+"");
		
		//좋아요 1 뺀값 업데이트
		int result2 = gukmo_sql.update("hgb.likeCntChange", paraMap);
		
		
		return result1*result2;
	}



	/**
	 * 좋아요 테이블에 insert하기
	 * @param paraMap(글번호,userid)
	 * @return 성공 1 실패 0
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int likeInsert(Map<String, String> paraMap) {
		int result1 = gukmo_sql.insert("hgb.likeInsert", paraMap);
		
		//좋아요 개수 알아오기
		int like_cnt = gukmo_sql.selectOne("hgb.getLike_cnt", paraMap);
		like_cnt = like_cnt + 1;
		
		paraMap.put("like_cnt",like_cnt+"");
		
		//좋아요 1 더한값 업데이트
		int result2 = gukmo_sql.update("hgb.likeCntChange", paraMap);
		
		return result1*result2;
	}
	
	/**
	 * 댓글좋아요 테이블에 delete하기
	 * @param paraMap(댓글번호,userid)
	 * @return 성공 1 실패 0
	 */
	@Override
	public int comment_likeDelete(Map<String, String> paraMap) {
		int result1 = gukmo_sql.delete("hgb.comment_likeDelete", paraMap);
		
		//좋아요 개수 알아오기
		int comment_like_cnt = gukmo_sql.selectOne("hgb.getComment_like_cnt", paraMap);
		comment_like_cnt = comment_like_cnt - 1;
		
		paraMap.put("comment_like_cnt",comment_like_cnt+"");
		
		//좋아요 1 뺀값 업데이트
		int result2 = gukmo_sql.update("hgb.comment_likeCntChange", paraMap);
		
		
		return result1*result2;
	}



	/**
	 * 댓글좋아요 테이블에 insert하기
	 * @param paraMap(댓글번호,userid)
	 * @return 성공 1 실패 0
	 */
	@Override
	public int comment_likeInsert(Map<String, String> paraMap) {
		int result1 = gukmo_sql.insert("hgb.comment_likeInsert", paraMap);
		
		//좋아요 개수 알아오기
		int comment_like_cnt = gukmo_sql.selectOne("hgb.getComment_like_cnt", paraMap);
		comment_like_cnt = comment_like_cnt + 1;
		
		paraMap.put("comment_like_cnt",comment_like_cnt+"");
		
		//좋아요 1 더한값 업데이트
		int result2 = gukmo_sql.update("hgb.comment_likeCntChange", paraMap);
		
		return result1*result2;
	}



	/**
	 * 글 조회수 1증가하기
	 * @param paraMap(글번호)
	 * @return 성공 1 실패 0
	 */
	@Override
	public void setAddReadCount(String board_num) {
		gukmo_sql.update("hgb.setAddReadCount", board_num);
		
	}



	// 글 상세피이지 진입시 로그인한 회원의 좋아요여부 체크하기
	
	@Override	
	public int likethis(Map<String, String> paraMap) {
		
		
		int likethis = gukmo_sql.selectOne("hgb.ilikethis", paraMap);
		
		return likethis;
	}
	
	@Override
	public int comment_likethis(Map<String, String> paraMap) {
		
		int comment_likethis = gukmo_sql.selectOne("hgb.comment_likethis", paraMap);
		
		return comment_likethis;
	}

	// 댓글 테이블에 insert
	@Override
	public int addComment(Map<String, String> paraMap) {
		// System.out.println("dao 파라맵" + paraMap);
		int n = gukmo_sql.insert("hgb.addComment", paraMap);
		return n;
	}

	// tbl_board의 comment_cnt 컬럼에  +1
	@Override
	public int addComment_cnt(Map<String, String> paraMap) {
		int n = gukmo_sql.update("hgb.addComment_cnt", paraMap);
		return n;
	}


	// 해당 회원의 포인트 10점 증가
	@Override
	public int addPoint(Map<String, String> paraMap) {
		int n = gukmo_sql.update("hgb.addPoint", paraMap);
		return n;
	}

	// *대댓글* 댓글 테이블에 insert
	@Override
	public int addComment_of_Comment(Map<String, String> paraMap) {
		int n = gukmo_sql.insert("hgb.addComment_of_Comment", paraMap);
		return n;
	}	

	//기본 댓글 리스트 불러오기(기본 : 그냥 댓글, 특수 : 대댓글)
	@Override
	public List<CommentVO> getBasic_commentList(Map<String, String> paraMap) {
		List<CommentVO> Basic_commentList = gukmo_sql.selectList("hgb.getBasic_commentList", paraMap);
		return Basic_commentList;
	}

	//기본 댓글 리스트 불러오기(기본 : 그냥 댓글, 특수 : 대댓글)
	@Override
	public List<CommentVO> getSpecial_commentList(Map<String, String> paraMap) {
		List<CommentVO> special_commentList = gukmo_sql.selectList("hgb.getSpecial_commentList", paraMap);
		return special_commentList;
	}

	// 댓글 삭제 및 그 대댓도 삭제
	@Override
	public int commentDelete(Map<String, String> paraMap) {
		int n = gukmo_sql.delete("hgb.commentDelete", paraMap);
		return n;
	}

	// 댓글 수정
	@Override
	public int commentEdit(Map<String, String> paraMap) {
		int n = gukmo_sql.update("hgb.commentEdit", paraMap);
		return n;
	}






	




	



	




	
}
