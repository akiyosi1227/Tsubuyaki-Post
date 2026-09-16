package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

/**
 * UserDAO - User データベース操作クラス
 * ユーザー認証とユーザー情報の取得を行います
 */
public class UserDAO {
	
	/**
	 * ユーザー名とパスワードでユーザーを検証
	 * @param userName ユーザー名
	 * @param password パスワード
	 * @return 認証成功時は true、失敗時は false
	 */
	public boolean authenticate(String userName, String password) {
		String sql = "SELECT id, name, pass FROM users WHERE name = ?";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setString(1, userName);
			
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					String storedPassword = rs.getString("pass");
					// パスワード検証
					// 注意: 実運用ではハッシュ化されたパスワードと比較すべき
					// 例: BCrypt.checkpw(password, storedPassword)
					return storedPassword.equals(password);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	/**
	 * ユーザー名からユーザー情報を取得
	 * @param userName ユーザー名
	 * @return ユーザー情報、見つからない場合は null
	 */
	public User findByName(String userName) {
		String sql = "SELECT id, name, pass FROM users WHERE name = ?";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setString(1, userName);
			
			try (ResultSet rs = pStmt.executeQuery()) {
				if (rs.next()) {
					String name = rs.getString("name");
					String pass = rs.getString("pass");
					return new User(name, pass);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * 新規ユーザーを登録
	 * @param userName ユーザー名
	 * @param password パスワード
	 * @return 登録成功時は true、失敗時は false
	 */
	public boolean create(String userName, String password) {
		String sql = "INSERT INTO users(name, pass) VALUES(?, ?)";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setString(1, userName);
			pStmt.setString(2, password);
			
			int result = pStmt.executeUpdate();
			return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ユーザー情報を更新
	 * @param userName ユーザー名
	 * @param newPassword 新しいパスワード
	 * @return 更新成功時は true、失敗時は false
	 */
	public boolean updatePassword(String userName, String newPassword) {
		String sql = "UPDATE users SET pass = ?, updated_at = CURRENT_TIMESTAMP WHERE name = ?";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setString(1, newPassword);
			pStmt.setString(2, userName);
			
			int result = pStmt.executeUpdate();
			return result >= 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ユーザーを削除
	 * @param userName ユーザー名
	 * @return 削除成功時は true、失敗時は false
	 */
	public boolean delete(String userName) {
		String sql = "DELETE FROM users WHERE name = ?";
		
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {
			
			pStmt.setString(1, userName);
			
			int result = pStmt.executeUpdate();
			return result >= 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
