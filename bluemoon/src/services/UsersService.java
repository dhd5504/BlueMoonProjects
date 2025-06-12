package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.UsersModel;

public class UsersService {

    public UsersModel login(String username, String password) throws ClassNotFoundException, SQLException {
        Connection conn = MysqlConnection.getMysqlConnection();
        String sql = "SELECT u.ID, u.username, u.passwd, v.TenVaiTro " +
                "FROM users u JOIN vai_tro v ON u.IDVaiTro = v.IDVaiTro " +
                "WHERE u.username = ? AND u.passwd = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            UsersModel user = new UsersModel();
            user.setId(rs.getInt("ID"));
            user.setUsername(rs.getString("username"));
            user.setPasswd(rs.getString("passwd"));
            user.setVaiTro(rs.getString("TenVaiTro"));
            return user;
        }

        return null;
    }
}
