package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import model.UserRegistrationLogic;

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 登録フォーム表示用GETリクエスト処理
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		// 登録フォームを表示
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerForm.jsp");
		dispatcher.forward(request, response);
	}
	
	/**
	 * 登録処理用POSTリクエスト処理
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		if (action == null || action.isEmpty() || "form".equals(action)) {
			// ステップ1: フォーム送信 → 確認画面へ
			handleFormSubmission(request, response);
		} else if ("confirm".equals(action)) {
			// ステップ2: 確認送信 → 登録実行
			handleRegistration(request, response);
		} else {
			// デフォルト: フォーム表示
			doGet(request, response);
		}
	}
	
	/**
	 * フォーム送信時の処理：確認画面へ遷移
	 */
	private void handleFormSubmission(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String passConfirm = request.getParameter("passConfirm");
		
		// バリデーション
		String errorMsg = null;
		if (name == null || name.isEmpty()) {
			errorMsg = "ユーザー名を入力してください";
		} else if (name.length() > 100) {
			errorMsg = "ユーザー名は100文字以内で入力してください";
		} else if (pass == null || pass.isEmpty()) {
			errorMsg = "パスワードを入力してください";
		} else if (!pass.equals(passConfirm)) {
			errorMsg = "パスワードが一致しません";
		}
		
		if (errorMsg != null) {
			// エラーがある場合は入力画面に戻す
			request.setAttribute("name", name);
			request.setAttribute("errorMsg", errorMsg);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerForm.jsp");
			dispatcher.forward(request, response);
			return;
		}
		
		// 確認画面へ
		request.setAttribute("name", name);
		request.setAttribute("pass", pass);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerConfirm.jsp");
		dispatcher.forward(request, response);
	}
	
	/**
	 * 登録確認送信時の処理：実際にユーザーを登録
	 */
	private void handleRegistration(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		
		UserRegistrationLogic registrationLogic = new UserRegistrationLogic();
		boolean isSuccess = registrationLogic.register(name, pass);
		
		request.setAttribute("isSuccess", isSuccess);
		if (!isSuccess) {
			request.setAttribute("errorMsg", "ユーザー登録に失敗しました。ユーザー名が既に使用されている可能性があります。");
		} else {
			request.setAttribute("registeredName", name);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registerDone.jsp");
		dispatcher.forward(request, response);
	}
}
