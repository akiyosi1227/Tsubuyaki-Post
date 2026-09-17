package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static final String DEFAULT_DRIVER = "org.postgresql.Driver";

    private static String url;
    private static String user;
    private static String password;

    static {
        String environmentUrl = System.getenv("DB_URL");
        String environmentUser = System.getenv("DB_USER");
        String environmentPassword = System.getenv("DB_PASSWORD");

        try {
            if (isPresent(environmentUrl)
                    && isPresent(environmentUser)
                    && isPresent(environmentPassword)) {

                // Renderなどの公開環境では環境変数を使用
                url = environmentUrl;
                user = environmentUser;
                password = environmentPassword;

                Class.forName(DEFAULT_DRIVER);
            } else {
                // ローカル開発環境ではdb.propertiesを使用
                loadLocalProperties();
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "JDBCドライバが見つかりませんでした", e);
        }
    }

    private static void loadLocalProperties()
            throws ClassNotFoundException {

        try (InputStream in =
                DBUtil.class.getResourceAsStream("/db.properties")) {

            if (in == null) {
                throw new IllegalStateException(
                        "DB接続情報が設定されていません");
            }

            Properties prop = new Properties();
            prop.load(in);

            String driver =
                    prop.getProperty("db.driver", DEFAULT_DRIVER);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

            Class.forName(driver);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "db.propertiesの読み込みに失敗しました", e);
        }
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                url,
                user,
                password);
    }
}

