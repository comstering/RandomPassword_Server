<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DBConnector.DBConnect"%>
<%@ page import="java.io.PrintWriter" %> <!-- 자바 클래스 사용 -->
<!-- 한명의 회원정보를 담는 user클래스를 자바 빈즈로 사용 / scope:페이지 현재의 페이지에서만 사용-->
<jsp:useBean id="user" class="DBConnector.User" scope="page" />
<jsp:setProperty name="user" property="userID" />
<jsp:setProperty name="user" property="userPassword" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>RandomPassword WebSite</title>
</head>
<body>
	<%
		DBConnect connectDB = new DBConnect(); //인스턴스생성
		String result = connectDB.logindb(user.getUserID(), user.getUserPassword());
		//로그인 성공
		if(result.equals("true")){
			session.setAttribute("userID", user.getUserID());
			response.sendRedirect("main.jsp");
			
		}
		//로그인 실패
		else if(result.equals("false")){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('비밀번호가 틀립니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
		//아이디 없음
		else if(result.equals("noId")){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('존재하지 않는 아이디 입니다.')");
		script.println("history.back()");
		script.println("</script>");
		}
		//DB오류
		else if(result.equals("error")){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('DB오류가 발생했습니다.')");
		script.println("history.back()");
		script.println("</script>");
		}
	%>
</body>
</html>