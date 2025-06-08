package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.TamTruModel;

public class TamTruService {

    public TamTruModel getByNhanKhauId(int idNhanKhau) {
        TamTruModel result = null;

        try {
            Connection connection = MysqlConnection.getMysqlConnection(); // Không còn throws ClassNotFoundException
            String sql = "SELECT * FROM tam_tru WHERE IDNhanKhau = ? ORDER BY NgayBatDau DESC LIMIT 1";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idNhanKhau);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result = new TamTruModel();
                result.setId(rs.getInt("ID"));
                result.setIdNhanKhau(rs.getInt("IDNhanKhau"));
                result.setDiaChiTamTru(rs.getString("DiaChiTamTru"));
                result.setNgayBatDau(rs.getDate("NgayBatDau"));
                result.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                result.setGhiChu(rs.getString("GhiChu"));
            }

            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean add(TamTruModel model) {
        try {
            Connection connection = MysqlConnection.getMysqlConnection();
            String sql = "INSERT INTO tam_tru (IDNhanKhau, DiaChiTamTru, NgayBatDau, NgayKetThuc, GhiChu) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, model.getIdNhanKhau());
            ps.setString(2, model.getDiaChiTamTru());
            ps.setDate(3, model.getNgayBatDau());
            ps.setDate(4, model.getNgayKetThuc());
            ps.setString(5, model.getGhiChu());

            ps.executeUpdate();
            ps.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteByNhanKhauId(int idNhanKhau) {
        try {
            Connection connection = MysqlConnection.getMysqlConnection();
            String sql = "DELETE FROM tam_tru WHERE IDNhanKhau = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idNhanKhau);
            ps.executeUpdate();

            ps.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update(TamTruModel model) {
        try {
            Connection connection = MysqlConnection.getMysqlConnection();
            String sql = "UPDATE tam_tru SET DiaChiTamTru = ?, NgayBatDau = ?, NgayKetThuc = ?, GhiChu = ? WHERE ID = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, model.getDiaChiTamTru());
            ps.setDate(2, model.getNgayBatDau());
            ps.setDate(3, model.getNgayKetThuc());
            ps.setString(4, model.getGhiChu());
            ps.setInt(5, model.getId());
            ps.executeUpdate();
            ps.close();
            connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
