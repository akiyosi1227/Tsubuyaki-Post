package model;

import dao.UserDAO;

/**
 * UserRegistrationLogic - ユーザー登録ロジック
 * 新規ユーザーの登録機能を提供します
 */
public class UserRegistrationLogic {
	private UserDAO userDAO;
	
	public UserRegistrationLogic() {
		this.userDAO = new UserDAO();
	}
	
	/**
	 * 新規ユーザーを登録
	 * @param userName ユーザー名
	 * @param password パスワード
	 * @return 登録成功時は true、失敗時は false
	 */
	public boolean register(String userName, String password) {
		// ユーザー名が既に存在するか確認
		if (userDAO.findByName(userName) != null) {
			return false; // ユーザーが既に存在
		}
		
		// 新規ユーザーを作成
		return userDAO.create(userName, password);
	}
	
	/**
	 * パスワードを変更
	 * @param userName ユーザー名
	 * @param oldPassword 現在のパスワード
	 * @param newPassword 新しいパスワード
	 * @return 変更成功時は true、失敗時は false
	 */
	public boolean changePassword(String userName, String oldPassword, String newPassword) {
		// 現在のパスワードを確認
		if (!userDAO.authenticate(userName, oldPassword)) {
			return false;
		}
		
		// パスワードを更新
		return userDAO.updatePassword(userName, newPassword);
	}
}
