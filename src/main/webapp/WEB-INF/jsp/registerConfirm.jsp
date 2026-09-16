<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String name = (String)request.getAttribute("name");
String pass = (String)request.getAttribute("pass");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>登録確認</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
<div class="auth-container">
  <div class="auth-section center-text">
    <h1>登録内容確認</h1>
    <p class="subtitle">以下の内容で登録します</p>

    <div class="info-box">
      <div class="info-item">
        <div class="info-label">ユーザー名</div>
        <div class="info-value"><%= name %></div>
      </div>
      <div class="info-item">
        <div class="info-label">パスワード</div>
        <div class="info-value">●●●●●●●●</div>
      </div>
    </div>

    <form action="Register" method="post" style="margin-bottom: 10px;">
      <input type="hidden" name="action" value="confirm">
      <input type="hidden" name="name" value="<%= name %>">
      <input type="hidden" name="pass" value="<%= pass %>">
      <div class="button-group single">
        <input type="submit" value="登録を確定">
      </div>
    </form>

    <button type="button" class="btn-secondary" onclick="history.back();">戻る</button>
  </div>
</div>
</body>
</html>
