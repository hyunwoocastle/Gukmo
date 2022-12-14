package com.gukmo.board.hw.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gukmo.board.hw.repository.InterBoardDAO;
import com.gukmo.board.model.BoardVO;
import com.gukmo.board.model.HashtagVO;

@Service
public class BoardService implements InterBoardService{
	
	@Autowired
	private InterBoardDAO dao;

	
	/**
	 * 총 게시물 건수(totalCount) 구하기(공지사항 게시판)
	 * @param paraMap(검색어)
	 * @return (총 게시물 건수) 를 반환한다. totalCount
	 */
	@Override
	public int getTotalNoticesCount(Map<String, String> paraMap) {
		int totalCount = dao.getTotalNoticesCount(paraMap);
		return totalCount;
	}


	/**
	 * 공지사항 게시판을 보여주기 위한 BoardVO 리스트 가져오기
	 * @param paraMap(검색어,시작rownum,끝rownum)
	 * @return BoardVO리스트
	 */
	@Override
	public List<BoardVO> getNotices(Map<String, String> paraMap) {
		List<BoardVO> notices = dao.getNotices(paraMap);
		return notices;
	}

	
	
	/**
	 * 총 게시물 건수(totalCount) 구하기(국비학원 게시판)
	 * @param paraMap(검색어)
	 * @return (총 게시물 건수) 를 반환한다. totalCount
	 */
	@Override
	public int getTotalAcademyCount(Map<String, String> paraMap) {
		int totalCount = dao.getTotalAcademyCount(paraMap);
		return totalCount;
	}

	
	
	/**
	 * 국비학원 게시판을 보여주기 위한 BoardVO 리스트 가져오기
	 * @param paraMap(검색어,시작rownum,끝rownum,정렬)
	 * @return BoardVO리스트
	 */
	@Override
	public List<BoardVO> getAcademyList(Map<String, String> paraMap) {
		List<BoardVO> academyList = dao.getAcademyList(paraMap);
		return academyList;
	}


	
	
	/**
	 * 하나의 글 불러오기(국비학원게시판)
	 * @param 글번호 boardNum
	 * @return BoardVO
	 */
	@Override
	public BoardVO getBoardDetail(int boardNum) {
		
		//상세카테고리 알아오기
		String detail_category = dao.getDetailCategory(boardNum);				
		
		Map<String,String> paraMap = new HashMap<>();
		
		paraMap.put("board_num", boardNum+"");
		paraMap.put("category","국비학원");
		paraMap.put("detail_category",detail_category);
		
		BoardVO board = dao.getAcademyDetail(paraMap);
		return board;
	}


	
	/**
	 * 총 게시물 건수(totalCount) 구하기(교육과정 게시판)
	 * @param paraMap(검색어)
	 * @return (총 게시물 건수) 를 반환한다. totalCount
	 */
	@Override
	public int getTotalCurriculumCount(Map<String, String> paraMap) {
		int totalCount = dao.getTotalCurriculumCount(paraMap);
		return totalCount;
	}


	
	/**
	 * 교육과정 게시판을 보여주기 위한 BoardVO 리스트 가져오기
	 * @param paraMap(검색어,시작rownum,끝rownum,정렬)
	 * @return BoardVO리스트
	 */
	@Override
	public List<BoardVO> getCurriculumList(Map<String, String> paraMap) {
		List<BoardVO> curriculumList = dao.getCurriculumList(paraMap);
		return curriculumList;
	}


	/**
	 * 학원 글 등록하기
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public boolean insertAcademy(Map<String, Object> paraMap) {
		String board_num = dao.getBoarSeq();
		paraMap.put("board_num",board_num);
		
		int result1 = dao.insertBoardByAcademy(paraMap);	//tbl_board에 insert 해주기	
		int result2 = dao.insertAcademy(paraMap);			//tbl_academy에 insert해주기
		int result3 = 1;
		
		List<String> hashTags = (List<String>) paraMap.get("hashTags");
        for (String hashTag : hashTags) {
        	
            HashtagVO hashtagvo = dao.findHashtag(hashTag);

            // 등록된 태그가 아니라면 태그부터 추가
            if (hashtagvo == null) {
            	dao.saveHashTag(hashTag);
            }
            // 태그-보드 매핑 테이블에 데이터 추가
            hashtagvo = dao.findHashtag(hashTag);
            int hashtag_num = hashtagvo.getHashtag_num();
           
            dao.upHashTagCount(hashtag_num); // 언급 카운트 올리기
            
            paraMap.put("hashtag_num", hashtag_num);
            
            result3 = result3 * dao.hashtagBoardMapping(paraMap);
        }
        
        int result4 = dao.pointPlusActivityRecord(paraMap);

		boolean success = result1*result2*result3*result4 > 1?true:false;
		return success;
	}


	/**
	 * 교육과정 글 등록하기
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public boolean insertCurriculum(Map<String, Object> paraMap) {
		String board_num = dao.getBoarSeq();
		paraMap.put("board_num",board_num);
		
		int result1 = dao.insertBoardByCurriculum(paraMap);	//tbl_board에 insert 해주기	
		int result2 = dao.insertCurriculum(paraMap);		//tbl_curriculum에 insert해주기
		int result3 = 1;
		
		//해시태그 넣기
		List<String> hashTags = (List<String>) paraMap.get("hashTags");
        for (String hashTag : hashTags) {
        	
            HashtagVO hashtagvo = dao.findHashtag(hashTag);

            // 등록된 태그가 아니라면 태그부터 추가
            if (hashtagvo == null) {
            	dao.saveHashTag(hashTag);
            }
            
            // 태그-보드 매핑 테이블에 데이터 추가
            hashtagvo = dao.findHashtag(hashTag);
            int hashtag_num = hashtagvo.getHashtag_num();
           
            dao.upHashTagCount(hashtag_num); // 언급 카운트 올리기
            
            paraMap.put("hashtag_num", hashtag_num);
            
            result3 = result3 * dao.hashtagBoardMapping(paraMap);
        }
        
        int result4 = dao.pointPlusActivityRecord(paraMap);

		boolean success = result1*result2*result3*result4 == 1?true:false;
		return success;
	}


	/**
	 * 국비학원 수정하기
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public boolean editAcademy(Map<String, Object> paraMap) {
		int result1 = dao.updateBoard(paraMap);	//아카데미글 수정
		int result2 = 1;
		int result3 = 0;
		String board_num = (String) paraMap.get("board_num");
		String orgin_hashTag = (String) paraMap.get("orgin_hashTag");
		if(orgin_hashTag != null && !orgin_hashTag.trim().isEmpty()) {
			result1 = dao.hashTagDel(board_num); // 기존 해시태그 맵핑테이블에 있는 데이터 삭제
		}
		String str_hashTag = (String) paraMap.get("str_hashTag");
		
		if(str_hashTag != null && !str_hashTag.trim().isEmpty()) {
			// System.out.println("str_해시태그" + str_hashTag);
			List<String> hashTags = Arrays.asList(str_hashTag.split(","));
			// 해시태그 처리 시작
			for (String hashTag : hashTags) {
	        	
	            HashtagVO hashtagvo = dao.findHashtag(hashTag);

	            // 등록된 태그가 아니라면 태그부터 추가
	            if (hashtagvo == null) {
	            	dao.saveHashTag(hashTag);
	            }
	            
	            // 태그-보드 매핑 테이블에 데이터 추가
	            hashtagvo = dao.findHashtag(hashTag);
	            int hashtag_num = hashtagvo.getHashtag_num();
	           
	            result3 = dao.upHashTagCount(hashtag_num); // 언급 카운트 올리기
	            
	            paraMap.put("hashtag_num", hashtag_num);
	            paraMap.put("board_num", board_num);
	            
	            result2 = dao.hashtagBoardMapping(paraMap);
	        };
		}
        
        int result4 = dao.pointPlusActivityRecord(paraMap);

		boolean success = result1 * result2 * result3 * result4 == 1?true:false;
		return success;
	}


	
	
	
	
	
	
	/**
	 * 교육과정 수정하기
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public boolean editCurriculum(Map<String, Object> paraMap) {
		int result1 = 0;
		int result2 = 0;
		int result3 = 0;
		int result4 = dao.updateCurriculum(paraMap);
		String board_num = (String) paraMap.get("board_num");
		String orgin_hashTag = (String) paraMap.get("orgin_hashTag");
		if(orgin_hashTag != null && !orgin_hashTag.trim().isEmpty()) {
			result1 = dao.hashTagDel(board_num); // 기존 해시태그 맵핑테이블에 있는 데이터 삭제
		}
		String str_hashTag = (String) paraMap.get("str_hashTag");
		
		if(str_hashTag != null && !str_hashTag.trim().isEmpty()) {
			// System.out.println("str_해시태그" + str_hashTag);
			List<String> hashTags = Arrays.asList(str_hashTag.split(","));
			// 해시태그 처리 시작
			for (String hashTag : hashTags) {
	        	
	            HashtagVO hashtagvo = dao.findHashtag(hashTag);

	            // 등록된 태그가 아니라면 태그부터 추가
	            if (hashtagvo == null) {
	            	dao.saveHashTag(hashTag);
	            }
	            
	            // 태그-보드 매핑 테이블에 데이터 추가
	            hashtagvo = dao.findHashtag(hashTag);
	            int hashtag_num = hashtagvo.getHashtag_num();
	           
	            result3 = dao.upHashTagCount(hashtag_num); // 언급 카운트 올리기
	            
	            paraMap.put("hashtag_num", hashtag_num);
	            paraMap.put("board_num", board_num);
	            
	            result2 = dao.hashtagBoardMapping(paraMap);
	        };
		}
		return result1*result2*result3*result4>0?true:false;
	}







}
