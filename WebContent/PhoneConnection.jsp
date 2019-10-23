<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnector.*"%>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("id");
	String pwd = request.getParameter("pwd");
	String type = request.getParameter("type");
	//로그인 요청인지 회원가입 요청인지를 구분하여 메서드를 실행하도록합니다.
	//싱글톤 방식으로 자바 클래스를 불러옵니다.
	
	DBConnect connectDB = DBConnect.getInstance();
	
	if(type.equals("login")) {
		String returns = connectDB.logindb(id, pwd);
		out.clear();
		out.print(returns);
		out.flush();
	} else if(type.equals("join")) {
		String returns = connectDB.joindb(id, pwd);
		out.clear();
		out.print(returns);
		out.flush();
	} else if(type.equals("update")) {
		String returns = connectDB.update(id, pwd);
		out.clear();
		out.print(returns);
		out.flush();
	}
	else if(type.equals("find")) {
		String returns = connectDB.findPw(id);
		out.clear();
		out.print(returns);
		out.flush();
	}
%>