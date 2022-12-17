<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 직접만든 javascript -->
<script type="text/javascript" src="<%=ctxPath%>/resources/js/hasol/alarm.js"></script>

<div class="dropdown">
	<div>
	<a class="login_icon alarm_drop position-relative"> 
		<i class="fa-solid fa-bell fa-lg alarm_drop"></i>
	</a>
	<span id="alarm_cnt" class="position-absolute top-0 start-100 translate-middle badge rounded-pill text-white bg-danger"></span>
	</div>
	<div id="alarm_dropContent" class="dropdown-content1 mt-1">
		<a href="#">알림</a>
		<div class="div_alarm_content px-3 d-flex flex-column ">
			<!-- 알림 내용이 없을 경우 -->
			<!-- <p>받으신 알림이 없습니다.</p> -->

			<!-- 알림 내용이 있을 경우 (반복문) -->
			

		</div>
	</div>
</div>