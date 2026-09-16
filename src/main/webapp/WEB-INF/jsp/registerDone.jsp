<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
Boolean isSuccess = (Boolean)request.getAttribute("isSuccess");
String registeredName = (String)request.getAttribute("registeredName");
String errorMsg = (String)request.getAttribute("errorMsg");
if (isSuccess == null) { isSuccess = false; }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>登録結果</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
<div class="auth-container">
  <div class="auth-section center-text">
    <% if (isSuccess) { %>
      <div class="icon">✓</div>
      <h1>登録完了</h1>
      
      <div class="message-box success">
        <strong>ユーザー登録が完了しました！</strong>
        <p>ユーザー名 <strong><%= registeredName %></strong> でログインしてご利用ください。</p>
      </div>
      
      <div class="button-group single">
        <a href="<%= request.getContextPath() %>/index.jsp" class="btn-primary">ログインへ進む</a>
      </div>
    <% } else { %>
      <div class="icon">!</div>
      <h1>登録失敗</h1>
      
      <div class="message-box error">
        <strong>エラーが発生しました</strong>
        <% if (errorMsg != null) { %>
          <p><%= errorMsg %></p>
        <% } else { %>
          <p>ユーザー登録処理でエラーが発生しました。もう一度お試しください。</p>
        <% } %>
      </div>
      
      <div class="button-group single">
        <a href="<%= request.getContextPath() %>/index.jsp" class="btn-primary">ログイン画面に戻る</a>
      </div>
    <% } %>
  </div>
</div>
</body>
</html>
