package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Mutter;

public class MuttersDAO {
	public List<Mutter> findAll() {
		List<Mutter> mutterList = new ArrayList<>();

		String sql = "SELECT id, name, text, created_at FROM mutters ORDER BY id DESC";

		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql);
			 ResultSet rs = pStmt.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String userName = rs.getString("name");
				String text = rs.getString("text");
				java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
				Mutter mutter = new Mutter(id, userName, text, createdAt);
				mutterList.add(mutter);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return mutterList;
	}

	public boolean create(Mutter mutter) {
		String sql = "INSERT INTO mutters(name, text) VALUES(?, ?)";

		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setString(1, mutter.getUserName());
			pStmt.setString(2, mutter.getText());

			int result = pStmt.executeUpdate();

			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean delete(int id, String userName) {
		String sql = "DELETE FROM mutters WHERE id = ? AND name = ?";

		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement pStmt = conn.prepareStatement(sql)) {

			pStmt.setInt(1, id);
			pStmt.setString(2, userName);

			int result = pStmt.executeUpdate();

			return result >= 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
