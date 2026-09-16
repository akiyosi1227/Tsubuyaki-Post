package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.DBUtil;

@WebServlet("/DbDebug")
public class DbDebug extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("DbDebug - debugging database connectivity and user data");

            // Check db.properties on classpath
            out.println("\nChecking /db.properties on classpath:");
            try (InputStream in = DBUtil.class.getResourceAsStream("/db.properties")) {
                if (in == null) {
                    out.println("  NOT FOUND: /db.properties is not on the runtime classpath");
                } else {
                    out.println("  FOUND: /db.properties contents:");
                    Properties p = new Properties();
                    p.load(in);
                    p.forEach((k, v) -> out.println("    " + k + "=" + v));
                }
            } catch (IOException e) {
                out.println("  Failed to read /db.properties: " + e.getMessage());
            }

            out.println("\nAttempting JDBC connection via DBUtil.getConnection():");
            try (Connection conn = DBUtil.getConnection()) {
                out.println("  Connection successful: " + (conn != null && !conn.isClosed()));

                // Query sample user 'testuser'
                String sql = "SELECT name, pass FROM users WHERE name = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, "testuser");
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            out.println("\n  Found user 'testuser':");
                            out.println("    name = " + rs.getString("name"));
                            out.println("    pass = " + rs.getString("pass"));
                        } else {
                            out.println("  User 'testuser' not found in users table");
                        }
                    }
                }

            } catch (SQLException e) {
                out.println("  JDBC error: " + e.getMessage());
                e.printStackTrace(out);
            } catch (Exception e) {
                out.println("  Unexpected error: " + e.getMessage());
                e.printStackTrace(out);
            }
        }
    }
}
