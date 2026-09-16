package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.LoginLogic;
import model.User;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		
		User user = new User(name, pass);
		
		HttpSession oldSession = request.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate();
		}
		
		LoginLogic loginLogic = new LoginLogic();
		boolean isLogic = loginLogic.excute(user);
		
		if (isLogic) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", user);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/loginResult.jsp");
		dispatcher.forward(request, response);
	}
}
