<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>つぶやきポスト</title>
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/auth.css">
</head>
<body class="auth-body">
<div class="auth-container auth-container.large auth-container.with-sidebar">
  <div class="auth-content">
    <div class="auth-section welcome">
      <h1>つぶやきポスト</h1>
      <p>あなたの日常のつぶやきをシェアしましょう。</p>
    </div>
    
    <div class="auth-section form">
      <h2>ログイン</h2>
      <form action="Login" method="post">
        <div class="form-group">
          <label for="name">ユーザー名</label>
          <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
          <label for="pass">パスワード</label>
          <input type="password" id="pass" name="pass" required>
        </div>
        <div class="button-group single">
          <input type="submit" value="ログイン">
        </div>
      </form>
      
      <div class="register-link-section">
        <p>アカウントをお持ちでない方</p>
        <a href="Register" class="btn-register">新規登録</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>