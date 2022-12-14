package com.gukmo.board.sun.controller;


import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gukmo.board.common.FileManager;
import com.gukmo.board.common.MyUtil;
import com.gukmo.board.model.BoardVO;
import com.gukmo.board.model.MemberVO;
import com.gukmo.board.model.ReportVO;
import com.gukmo.board.sun.service.InterBoardService;


@Controller
public class BoardController {
	
	@Autowired
	private InterBoardService service;
	
	@Autowired 
	private FileManager fileManager;
	
	
	// 자유게시판 글목록 보기 페이지 요청
	@RequestMapping(value="/community/freeBoards.do", method= {RequestMethod.GET})
	public String getNotice_viewFreeBoards(HttpServletRequest request) {
		List<BoardVO> boardList = null;
		
		Map<String, String> paraMap = new HashMap<>();
		 String searchWord = request.getParameter("searchWord");
		 String sort = request.getParameter("sort");
		 String str_page = request.getParameter("page");
		 String detail_category = "자유게시판";
		 
		 if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		 	searchWord = "";
		 }
		 
		 if(sort == null || sort.trim() == "") {   //sort 값이 없다면
	         sort = "write_date";
	      } else {                        //sort 값이 있다면 아래 sort 값 구하기 메서드 호출
	         sort = getSort(sort);
	      }
		 
		 paraMap.put("searchWord", searchWord);
		 paraMap.put("sort", sort);
		 paraMap.put("detail_category", detail_category);
		 
		 // 총 게시물 건수
		 int totalCount = service.getTotalCount(paraMap);    
		 // 한 페이지당 보여줄 게시물 건수 
		 int sizePerPage = 10;         
		 // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		 int totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );            
		 //현재페이지번호 구하기(예외처리 포함)
		 int page = getPage(str_page,totalPage);	
	
		 // 시작행번호,끝 행번호 구하기(맵에 담아서 반환)
		 paraMap = getRno(page,sizePerPage,paraMap); 
		 
		 boardList = service.boardList(paraMap); // 글목록 가져오기
		 
		 // 검색대상 컬럼과 검색어를 뷰단 페이지에서 유지시키기 위한 것
		 if(!"".equals(searchWord) ) {
			 request.setAttribute("paraMap", paraMap);
		 }
		
		//정렬기준 넣기
	      switch (sort) {
	         case "write_date":
	            sort = "최신순";
	            break;
	         case "comment_cnt":
	            sort = "댓글순";   
	            break;
	         case "like_cnt":
	            sort = "추천순";
	            break;
	         case "views":
	            sort = "조회순";
	            break;
	         default :
	            sort = "최신순";
	            break;
	      }
	      
	    String url = "freeBoards.do";
		//페이지바 얻기
		String pageBar = getPageBar(page,totalPage, url, searchWord, sort);
		 
		request.setAttribute("pageBar", pageBar);
		request.setAttribute("page", page);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("boardList",boardList);
		request.setAttribute("searchWord",searchWord);
		request.setAttribute("sort",sort);
		request.setAttribute("detail_category",detail_category);
		
		return "board/community/communityList.tiles1";
	}
	
	
	
	
	@RequestMapping(value="/community/questions.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임. 
	public String getNotice_viewQuestions(HttpServletRequest request) {
		List<BoardVO> boardList = null;
		
		Map<String, String> paraMap = new HashMap<>();
		 String searchWord = request.getParameter("searchWord");
		 String sort = request.getParameter("sort");
		 String str_page = request.getParameter("page");
		 String detail_category = "QnA";
		 
		 if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		 	searchWord = "";
		 }
		 
		 if(sort == null || sort.trim() == "") {   //sort 값이 없다면
	         sort = "write_date";
	      } else {                        //sort 값이 있다면 아래 sort 값 구하기 메서드 호출
	         sort = getSort(sort);
	      }
		 
		 paraMap.put("searchWord", searchWord);
		 paraMap.put("sort", sort);
		 paraMap.put("detail_category", detail_category);
		 
		 // 총 게시물 건수
		 int totalCount = service.getTotalCount(paraMap);    
		 // 한 페이지당 보여줄 게시물 건수 
		 int sizePerPage = 10;         
		 // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		 int totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );            
		 //현재페이지번호 구하기(예외처리 포함)
		 int page = getPage(str_page,totalPage);	
	
		 // 시작행번호,끝 행번호 구하기(맵에 담아서 반환)
		 paraMap = getRno(page,sizePerPage,paraMap); 
		 
		 boardList = service.boardList(paraMap); // 글목록 가져오기
		 
		 // 검색대상 컬럼과 검색어를 뷰단 페이지에서 유지시키기 위한 것
		 if(!"".equals(searchWord) ) {
			 request.setAttribute("paraMap", paraMap);
		 }
		 
		  //정렬기준 넣기
	      switch (sort) {
	         case "write_date":
	            sort = "최신순";
	            break;
	         case "comment_cnt":
	            sort = "댓글순";   
	            break;
	         case "like_cnt":
	            sort = "추천순";
	            break;
	         case "views":
	            sort = "조회순";
	            break;
	         default :
	            sort = "최신순";
	            break;
	      }
	      
	    String url = "questions.do";
		//페이지바 얻기
		String pageBar = getPageBar(page,totalPage, url, searchWord, sort);
		
		request.setAttribute("pageBar", pageBar);
		request.setAttribute("page", page);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("boardList",boardList);
		request.setAttribute("searchWord",searchWord);
		request.setAttribute("sort",sort);
		request.setAttribute("detail_category",detail_category);
		
		return "board/community/communityList.tiles1";
   }
	
	
	
	@RequestMapping(value="/community/studies.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임. 
	public String getNotice_viewStudies(HttpServletRequest request) {
		List<BoardVO> boardList = null;
		
		Map<String, String> paraMap = new HashMap<>();
		String searchWord = request.getParameter("searchWord");
		String sort = request.getParameter("sort");
		String str_page = request.getParameter("page");
		String detail_category = "스터디";
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
			searchWord = "";
		}
		
		 if(sort == null || sort.trim() == "") {   //sort 값이 없다면
	         sort = "write_date";
	      } else {                        //sort 값이 있다면 아래 sort 값 구하기 메서드 호출
	         sort = getSort(sort);
	      }
		
		paraMap.put("searchWord", searchWord);
		paraMap.put("sort", sort);
		paraMap.put("detail_category", detail_category);
		
		// 총 게시물 건수
		int totalCount = service.getTotalCount(paraMap);    
		// 한 페이지당 보여줄 게시물 건수 
		int sizePerPage = 10;         
		// 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		int totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );            
		//현재페이지번호 구하기(예외처리 포함)
		int page = getPage(str_page,totalPage);	
		
		// 시작행번호,끝 행번호 구하기(맵에 담아서 반환)
		paraMap = getRno(page,sizePerPage,paraMap); 
		
		boardList = service.boardList(paraMap); // 글목록 가져오기
		
		// 검색대상 컬럼과 검색어를 뷰단 페이지에서 유지시키기 위한 것
		if(!"".equals(searchWord) ) {
			request.setAttribute("paraMap", paraMap);
		}
		
		
		//정렬기준 넣기
	      switch (sort) {
	         case "write_date":
	            sort = "최신순";
	            break;
	         case "comment_cnt":
	            sort = "댓글순";   
	            break;
	         case "like_cnt":
	            sort = "추천순";
	            break;
	         case "views":
	            sort = "조회순";
	            break;
	         default :
	            sort = "최신순";
	            break;
	      }
	      
	    String url = "studies.do";
		//페이지바 얻기
		String pageBar = getPageBar(page,totalPage, url, searchWord, sort);
	            
		request.setAttribute("pageBar", pageBar);
		request.setAttribute("page", page);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("boardList",boardList);
		request.setAttribute("searchWord",searchWord);
		request.setAttribute("sort",sort);
		request.setAttribute("detail_category",detail_category);
		
		return "board/community/communityList.tiles1";
	}
	
	
	@RequestMapping(value="/community/hobbies.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임. 
	public String getNotice_viewHobbies(HttpServletRequest request) {
		List<BoardVO> boardList = null;
		
		Map<String, String> paraMap = new HashMap<>();
		 String searchWord = request.getParameter("searchWord");
		 String sort = request.getParameter("sort");
		 String str_page = request.getParameter("page");
		 String detail_category = "취미모임";
		 
		 if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		 	searchWord = "";
		 }
		 
		 if(sort == null || sort.trim() == "") {   //sort 값이 없다면
	         sort = "write_date";
	      } else {                        //sort 값이 있다면 아래 sort 값 구하기 메서드 호출
	         sort = getSort(sort);
	      }
		 
		 paraMap.put("searchWord", searchWord);
		 paraMap.put("sort", sort);
		 paraMap.put("detail_category", detail_category);
		 
		 // 총 게시물 건수
		 int totalCount = service.getTotalCount(paraMap);    
		 // 한 페이지당 보여줄 게시물 건수 
		 int sizePerPage = 10;         
		 // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		 int totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );            
		 //현재페이지번호 구하기(예외처리 포함)
		 int page = getPage(str_page,totalPage);	
	
		 // 시작행번호,끝 행번호 구하기(맵에 담아서 반환)
		 paraMap = getRno(page,sizePerPage,paraMap); 
		 
		 boardList = service.boardList(paraMap); // 글목록 가져오기
		 
		 // 검색대상 컬럼과 검색어를 뷰단 페이지에서 유지시키기 위한 것
		 if(!"".equals(searchWord) ) {
			 request.setAttribute("paraMap", paraMap);
		 }
		 
		//정렬기준 넣기
      switch (sort) {
         case "write_date":
            sort = "최신순";
            break;
         case "comment_cnt":
            sort = "댓글순";   
            break;
         case "like_cnt":
            sort = "추천순";
            break;
         case "views":
            sort = "조회순";
            break;
         default :
            sort = "최신순";
            break;
      }
      
		String url = "hobbies.do";
		//페이지바 얻기
		String pageBar = getPageBar(page,totalPage, url, searchWord, sort);

		request.setAttribute("pageBar", pageBar);
		request.setAttribute("page", page);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("boardList",boardList);
		request.setAttribute("searchWord",searchWord);
		request.setAttribute("sort",sort);
		request.setAttribute("detail_category",detail_category);
		
		return "board/community/communityList.tiles1";
   }
	
	
	
	
	@RequestMapping(value="/community/reviews.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임. 
	public String getNotice_viewReviews(HttpServletRequest request) {
		List<BoardVO> boardList = null;
		
		Map<String, String> paraMap = new HashMap<>();
		 String searchWord = request.getParameter("searchWord");
		 String sort = request.getParameter("sort");
		 String str_page = request.getParameter("page");
		 String detail_category = "수강/취업후기";
		 
		 if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		 	searchWord = "";
		 }
		 
		 if(sort == null || sort.trim() == "") {   //sort 값이 없다면
	         sort = "write_date";
	      } else {                        //sort 값이 있다면 아래 sort 값 구하기 메서드 호출
	         sort = getSort(sort);
	      }
		 
		 paraMap.put("searchWord", searchWord);
		 paraMap.put("sort", sort);
		 paraMap.put("detail_category", detail_category);
		 
		 // 총 게시물 건수
		 int totalCount = service.getTotalCount(paraMap);    
		 // 한 페이지당 보여줄 게시물 건수 
		 int sizePerPage = 10;         
		 // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		 int totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );            
		 //현재페이지번호 구하기(예외처리 포함)
		 int page = getPage(str_page,totalPage);	
	
		 // 시작행번호,끝 행번호 구하기(맵에 담아서 반환)
		 paraMap = getRno(page,sizePerPage,paraMap); 
		 
		 boardList = service.boardList(paraMap); // 글목록 가져오기
		 
		 // 검색대상 컬럼과 검색어를 뷰단 페이지에서 유지시키기 위한 것
		 if(!"".equals(searchWord) ) {
			 request.setAttribute("paraMap", paraMap);
		 }

		 
		//정렬기준 넣기
	      switch (sort) {
	         case "write_date":
	            sort = "최신순";
	            break;
	         case "comment_cnt":
	            sort = "댓글순";   
	            break;
	         case "like_cnt":
	            sort = "추천순";
	            break;
	         case "views":
	            sort = "조회순";
	            break;
	         default :
	            sort = "최신순";
	            break;
	      }
	      
	    String url = "reviews.do";
		//페이지바 얻기
		String pageBar = getPageBar(page,totalPage, url, searchWord, sort);

		request.setAttribute("pageBar", pageBar);
		request.setAttribute("page", page);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("boardList",boardList);
		request.setAttribute("searchWord",searchWord);
		request.setAttribute("sort",sort);
		request.setAttribute("detail_category",detail_category);
		
		return "board/community/communityList.tiles1";
   }
	
	
	/////////////////////////////////////// 커뮤니티 리스트 출력 //////////////////////////////////////
	
	
	
	
	// 게시판 글쓰기 페이지 요청
	@RequestMapping(value="/community/new.do" )
	 public String requiredLogin_communityNew(HttpServletRequest request, HttpServletResponse response){ // <== before Advice(로그인체크)
		
		// 카테고리 값 지정용
		// String detail_category = request.getParameter("detail_category");
		
		// request.setAttribute("detail_category", detail_category);
		return "board/community/communityNew.tiles1";
	}

	
   // 스마트에디터. 드래그앤드롭을 사용한 다중사진 파일업로드
   @RequestMapping(value="/image/multiplePhotoUpload.do", method= {RequestMethod.POST} )
	public void multiplePhotoUpload(HttpServletRequest request, HttpServletResponse response) {
		
//		String path = "C:\\Users\\sist\\git\\Gukmo\\Gukmo\\src\\main\\webapp\\resources"+File.separator+"photo_upload"; // 사진 첨부시 업로드 되는 경로
	    String path = "C:/Users/sist/git/Gukmo/Gukmo/src/main/webapp/resources/photo_upload";
		
		// System.out.println("~~~~ 확인용 path => " + path);
		
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			String filename = request.getHeader("file-name"); // 원본파일명
			
			// System.out.println(">>> 확인용 filename ==> " + filename);
			
			InputStream is = request.getInputStream();
			
			String newFilename = fileManager.doFileUpload(is, filename, path);
			
			int width = fileManager.getImageWidth(path+File.separator+newFilename);
			
		    if(width > 600) {
		       width = 600;
		    }
		    
			String ctxPath = request.getContextPath();
			String strURL = "";
			strURL += "&bNewLine=true&sFileName="+newFilename; 
			strURL += "&sWidth="+width;
			strURL += "&sFileURL="+ctxPath+"/resources/photo_upload/"+newFilename;
			
			PrintWriter out = response.getWriter();
			out.print(strURL);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}


   // 게시판 글쓰기 등록
	@RequestMapping(value="/community/newEnd.do", method= {RequestMethod.POST})
	public String communityNewEnd(Map<String, Object> paraMap, BoardVO boardvo, HttpServletRequest request, ModelAndView mav) {  // <== After Advice(활동점수 올리기)
		
		boardvo.setContent(MyUtil.secureCode(boardvo.getContent()));
		int n = service.communityNew(boardvo);
		
		String message ="";
		String loc = "";
		if(n==1) {
			
			String board_num = boardvo.getBoard_num();

			// 해시태그 리스트 만들기
			String str_hashTag = request.getParameter("str_hashTag");
			
			if(str_hashTag != null && !str_hashTag.trim().isEmpty()) {
				
				List<String> hashTags = Arrays.asList(str_hashTag.split(","));
				// 해시태그 처리 시작
				boolean success = service.saveTag(hashTags, board_num);
				if(success) {
					System.out.println("해시태그 처리 함수 구동 성공");
				}
				else {
					System.out.println("해시태그 처리 함수 구동 실패");
				}
			}
			
			// pointPlusActivityLog After Advice(포인트 업데이트, 활동내역 등록하기) 
			HttpSession session = request.getSession();
			MemberVO user = (MemberVO)session.getAttribute("user");
			String userid = user.getUserid();
			
			paraMap.put("fk_userid", userid);// 포인트, 활동내역용
		    paraMap.put("board_num", board_num);// 활동내역용(지금 등록된 글번호)
			paraMap.put("boardvo", boardvo); // 활동내역용
			paraMap.put("division", "게시글작성");// 활동내역용
			paraMap.put("point", 10); // 포인트용		
			
			service.pointPlusActivityLog(paraMap);
			String detail_category = boardvo.getDetail_category();
			
			switch (detail_category) {
			case "자유게시판":
				loc = request.getContextPath()+"/community/freeBoards.do";
				break;
			case "QnA":
				loc = request.getContextPath()+"/community/questions.do";
				break;
							
			case "스터디":
				loc = request.getContextPath()+"/community/studies.do";
				break;
				
			case "취미모임":
				loc = request.getContextPath()+"/community/hobbies.do";
				break;
				
			case "수강/취업후기":
				loc = request.getContextPath()+"/community/reviews.do";
				break;
			}
			message = "글작성 성공!";
			
		}
		else { // 글쓰기 실패시
			message = "글작성 실패!";
		}
		request.setAttribute("message", message);
		request.setAttribute("loc", loc);
		
		return "msg";
	}
	
	
	
	// 게시글 수정페이지 요청
	@RequestMapping(value="/community/modify.do", method= {RequestMethod.GET})
	public String requiredLogin_communityModify(HttpServletRequest request, HttpServletResponse response) { 
			
		// 글 수정해야 할 글번호 가져오기
		String board_num = request.getParameter("boardNum");
		
		// 글 내용 가져오기
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("board_num", board_num);
		BoardVO boardvo = service.getBoardDetail(paraMap);
		
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		if( !user.getNickname().equals(boardvo.getNickname()) ) {
			request.setAttribute("message","다른 사용자의 글은 수정이 불가합니다.");
			request.setAttribute("loc","javascript:history.back()");
			
			return "msg";
		}
		else {
			// 자신의 글을 수정할 경우
			request.setAttribute("boardvo",boardvo);
			return "board/community/communityNew.tiles1";
		}
	}
	
	
	// 게시글 수정 완료 요청
	@RequestMapping(value="/community/modify.do", method= {RequestMethod.POST})
	public String communityModifyEnd(HttpServletRequest request, BoardVO boardvo) { 
			
		int n = service.modify(boardvo);
		
		if(n==1) {
			
			String board_num = boardvo.getBoard_num();
			
			String orgin_hashTag = request.getParameter("orgin_hashTag");
			
			if(orgin_hashTag != null && !orgin_hashTag.trim().isEmpty()) {
				n = service.hashTagDel(board_num); // 기존 해시태그 맵핑테이블에 있는 데이터 삭제
			}
			
			String str_hashTag = request.getParameter("str_hashTag");
			
			if(str_hashTag != null && !str_hashTag.trim().isEmpty()) {
				// System.out.println("str_해시태그" + str_hashTag);
				List<String> hashTags = Arrays.asList(str_hashTag.split(","));
				// 해시태그 처리 시작
				boolean success = service.saveTag(hashTags, board_num);
				if(success) {
					System.out.println("해시태그 처리 함수 구동 성공");
				}
				else {
					System.out.println("해시태그 처리 함수 구동 실패");
				}
			}
		}
		
		if(n==0) {
			request.setAttribute("message", "다시 시도해 주세요.");
			request.setAttribute("loc", "javascript:history.back()");
		}
		else {
			request.setAttribute("message", "글 수정 성공!!");
			request.setAttribute("loc", request.getContextPath()+"/detail.do?boardNum="+boardvo.getBoard_num());
		}
		
		return "msg";
	}
	





	// 글삭제 요청
	@RequestMapping(value="/community/del.do")
	public String requiredLogin_del(HttpServletRequest request, HttpServletResponse response) {
		
		// 삭제해야 할 글번호 가져오기
		String board_num = request.getParameter("boardNum");
		
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("board_num", board_num);

		BoardVO boardvo = service.getBoardDetail(paraMap);
		
		HttpSession session = request.getSession();
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		if( user.getNickname().equals(boardvo.getNickname()) || "관리자".equals(user.getAuthority())) {
			int n = service.boardDel(paraMap);
			
			if(n==0) {
				request.setAttribute("message", "글 삭제 실패!!");
				request.setAttribute("loc", "javascript:history.back()");
			}
			else {
				request.setAttribute("message", "글 삭제 성공!!");
				request.setAttribute("loc", request.getContextPath()+"/community/freeBoards.do");
			}
			
		}
		else {// 본인 글을 삭제하는 경우
			request.setAttribute("message", "다른 사용자의 글은 삭제가 불가합니다.");
			request.setAttribute("loc", "javascript:history.back()");
			
		}
		return "msg";
		
	}
	
	
	
	
	// 신고 페이지 요청
	@RequestMapping(value="/community/report.do", method= {RequestMethod.GET} )
	public String requiredLogin_report(HttpServletRequest request, HttpServletResponse response){
		
		String board_num = request.getParameter("boardNum");
		
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("board_num", board_num);
		
		BoardVO boardvo = service.getBoardDetail(paraMap);
		
		request.setAttribute("boardvo", boardvo);
		
		return "/report";
	}
	
	 
	
	// 신고하기
	@RequestMapping(value="/community/reportEnd.do", method= {RequestMethod.POST} )
	public String setAlarm_reportEnd(HttpServletRequest request, HttpServletResponse response, ReportVO reportvo, @RequestParam Map<String,String> paraMap){
		// ㅎㅎ... 로그인 안하면 신고버튼 안보이길래 냅다 setAlarm 으로 aop 박아용 ㅎㅎ!
		int n = service.reportInsert(reportvo);
		
		
		// System.out.println("여기아냐?");
	      //AOP 용
	      Map<String,String> alarmMap = new HashMap<>();
	      alarmMap.put("alarm_nickname", paraMap.get("reported_nickname"));
    	  alarmMap.put("cmd", "penalty");
    	  alarmMap.put("url", "/detail.do?boardNum=");
    	  alarmMap.put("content", paraMap.get("subject"));
    	  alarmMap.put("url_num", paraMap.get("fk_num"));
    	  
    	  // System.out.println("alarm:" + alarmMap);

		 request.setAttribute("alarmMap", alarmMap);
		 	
		if(n==0) {
			request.setAttribute("message", "시스템 오류로 실패했습니다. 다시 시도해주세요.");
			request.setAttribute("loc", "javascript:history.back()");
		}
		else {
			request.setAttribute("message", "신고완료");
			request.setAttribute("loc", "javascript:window.close()");
		}
		return "msg";
	}
	
	
	   
    
	   // 댓글 신고 페이지 요청
	   @RequestMapping(value="/community/report_comment.do", method= {RequestMethod.GET} )
	   public String requiredLogin_report_comment(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> paraMap){
	      String board_num = (String) paraMap.get("boardNum");
	      paraMap.put("board_num", board_num);
	      
	      request.setAttribute("paraMap", paraMap);      
	      return "/report_comment";
	   }
	   
	   // 댓글 신고하기
	   @RequestMapping(value="/community/comment_reportEnd.do", method= {RequestMethod.POST} )
	   public String setAlarm_comment_reportEnd(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> paraMap){
		   
	      int n = service.comment_reportInsert(paraMap);
	      
	      //System.out.println("url_num:" + paraMap.get("board_num"));
	      //AOP 용
	      Map<String,String> alarmMap = new HashMap<>();
	      alarmMap.put("alarm_nickname", paraMap.get("reported_nickname"));
      	  alarmMap.put("cmd", "cmtPenalty");
      	  alarmMap.put("url", "/detail.do?boardNum=");
      	  alarmMap.put("content", paraMap.get("subject"));
      	  alarmMap.put("url_num", paraMap.get("board_num"));

		 request.setAttribute("alarmMap", alarmMap);
	      
	      
	      if(n==0) {
	         request.setAttribute("message", "시스템 오류로 실패했습니다. 다시 시도해주세요.");
	         request.setAttribute("loc", "javascript:history.back()");
	      }
	      else {
	         request.setAttribute("message", "신고완료");
	         request.setAttribute("loc", "javascript:window.close()");
	      }
	      return "msg";
	   }
	
	
	
   // ============================================================================== //
   // ==================================== 페이징처리 유틸 =============================== //
   // ============================================================================== //
	
   /**
    * 페이지바 만들기 메소드
    * @param page(현재 페이지번호)
    * @param totalPage(총페이지 수)
    * @param searchWord(검색어)
    * @param sort(정렬조건)
    * @return pageBar
    */
   private String getPageBar(int page, int totalPage,String url, String searchWord, String sort) {
		// 페이지바 만들기 
		int blockSize = 5;
		// blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수이다.
		
		int loop = 1;
		
		int pageNo = ((page - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul class='my pagination pagination-md justify-content-center mt-5'>";
		
		// === [<<][<] 만들기 === //
		if(pageNo != 1) {
			//[<<]
			pageBar += "<li class='page-item'>" + 
					   "  <a class='page-link' href='"+url+"?searchWord="+searchWord+"&sort="+sort+"&page=1'>" + 
					   "    <i class='fa-solid fa-angles-left'></i>" + 
					   "  </a>" + 
					   "</li>";
			//[<]
			pageBar += "<li class='page-item'>" + 
					   "  <a class='page-link' href='"+url+"?searchWord="+searchWord+"&sort="+sort+"&page="+(pageNo-1)+"'>" + 
					   "    <i class='fa-solid fa-angle-left'></i>" + 
					   "  </a>" + 
					   "</li>"; 
		}
		
		while( !(loop > blockSize || pageNo > totalPage) ) {
			
			if(pageNo == page) {	//페이지번호가 현재페이지번호와 같다면 .active
				pageBar += "<li class='page-item active' aria-current='page'>" + 
						   "  <a class='page-link' href='#'>"+pageNo+"</a>" + 
						   "</li>";  
			}
			
			else {	//페이지번호가 현재페이지번호랑 다르다면 .active 뺌
				pageBar += "<li class='page-item'>" + 
						   "  <a class='page-link' href='"+url+"?searchWord="+searchWord+"&sort="+sort+"&page="+pageNo+"'>"+pageNo+"</a>" + 
						   "</li>";        
			}
			
			loop++;
			pageNo++;
		}// end of while--------------------------
		
		// === [>][>>] 만들기 === //
		if( pageNo <= totalPage) {
			//[>]
			pageBar += "<li class='page-item'>" + 
					   "  <a class='page-link' href='"+url+"?searchWord="+searchWord+"&sort="+sort+"&page="+pageNo+"'>"+
					   "    <i class='fa-solid fa-angle-right'></i>"+
					   "  </a>" + 
					   "</li>";
			
			//[>>] 
			pageBar += "<li class='page-item'>" + 
					   "  <a class='page-link' href='"+url+"?searchWord="+searchWord+"&sort="+sort+"&page="+totalPage+"'>"+
					   "    <i class='fas fa-solid fa-angles-right'></i>"+
					   "  </a>" + 
					   "</li>";
		}
		
		pageBar += "</ul>";
		
		return pageBar;
    }//end of getPageBar(){}---
   
   
   
   
   /**
    * 페이지 번호 예외처리하기
    * @param str_page(쿼리스트링으로 날아온 페이지)
    * @param totalPage
    * @return page(현재 페이지번호)
    */
   private int getPage(String str_page,int totalPage) {
	   int page = 0;
	   if(str_page == null) {	//쿼리스트링에 페이지가 없다면
			 // 게시판에 보여지는 초기화면 
			 page = 1;
	   } else {
			 try {
				 page = Integer.parseInt(str_page);
				 if( page < 1) {	//페이지가 1페이지보다 작은경우
					 page = 1;
				 }
				 else if(page > totalPage) { //페이지가 총페이지보다 큰 경우
					 page = totalPage;
				 }
			 } catch(NumberFormatException e) {	//페이지번호에 글자를 써서 들어올 경우 오류방지
				 page = 1;
			 }//end of try-catch
	   }
	   return page;
   }//end of method---
   
   
   /**
    * 시작행번호,끝행번호 구하기
    * @param 페이지번호,한페이지당보여줄 갯수,쓰던 맵
    * @return 맵(시작행번호,끝행번호 담아서 줌)
    */
   private Map<String, String> getRno(int page, int sizePerPage, Map<String, String> paraMap) {
		int startRno = ((page - 1) * sizePerPage) + 1; // 시작 행번호(쿼리문 rownum where절에 쓰임)
		int endRno = startRno + sizePerPage - 1; // 끝 행번호(쿼리문 rownum where절에 쓰임)
		
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		return paraMap;
	}
   
   
   
   
   // ============================================================================== //
   // ==================================== 페이징처리 유틸 =============================== //
   // ============================================================================== //
	
	

   /**
    * 리퀘스트에 담겨있는 sort
    * @param sort
    * @return 오라클 필드명과 매칭시킨 정렬조건 sort를 반환한다.
    */
   private String getSort(String sort) {
      if(sort != null) {
         switch (sort.trim()) {
         case "최신순":
            sort = "write_date";
            break;
         case "댓글순":
            sort = "comment_cnt";   
            break;
         case "추천순":
            sort = "like_cnt";
            break;
         case "조회순":
            sort = "views";
            break;
         default: 
            sort = "write_date";
            break;
            
         }//end of switch-case---
         
      }
      else {
         sort = "write_date";
      }
      return sort;
   }
	
	
}
