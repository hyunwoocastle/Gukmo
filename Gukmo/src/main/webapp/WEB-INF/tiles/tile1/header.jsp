<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- 직접 만든 CSS -->
<link rel="stylesheet"
	href="<%=ctxPath%>/resources/css/hasol/header.css">

<!-- 직접만든 javascript -->
<script type="text/javascript"
	src="<%=ctxPath%>/resources/js/hasol/header.js"></script>

<!-- 네비게이션 시작 -->
<nav bar class="mainNav_bar w-100">
	<nav class="navbar navbar-expand-lg bg-white mainNav w-100">

		<!-- 로고 및 메뉴 영역 -->
		<div class="main_left">

			<!-- Brand/logo -->
			<a
				class="navbar-brand d-flex justify-content-start align-items-center"
				href="<%=ctxPath%>/index.do" style="width: 100px;"> <img
				src="" alt="logo">
			</a>

			<!-- Links -->
			<nav>
				<ul class="mainCate">
					<li><a class="nav-link" href="<%=ctxPath%>/academy/academies.do">국비학원</a></li>
					<li><a class="nav-link"
						href="<%=ctxPath%>/community/freeBoards.do">커뮤니티</a></li>
					<li><a class="nav-link" href="<%=ctxPath%>/notices.do">공지사항</a></li>

					<!-- 관리자로 로그인 했을 경우 추가 메뉴 -->
					<c:if test="${sessionScope.user.userid eq 'admin'}">
						<div class="dropdown">
							<div class="adminMenu">
								<a class="nav-link adminMenu" onclick="">관리자 메뉴</a>
							</div>
							<div id="admin_dropContent" class="dropdown-content2 mt-2">
								<a href="<%=ctxPath%>/admin/memberManage_List.do">일반회원 관리</a> 
								<a href="<%=ctxPath%>/admin/academyManage_List.do">학원회원 관리</a>
								<a href="<%=ctxPath%>/admin/adManage_List.do">광고현황 관리</a>
								<a href="<%=ctxPath%>/admin/reportManage_List.do">신고현황 관리</a>
							</div>
						</div>
					</c:if>
				</ul>
			</nav>
		</div>


		<!-- login 메뉴 영역 -->
		<div class="main_right">

			<!-- 비로그인 시 -->
			<c:if test="${empty sessionScope.user}">
				<div class="non-login">
					<button type="button" class="btn_login" id="login"
						onclick="location.href='<%=ctxPath%>/login.do'">로그인</button>
					<button type="button" class="btn_regist" id="regist"
						onclick="location.href='<%=ctxPath%>/signup.do'">회원가입</button>
				</div>
			</c:if>

			<!-- 로그인 시 -->
			<c:if test="${not empty sessionScope.user}">
				<div class="login d-flex justify-content-between align-items-center">

					<!-- 북마크(스크랩) -->
					<a class="login_icon"> <i class="fa-regular fa-bookmark fa-lg"></i>
					</a>

					<!-- 알림 -->
					<div class="dropdown">
						<a class="login_icon alarm_drop"> <i
							class="fa-solid fa-bell fa-lg alarm_drop" onclick="drop_alarm()"></i>
						</a>
						<div id="alarm_dropContent" class="dropdown-content1 mt-1">
							<a href="#">알림</a>
							<div class="div_alarm_content px-3 d-flex flex-column ">
								<!-- 알림 내용이 없을 경우 -->
								<!-- <p>받으신 알림이 없습니다.</p> -->

								<!-- 알림 내용이 있을 경우 (반복문) -->								
								<div class="alarm_content" onclick="location.href='<%=ctxPath%>/community/questions.do'">
									<div class="alarm_info">
										<span class="like">좋아요 </span>
										<span>11:53</span>
									</div>
									<p class="alarm_text"> [안녕하세요. 질문이...] 글이 좋아요(1)를 받았습니다. </p>
								</div>
								
								<div class="alarm_content" onclick="location.href='<%=ctxPath%>/community/questions.do'">
									<div class="alarm_info">
										<span class="reple"> 댓글 </span>
										<span>11:53</span>
									</div>
									<p class="alarm_text"> [와 그거 정말 좋은 기능...] 댓글에 댓글(4)이 달렸습니다. </p>
								</div>
								
								<div class="alarm_content" onclick="location.href='<%=ctxPath%>/community/questions.do'">
									<div class="alarm_info">
										<span class="declare"> 신고 </span>
										<span>2022.11.22</span>
									</div>
									<p class="alarm_text"> [암튼 좀 짜증나네요.] 글에 신고(1)가 접수되었습니다. </p>
								</div>

							</div>
						</div>
					</div>
					<!-- 프로필 drop -->
					<div class="dropdown">
						<div class="dropbtn">
							<c:if test="${fn:substring(sessionScope.user.profile_image,0,4) != 'http'}">
			                  <img src="<%=ctxPath %>/resources/images/${sessionScope.user.profile_image}" onclick="drop_profile()"/>
			                </c:if>
			                <c:if test="${fn:substring(sessionScope.user.profile_image,0,4) == 'http'}">
			             	   <img src="${sessionScope.user.profile_image}" onclick="drop_profile()"/>
			                </c:if>
						</div>
						<div id="profile_dropContent" class="dropdown-content2">
							<div class="px-1 py-1">
								<a href="<%=ctxPath%>/member/myId.do"> <i
									class="fa-solid fa-user"></i> 내 계정
								</a> <a href="<%=ctxPath%>/member/myInfo.do"> <i
									class="fa-solid fa-gear"></i> 내 정보
								</a> <a href="<%=ctxPath%>/member/activities.do"> <i
									class="fa-solid fa-gear"></i> 활동내역
								</a> <a href="<%=ctxPath%>/logout.do ">로그아웃</a>
							</div>
						</div>
					</div>
				</div>
			</c:if>
		</div>
	</nav>
</nav>