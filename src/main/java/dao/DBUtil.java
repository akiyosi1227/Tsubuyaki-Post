package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DBUtil.class.getResourceAsStream("/db.properties")) {
            Properties prop = new Properties();
            if (in != null) {
                prop.load(in);
                driver = prop.getProperty("db.driver");
                url = prop.getProperty("db.url");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");
            } else {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            Class.forName(driver);
        } catch (IOException e) {
            throw new IllegalStateException("db.properties の読み込みに失敗しました", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBCドライバが見つかりませんでした", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
