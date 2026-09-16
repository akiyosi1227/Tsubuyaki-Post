<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Mutter, java.util.List, java.text.SimpleDateFormat" %>
<%
User loginUser = (User)session.getAttribute("loginUser");
List<Mutter> mutterList = (List<Mutter>)request.getAttribute("mutterList"); 
String errorMsg = (String)request.getAttribute("errorMsg");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>つぶやきポスト</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body class="main-body">
<div class="main-container">
  <div class="main-header">
    <div class="header-left">
      <h1>つぶやきポスト</h1>
      <p><%= loginUser.getName() %>さんでログイン中</p>
    </div>
    <div class="header-right">
      <a href="<%= request.getContextPath() %>/Main">更新</a>
      <a href="<%= request.getContextPath() %>/Logout">ログアウト</a>
    </div>
  </div>

  <div class="post-section">
    <form action="Main" method="post" class="post-form">
      <input type="hidden" name="action" value="post">
      <input type="text" name="text" placeholder="今何してますか？" required>
      <button type="submit" class="post-btn">つぶやく</button>
    </form>

    <div class="refresh-link">
      <a href="<%= request.getContextPath() %>/Main">↻ 最新情報を取得</a>
    </div>
  </div>

  <% if (errorMsg != null) { %>
    <div class="error"><%= errorMsg %></div>
  <% } %>

  <div class="mutter-list">
    <% if (mutterList != null && mutterList.size() > 0) {
        for (Mutter mutter : mutterList) { %>
      <div class="mutter-item">
        <div class="mutter-header">
          <span class="mutter-user"><%= mutter.getUserName() %></span>
          <% if (loginUser.getName().equals(mutter.getUserName())) { %>
            <form class="mutter-delete-form" action="Main" method="post">
              <input type="hidden" name="action" value="delete">
              <input type="hidden" name="mutterId" value="<%= mutter.getID() %>">
              <input type="submit" class="delete-btn" value="削除">
            </form>
          <% } %>
        </div>
        <div class="mutter-text"><%= mutter.getText() %></div>
        <div class="mutter-time"><%= sdf.format(mutter.getCreatedAt()) %></div>
      </div>
    <%   }
    } else { %>
      <div class="empty-message">
        <p>まだつぶやきがありません。<br>最初のつぶやきを投稿してみてください！</p>
      </div>
    <% } %>
  </div>
</div>
</body>
</html>