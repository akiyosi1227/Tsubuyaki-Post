<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String name = (String)request.getAttribute("name");
String errorMsg = (String)request.getAttribute("errorMsg");
if (name == null) { name = ""; }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>新規ユーザー登録</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
<div class="auth-container">
  <div class="auth-section form">
    <h1>新規ユーザー登録</h1>

    <% if (errorMsg != null) { %>
      <div class="error-message"><%= errorMsg %></div>
    <% } %>

    <form action="Register" method="post">
      <input type="hidden" name="action" value="form">
      
      <div class="form-group">
        <label for="name">ユーザー名</label>
        <input type="text" id="name" name="name" value="<%= name %>" required>
      </div>
      
      <div class="form-group">
        <label for="pass">パスワード</label>
        <input type="password" id="pass" name="pass" required>
      </div>
      
      <div class="form-group">
        <label for="passConfirm">パスワード（確認）</label>
        <input type="password" id="passConfirm" name="passConfirm" required>
      </div>
      
      <div class="button-group single">
        <input type="submit" value="確認へ進む">
      </div>
    </form>

    <div class="link-section">
      <a href="<%= request.getContextPath() %>/index.jsp" class="btn-link">← ログイン画面に戻る</a>
    </div>
  </div>
</div>
</body>
</html>
