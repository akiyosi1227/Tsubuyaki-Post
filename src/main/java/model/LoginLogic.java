package model;

import dao.UserDAO;

/**
 * LoginLogic - ユーザー認証ロジック
 * PostgreSQL データベースを使用してユーザーを認証します
 */
public class LoginLogic {
	private UserDAO userDAO;
	
	public LoginLogic() {
		this.userDAO = new UserDAO();
	}
	
	/**
	 * ユーザーを認証
	 * @param user 認証対象のユーザーオブジェクト
	 * @return 認証成功時は true、失敗時は false
	 */
	public boolean excute(User user) {
		// データベースからユーザーを認証
		return userDAO.authenticate(user.getName(), user.getPass());
	}
	
	/**
	 * ユーザー名からユーザー情報を取得
	 * @param userName ユーザー名
	 * @return ユーザー情報、見つからない場合は null
	 */
	public User getUser(String userName) {
		return userDAO.findByName(userName);
	}
}
