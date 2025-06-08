package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.KhoanThuModel;

public class KhoanThuService {

	// Thêm khoản thu
	public boolean add(KhoanThuModel model) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO khoan_thu "
				+ "(TenKhoanThu, SoTien, LoaiKhoanThu, CachTinh, DonViTinh, MoTa) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, model.getTenKhoanThu());
			stmt.setDouble(2, model.getSoTien());
			String loaiStr = model.getLoaiKhoanThu() == 1 ? "BatBuoc" : "DongGop";
			stmt.setString(3, loaiStr);
			stmt.setString(4, model.getCachTinh());
			stmt.setString(5, model.getDonViTinh());
			stmt.setString(6, model.getMoTa());
			stmt.executeUpdate();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					model.setMaKhoanThu(keys.getInt(1));
				}
			}
		}
		return true;
	}

	// Xóa khoản thu (và các bản ghi liên quan)
	public boolean del(int maKhoanThu) throws ClassNotFoundException, SQLException {
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt1 = conn.prepareStatement(
					 "DELETE FROM nop_tien WHERE MaKhoanThu = ?");
			 PreparedStatement stmt2 = conn.prepareStatement(
					 "DELETE FROM khoan_thu WHERE MaKhoanThu = ?")) {

			stmt1.setInt(1, maKhoanThu);
			stmt1.executeUpdate();

			stmt2.setInt(1, maKhoanThu);
			stmt2.executeUpdate();
		}
		return true;
	}

	// Cập nhật khoản thu
	public boolean update(KhoanThuModel model) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE khoan_thu SET "
				+ "TenKhoanThu = ?, SoTien = ?, LoaiKhoanThu = ?, "
				+ "CachTinh = ?, DonViTinh = ?, MoTa = ? "
				+ "WHERE MaKhoanThu = ?";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, model.getTenKhoanThu());
			stmt.setDouble(2, model.getSoTien());
			String loaiStr = model.getLoaiKhoanThu() == 1 ? "BatBuoc" : "DongGop";
			stmt.setString(3, loaiStr);
			stmt.setString(4, model.getCachTinh());
			stmt.setString(5, model.getDonViTinh());
			stmt.setString(6, model.getMoTa());
			stmt.setInt(7, model.getMaKhoanThu());
			stmt.executeUpdate();
		}
		return true;
	}

	// Lấy danh sách khoản thu
	public List<KhoanThuModel> getListKhoanThu() throws ClassNotFoundException, SQLException {
		List<KhoanThuModel> list = new ArrayList<>();
		String sql = "SELECT MaKhoanThu, TenKhoanThu, SoTien, LoaiKhoanThu, CachTinh, DonViTinh, MoTa, "
				+ "DATE_FORMAT(NgayTao, '%Y-%m-%d %H:%i:%s') AS NgayTao "
				+ "FROM khoan_thu";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				String loaiDb = rs.getString("LoaiKhoanThu");
				int loai = "BatBuoc".equals(loaiDb) ? 1 : 0;
				KhoanThuModel kt = new KhoanThuModel(
						rs.getInt("MaKhoanThu"),
						rs.getString("TenKhoanThu"),
						rs.getDouble("SoTien"),
						loai,
						rs.getString("CachTinh"),
						rs.getString("DonViTinh"),
						rs.getString("MoTa"),
						rs.getString("NgayTao")
				);
				list.add(kt);
			}
		}
		return list;
	}

	// Tìm khoản thu theo ID
	public KhoanThuModel findById(int maKhoanThu) throws ClassNotFoundException, SQLException {
		String sql = "SELECT MaKhoanThu, TenKhoanThu, SoTien, LoaiKhoanThu, CachTinh, DonViTinh, MoTa, "
				+ "DATE_FORMAT(NgayTao, '%Y-%m-%d %H:%i:%s') AS NgayTao "
				+ "FROM khoan_thu WHERE MaKhoanThu = ?";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, maKhoanThu);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int loai = "BatBuoc".equals(rs.getString("LoaiKhoanThu")) ? 1 : 0;
					return new KhoanThuModel(
							rs.getInt("MaKhoanThu"),
							rs.getString("TenKhoanThu"),
							rs.getDouble("SoTien"),
							loai,
							rs.getString("CachTinh"),
							rs.getString("DonViTinh"),
							rs.getString("MoTa"),
							rs.getString("NgayTao")
					);
				}
			}
		}
		return null;
	}

	// Đếm tổng số khoản thu
	public long countKhoanThu() throws ClassNotFoundException, SQLException {
		String sql = "SELECT COUNT(*) FROM khoan_thu";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return rs.getLong(1);
			}
		}
		return 0;
	}

	// Đếm theo loại (1 = bắt buộc, 0 = tự nguyện)
	public long countByLoai(int loaiInt) throws ClassNotFoundException, SQLException {
		String loaiStr = loaiInt == 1 ? "BatBuoc" : "DongGop";
		String sql = "SELECT COUNT(*) FROM khoan_thu WHERE LoaiKhoanThu = ?";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, loaiStr);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getLong(1);
				}
			}
		}
		return 0;
	}
}
