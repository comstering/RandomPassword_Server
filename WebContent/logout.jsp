<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnector.*"%>

<%
	session.invalidate();
	response.sendRedirect("WebSite.jsp");
%>