package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import model.User;

/**
 * ユーザー認証とユーザー情報のデータベース操作を担当します。
 */
public class UserDAO {

    /**
     * ユーザー名とパスワードを検証します。
     *
     * @param userName ユーザー名
     * @param password 入力された平文パスワード
     * @return 認証成功時はtrue、失敗時はfalse
     */
    public boolean authenticate(String userName, String password) {
        String sql =
                "SELECT id, name, pass FROM users WHERE name = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pStmt =
                        conn.prepareStatement(sql)) {

            pStmt.setString(1, userName);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                String passwordHash = rs.getString("pass");

                if (passwordHash == null
                        || !passwordHash.startsWith("$2")) {
                    return false;
                }

                return BCrypt.checkpw(password, passwordHash);
            }
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ユーザー名からユーザー情報を取得します。
     *
     * @param userName ユーザー名
     * @return ユーザー情報。見つからない場合はnull
     */
    public User findByName(String userName) {
        String sql =
                "SELECT id, name, pass FROM users WHERE name = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pStmt =
                        conn.prepareStatement(sql)) {

            pStmt.setString(1, userName);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String passwordHash = rs.getString("pass");

                    return new User(name, passwordHash);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 新しいユーザーを登録します。
     *
     * @param userName ユーザー名
     * @param password 平文パスワード
     * @return 登録成功時はtrue、失敗時はfalse
     */
    public boolean create(String userName, String password) {
        String sql =
                "INSERT INTO users(name, pass) VALUES(?, ?)";

        String passwordHash =
                BCrypt.hashpw(password, BCrypt.gensalt(12));

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pStmt =
                        conn.prepareStatement(sql)) {

            pStmt.setString(1, userName);
            pStmt.setString(2, passwordHash);

            return pStmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ユーザーのパスワードを更新します。
     *
     * @param userName ユーザー名
     * @param newPassword 新しい平文パスワード
     * @return 更新成功時はtrue、失敗時はfalse
     */
    public boolean updatePassword(
            String userName,
            String newPassword) {

        String sql =
                "UPDATE users "
                + "SET pass = ?, updated_at = CURRENT_TIMESTAMP "
                + "WHERE name = ?";

        String passwordHash =
                BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pStmt =
                        conn.prepareStatement(sql)) {

            pStmt.setString(1, passwordHash);
            pStmt.setString(2, userName);

            return pStmt.executeUpdate() >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ユーザーを削除します。
     *
     * @param userName ユーザー名
     * @return 削除成功時はtrue、失敗時はfalse
     */
    public boolean delete(String userName) {
        String sql =
                "DELETE FROM users WHERE name = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pStmt =
                        conn.prepareStatement(sql)) {

            pStmt.setString(1, userName);

            return pStmt.executeUpdate() >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
