package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.DotThuModel;

public class DotThuService {

    /**
     * Trả về danh sách tất cả các đợt thu
     */
    public List<DotThuModel> getAll() throws ClassNotFoundException, SQLException {
        List<DotThuModel> list = new ArrayList<>();
        String sql = "SELECT MaDotThu, TenDotThu, MaKhoanThu, NgayBatDau, NgayKetThuc FROM dot_thu";
        try (Connection conn = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DotThuModel dt = new DotThuModel(
                        rs.getInt("MaDotThu"),
                        rs.getString("TenDotThu"),
                        rs.getInt("MaKhoanThu"),
                        rs.getDate("NgayBatDau"),
                        rs.getDate("NgayKetThuc")
                );
                list.add(dt);
            }
        }
        return list;
    }

    /**
     * Trả về danh sách các đợt thu của khoản thu maKhoanThu
     */
    public List<DotThuModel> getDotThuByKhoan(int maKhoanThu)
            throws ClassNotFoundException, SQLException {
        List<DotThuModel> list = new ArrayList<>();
        String sql = "SELECT MaDotThu, TenDotThu, MaKhoanThu, NgayBatDau, NgayKetThuc "
                + "FROM dot_thu WHERE MaKhoanThu = ?";
        try (Connection conn = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKhoanThu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DotThuModel dt = new DotThuModel(
                            rs.getInt("MaDotThu"),
                            rs.getString("TenDotThu"),
                            rs.getInt("MaKhoanThu"),
                            rs.getDate("NgayBatDau"),
                            rs.getDate("NgayKetThuc")
                    );
                    list.add(dt);
                }
            }
        }
        return list;
    }

    /**
     * Thêm mới một đợt thu
     */
    public boolean addDotThu(DotThuModel model) throws ClassNotFoundException, SQLException {
        String sql = "INSERT INTO dot_thu (TenDotThu, MaKhoanThu, NgayBatDau, NgayKetThuc) VALUES (?, ?, ?, ?)";
        try (Connection conn = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, model.getTenDotThu());
            ps.setInt   (2, model.getMaKhoanThu());
            ps.setDate  (3, new java.sql.Date(model.getNgayBatDau().getTime()));
            if (model.getNgayKetThuc() != null) {
                ps.setDate(4, new java.sql.Date(model.getNgayKetThuc().getTime()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    model.setMaDotThu(keys.getInt(1));
                }
            }
            return true;
        }
    }
    public boolean updateDotThu(DotThuModel model) throws ClassNotFoundException, SQLException {
        String sql = "UPDATE dot_thu "
                + "SET TenDotThu = ?, NgayBatDau = ?, NgayKetThuc = ? "
                + "WHERE MaDotThu = ?";
        try (Connection conn = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, model.getTenDotThu());
            ps.setDate(2, new java.sql.Date(model.getNgayBatDau().getTime()));
            if (model.getNgayKetThuc() != null) {
                ps.setDate(3, new java.sql.Date(model.getNgayKetThuc().getTime()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            ps.setInt(4, model.getMaDotThu());

            int updated = ps.executeUpdate();
            return updated > 0;
        }
    }
}
