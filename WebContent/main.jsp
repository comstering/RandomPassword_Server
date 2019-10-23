<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>RandomPassword WebSite</title>
</head>
<body>
	<%= session.getAttribute("userID") %> 님 환영합니다.
	<input type="button" value="로그아웃" onClick="window.location='logout.jsp'">
	
</body>
</html>