package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {
    public static Connection getMysqlConnection() {
        String hostName = "localhost";
        String dbName   = "quan_ly_khoan_thu";
        String userName = "root";
        String password = "123456";
        return getMysqlConnection(hostName, dbName, userName, password);
    }

    public static Connection getMysqlConnection(String hostName, String dbName, String userName, String password) {
        Connection conn = null;
        String connectionUrl = "jdbc:mysql://localhost:3306/quan_ly_khoan_thu";
        try {
            // Đảm bảo Driver MySQL được load (JDBC 4.0+ thường không cần Class.forName)
            // Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(connectionUrl, userName, password);
        } 
        catch (SQLException e) {
            System.err.println("Không thể kết nối tới database!");
            System.err.println("URL: " + connectionUrl);
            System.err.println("Lỗi SQL State: " + e.getSQLState());
            System.err.println("Lỗi mã (Error Code): " + e.getErrorCode());
            System.err.println("Thông báo: " + e.getMessage());
            // In ra stack trace để debug nếu cần thiết
            e.printStackTrace();
        }
        catch (Exception e) {
            // Bắt các lỗi khác (ClassNotFoundException nếu bạn dùng Class.forName)
            System.err.println("Có lỗi xảy ra khi cố gắng load driver hoặc tạo kết nối:");
            e.printStackTrace();
        }
        return conn;  // nếu không kết nối được, conn vẫn là null
    }
}
