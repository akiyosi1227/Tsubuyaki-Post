package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.GetMutterListLogic;
import model.Mutter;
import model.PostMutterLogic;
import model.DeleteMutterLogic;
import model.User;

@WebServlet("/Main")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		GetMutterListLogic getMutterListLogic = new GetMutterListLogic();
		List<Mutter> mutterList = getMutterListLogic.execute();
		request.setAttribute("mutterList", mutterList);
		
		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");
		
		if (loginUser == null) {
			response.sendRedirect("index.jsp");
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
			dispatcher.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");
		
		// つぶやき投稿の処理
		if (action == null || "post".equals(action)) {
			String text = request.getParameter("text");
			
			if (text != null && text.length() != 0) {
				Mutter mutter = new Mutter(loginUser.getName(), text);
				PostMutterLogic postMutterLogic = new PostMutterLogic();
				postMutterLogic.execute(mutter);
			} else {
				request.setAttribute("errorMsg", "つぶやきが入力されていません");
			}
		} 
		// つぶやき削除の処理
		else if ("delete".equals(action)) {
			String mitterIdStr = request.getParameter("mutterId");
			try {
				int mutterId = Integer.parseInt(mitterIdStr);
				DeleteMutterLogic deleteMutterLogic = new DeleteMutterLogic();
				boolean isDeleted = deleteMutterLogic.execute(mutterId, loginUser.getName());
				
				if (!isDeleted) {
					request.setAttribute("errorMsg", "つぶやき削除に失敗しました。他のユーザーの投稿は削除できません。");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("errorMsg", "無効なリクエストです");
			}
		}
		
		// つぶやき一覧を取得して表示
		GetMutterListLogic getMutterListLogic = new GetMutterListLogic();
		List<Mutter> mutterList = getMutterListLogic.execute();
		request.setAttribute("mutterList", mutterList);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/main.jsp");
		dispatcher.forward(request, response);
	}
}