<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
User loginUser = (User)session.getAttribute("loginUser");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン結果</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
<div class="auth-container">
  <div class="auth-section center-text">
    <% if (loginUser != null) { %>
      <div class="icon">✓</div>
      <h1>ログイン成功</h1>
      
      <div class="message">ログインしました</div>
      <div class="welcome-text">ようこそ、<strong><%= loginUser.getName() %></strong> さん</div>
      
      <div class="button-group single">
        <a href="<%= request.getContextPath() %>/Main" class="btn-primary">つぶやき投稿・閲覧へ</a>
      </div>
    <% } else { %>
      <div class="icon">!</div>
      <h1>ログイン失敗</h1>
      
      <div class="error-message">
        ユーザー名またはパスワードが正しくありません
      </div>
      
      <div class="button-group single">
        <a href="<%= request.getContextPath() %>/index.jsp" class="btn-primary">もう一度ログインする</a>
      </div>
      
      <div>
        <a href="<%= request.getContextPath() %>/Register" class="secondary-link">アカウントを作成</a>
      </div>
    <% } %>
  </div>
</div>
</body>
</html>