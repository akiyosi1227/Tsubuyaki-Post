<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログアウト</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auth.css">
</head>
<body class="auth-body">
<div class="auth-container">
  <div class="auth-section center-text">
    <!-- <div class="icon">👋</div> -->
    <h1>ログアウトしました</h1>
    
    <div class="message">
      ご利用ありがとうございました。<br>
    </div>
    
    <div class="button-group single">
      <a href="<%= request.getContextPath() %>/index.jsp" class="btn-primary">トップページに戻る</a>
    </div>
  </div>
</div>
</body>
</html>