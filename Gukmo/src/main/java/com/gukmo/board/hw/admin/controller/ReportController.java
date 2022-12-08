package com.gukmo.board.hw.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gukmo.board.hw.admin.service.InterReportService;
import com.gukmo.board.model.ReportVO;


@Controller
public class ReportController {
	@Autowired   // Type 에 따라 알아서 Bean 을 주입해준다.
	private InterReportService service;
	
	
	// =============== 신고내역 관리 시작 ====================//
	
	@RequestMapping(value="/admin/report/list.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임.
	public ModelAndView requiredAdminLogin_reportManage_List(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
	    List<ReportVO> reportList = null;
	    
	    String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String memberStatus = request.getParameter("report_type");
		
		// System.out.println(searchType);
		// System.out.println(searchWord);
		
		String str_page = request.getParameter("page");
		
		if(memberStatus == null) {
			memberStatus = "";
		}
		
		 if(searchType == null || (!"report_nickname".equals(searchType) && !"reported_nickname".equals(searchType) ) ) {
			searchType = "";
		 }
		
		 if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		 	searchWord = "";
		 }
		 
		 Map<String, String> paraMap = new HashMap<>();
		 paraMap.put("searchType", searchType);
		 paraMap.put("searchWord", searchWord);
		 paraMap.put("memberStatus", memberStatus);
		 // 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		 // 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을때로 나뉘어진다.
		 // 총 게시물 건수(totalCount)
		 int totalCount = service.getTotalCount_report(paraMap);
		 int sizePerPage = 10;         // 한 페이지당 보여줄 게시물 건수 
		 int totalPage = (int) Math.ceil( (double)totalCount/sizePerPage );
		 int page = getPage(str_page,totalPage);    // 현재 보여주는 페이지번호로서, 초기치로는 1페이지로 설정함.
			
		 paraMap = getRno(page,sizePerPage,paraMap);
		 String url = "reportManage_List.do";
		 
		 reportList = service.reportList(paraMap);
		 
		 
		Map<String,String> pageMap = new HashMap<>();
		pageMap.put("searchWord",searchWord);
		pageMap.put("searchType",searchType);
		pageMap.put("memberStatus",memberStatus);
		pageMap.put("keyWord", "report_type");
		
		String pageBar = getPageBar(page,totalPage,url,pageMap);
		
		
		if( !"".equals(searchType) && !"".equals(searchWord) ) {
			 mav.addObject("paraMap", paraMap);
		 }
		mav.addObject("pageBar", pageBar);
		mav.addObject("reportList", reportList);
		
		request.setAttribute("paraMap", paraMap);
		request.setAttribute("totalCount", totalCount);
		
		mav.setViewName("admin/report/list.tiles1");
		
		return mav;
	} // end of 신고내역 리스트 보기	
	
	
	
	// 신고 관련 정보 상세보기 
	@RequestMapping(value="/admin/reportDetail.do", method= {RequestMethod.GET})  // 오로지 GET 방식만 허락하는 것임.
	public ModelAndView requiredAdminLogin_reportDetail(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		Map<String, String> paraMap = new HashMap<>();
		String report_num = request.getParameter("report_num");
		paraMap.put("report_num", report_num);
		
		ReportVO reportDetail = service.getreportDetail(paraMap);
		
		String reportedNickname = reportDetail.getReported_nickname();
		
		String reportedId = service.getReportedId(reportedNickname);

		request.setAttribute("reportedId", reportedId);		
		mav.addObject("reportDetail", reportDetail);
		
		
		mav.setViewName("admin/report/detail.tiles1");
		return mav;
	}// 신고 관련 정보 상세보기 끝
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	    * 페이지바 만들기 메소드
	    * @param page(현재 페이지번호),totalPage(총페이지 수),반응할url,searchWord(검색어)
	    * @return pageBar
	    */
	   private String getPageBar(int page, int totalPage, String url, Map<String,String> pageMap) {
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
	                  "  <a class='page-link' href='"+url+"?"+pageMap.get("keyWord")+"="+pageMap.get("memberStatus")+"&searchType="+pageMap.get("searchType")+"&searchWord="+pageMap.get("searchWord")+"&page=1'>" + 
	                  "    <i class='fa-solid fa-angles-left'></i>" + 
	                  "  </a>" + 
	                  "</li>";
	         //[<]
	         pageBar += "<li class='page-item'>" + 
	                  "  <a class='page-link' href='"+url+"?"+pageMap.get("keyWord")+"="+pageMap.get("memberStatus")+"&searchType="+pageMap.get("searchType")+"&searchWord="+pageMap.get("searchWord")+"&page="+(pageNo-1)+"'>" + 
	                  "    <i class='fa-solid fa-angle-left'></i>" + 
	                  "  </a>" + 
	                  "</li>"; 
	      }
	      
	      while( !(loop > blockSize || pageNo > totalPage) ) {
	         
	         if(pageNo == page) {   //페이지번호가 현재페이지번호와 같다면 .active
	            pageBar += "<li class='page-item active' aria-current='page'>" + 
	                     "  <a class='page-link' href='#'>"+pageNo+"</a>" + 
	                     "</li>";  
	         }
	         
	         else {   //페이지번호가 현재페이지번호랑 다르다면 .active 뺌
	            pageBar += "<li class='page-item'>" + 
	                     "  <a class='page-link' href='"+url+"?"+pageMap.get("keyWord")+"="+pageMap.get("memberStatus")+"&searchType="+pageMap.get("searchType")+"&searchWord="+pageMap.get("searchWord")+"&page="+pageNo+"'>"+pageNo+"</a>" + 
	                     "</li>";        
	         }
	         
	         loop++;
	         pageNo++;
	      }// end of while--------------------------
	      
	      // === [>][>>] 만들기 === //
	      if( pageNo <= totalPage) {
	         //[>]
	         pageBar += "<li class='page-item'>" + 
	                  "  <a class='page-link' href='"+url+"?"+pageMap.get("keyWord")+"="+pageMap.get("memberStatus")+"&searchType="+pageMap.get("searchType")+"&searchWord="+pageMap.get("searchWord")+"&page="+pageNo+"'>"+
	                  "    <i class='fa-solid fa-angle-right'></i>"+
	                  "  </a>" + 
	                  "</li>";
	         
	         //[>>] 
	         pageBar += "<li class='page-item'>" + 
	                  "  <a class='page-link' href='"+url+"?"+pageMap.get("keyWord")+"="+pageMap.get("memberStatus")+"&searchType="+pageMap.get("searchType")+"&searchWord="+pageMap.get("searchWord")+"&page="+totalPage+"'>"+
	                  "    <i class='fas fa-solid fa-angles-right'></i>"+
	                  "  </a>" + 
	                  "</li>";
	      }
	      
	      pageBar += "</ul>";
	      
	      return pageBar;
	    }//end of getPageBar(){}---
	   
	   
   
   
   
   
   /**
    * 페이지 번호 예외처리하기
    * @param str_page(쿼리스트링으로 날아온 페이지),totalPage(총페이지수)
    * @return page(현재 페이지번호)
    */
   private int getPage(String str_page,int totalPage) {
      int page = 0;
      if(str_page == null) {   //쿼리스트링에 페이지가 없다면
          // 게시판에 보여지는 초기화면 
          page = 1;
      } else {
          try {
             page = Integer.parseInt(str_page);
             if( page < 1) {   //페이지가 1페이지보다 작은경우
                page = 1;
             }
             else if(page > totalPage) { //페이지가 총페이지보다 큰 경우
                page = totalPage;
             }
          } catch(NumberFormatException e) {   //페이지번호에 글자를 써서 들어올 경우 오류방지
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
