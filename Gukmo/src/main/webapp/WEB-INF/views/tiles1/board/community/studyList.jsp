<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<%-- 직접 만든 CSS --%>
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/resources/css/hyunwoo/boardList.css" />

<%-- 직접만든 javascript --%>
<script type="text/javascript" src="<%=ctxPath %>/resources/js/hyunwoo/studyList.js" ></script>

  <div class="container mt-4">
    <div id="category" class="d-flex justify-content-center align-content-center">
      <div class="mx-2 px-2 py-1 rounded">자유게시판</div>
      <div class="mx-2 px-2 py-1 rounded">Q&A</div>
      <div class="mx-2 px-2 py-1 rounded">정보공유</div>
      <div class="mx-2 px-2 py-1 rounded">스터디</div>
      <div class="mx-2 px-2 py-1 rounded">취미모임</div>
      <div class="mx-2 px-2 py-1 rounded active">수강/취업성공후기</div>
    </div>

     <%-- 검색창 영역 --%>
     <div class="searchBar d-flex mx-auto my-4">
        <input type="text" id="searchWord" class="pl-2" placeholder="검색할 내용을 입력해 주세요!"/>
        <button type="button" id="btn_search">
          <i class="fa-solid fa-magnifying-glass" style="color:#208EC9;"></i>
        </button>
     </div>
    <%-- 검색바 끝 --%>


    <%-- navbar 시작 --%>
    <div id="nav" class="d-flex align-items-center py-2">
      <%-- 필터 시작 이곳에 자바스크립트로 필터 넣으세요. --%>
      <div id="filter_area" class="d-flex align-items-end">
        
      </div>
      <%-- filter_area --%>

      
      <div class="d-flex ml-auto">
        <div id="btn_filter" class="d-flex justify-content-center align-items-center border rounded">
          <i class="fa-solid fa-filter"></i>
        </div>
  
        <div id="mask"></div>
        <div id="sort" class="d-flex ml-3 border rounded justify-content-center align-items-center">
          <i class="fa-solid fa-arrow-down-short-wide"></i>
          <span id=current_sort>최신순</span>
          <div id="sort_option" class="border rounded px-3 py-2">
            <span>최신순</span>
            <span>추천순</span>
            <span>댓글순</span>
            <span>조회순</span>
          </div>
        </div>
      </div>
    </div>
    <%-- 필터 끝 --%>



    <%------------------------------------- 게시판 리스트 시작 -------------------------------------%>
	
    <%-- 게시글 반복문 시작 --%>
    <c:forEach var="study" items="${requestScope.studies}">
    
    <div class="border-top px-2 py-2">
      <div class="d-flex align-items-center my-2">
        <%-- 작성자 프로필사진 --%>
        <a href="#" class="writer_image_box border">
          <img src="<%=ctxPath %>/resources/images/${study.profile_image}"/>
        </a>

        <%-- 작성자 닉네임 --%>
        <%-- 클릭하면 해당 유저의 활동내역 페이지로 이동하게 링크 거세요. --%>
        <a href="#" class="writer_nickname ml-2">
         	 ${study.nickname }
        </a>

        <%-- 작성자 활동점수 --%>
        <div class="writer_point ml-2">
          <i class="fa-solid fa-bolt"></i>
          <span>${study.writer_point}</span>
        </div>

        <%-- 작성일자 --%>
        <div class="write_date ml-2">
          ${study.write_date}
        </div>
      </div>

      <%-- 글제목 --%>
      <a href="#" class="subject align-items-center my-2">
        ${study.subject}
      </a>

      <div class="d-flex justify-content-between align-items-center my-2">
        <div class="d-flex align-items-center">
          <%-- 게시판상세카테고리 클릭하면 해당 게시판으로 이동하게 하세요 변수 말고 아예 값 박아도 됨--%>
          <div class="detail_category border rounded px-2 py-1">
          	  ${study.detail_category}
          </div>
          <div class="hashtag ml-1">
            <%-- 해시태그 리스트 들어갈 곳--%>
            <%-- 해시태그리스트 반복문시작 --%>
            <c:forEach var="hashtag" items="${study.hashtags}">
            <a href="#" class="hashtag mx-1">#<span>${hashtag.hashtag}</span></a>
            </c:forEach>
            <%-- 해시태그리스트 반복문 끝--%>
          </div>
        </div>

        <%-- 조회수,댓글수,추천수 --%>
        <div class="board_info_box d-flex justify-content-end">
          <%-- 조회수 --%>
          <div>
            <i class="fa-solid fa-eye"></i>
            <span>${study.views}</span>
          </div>

          <%-- 댓글수 --%>
          <div class="ml-2">
            <i class="fa-solid fa-comment-dots"></i>
            <span>${study.comment_cnt}</span>
          </div>

          <%-- 추천수 --%>
          <div class="ml-2">
            <i class="fa-solid fa-heart"></i>
            <span>${study.like_cnt}</span>
          </div>
        </div>
      </div>
    </div>
    </c:forEach>
    
    <%-- 게시글 반복문 끝 --%>
    
    
    
    <%----------------------------------- 게시판 리스트 끝 -------------------------------------%>

    <div class="d-flex border-top pt-3 justify-content-between">

      <div id="total_cnt">
        <%-- 총 건수 변수 들어갈 곳--%>
        총&nbsp;<span style="font-weight:bold;">${requestScope.totalCount}&nbsp;</span>건
      </div>

      <button type="button" id="btn_write" class="btn border-0 rounded">
        +스터디 등록
      </button>
    </div>




    <%----------------------------------------------------------- 페이지 바 시작 ---------------------------------------------%>
    <nav aria-label="...">
      ${requestScope.pageBar}
    </nav>
	<%----------------------------------------------------------- 페이지 바 끝 ---------------------------------------------%>
  </div>
