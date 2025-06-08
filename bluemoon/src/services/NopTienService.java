package services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.NopTienModel;

public class NopTienService {

	/**
	 * Thêm mới nộp tiền, bao gồm người nộp, khoản thu, đợt thu, số tiền đã nộp và trạng thái.
	 */
	public boolean add(NopTienModel model) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO nop_tien "
				+ "(IDNhanKhau, MaKhoanThu, MaDotThu, NgayThu, SoTienDaNop, TrangThai) "
				+ "VALUES (?, ?, ?, NOW(), ?, ?)";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			// 1. Người nộp
			stmt.setInt(1, model.getIdNhanKhau());
			// 2. Khoản thu
			stmt.setInt(2, model.getMaKhoanThu());
			// 3. Đợt thu
			stmt.setInt(3, model.getMaDotThu());
			// 4. Số tiền
			stmt.setDouble(4, model.getSoTienDaNop());
			// 5. Trạng thái
			stmt.setString(5, model.getTrangThai());

			stmt.executeUpdate();

			// Lấy IDNopTien tự sinh
			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					model.setIdNopTien(keys.getInt(1));
				}
			}
			return true;
		}
	}

	/**
	 * Xóa bản ghi nộp tiền theo IDNopTien và MaKhoanThu.
	 */
	public boolean del(int idNopTien, int maKhoanThu) throws ClassNotFoundException, SQLException {
		String sql = "DELETE FROM nop_tien WHERE IDNopTien = ? AND MaKhoanThu = ?";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idNopTien);
			stmt.setInt(2, maKhoanThu);
			stmt.executeUpdate();
			return true;
		}
	}

	/**
	 * Lấy danh sách nộp tiền, bao gồm cả IDNhanKhau và MaDotThu để hiển thị hoặc xử lý.
	 */
	public List<NopTienModel> getListNopTien() throws ClassNotFoundException, SQLException {
		List<NopTienModel> list = new ArrayList<>();
		String sql = "SELECT IDNopTien, IDNhanKhau, MaKhoanThu, MaDotThu, NgayThu, SoTienDaNop, TrangThai "
				+ "FROM nop_tien";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				NopTienModel m = new NopTienModel();
				m.setIdNopTien   (rs.getInt("IDNopTien"));
				m.setIdNhanKhau  (rs.getInt("IDNhanKhau"));   // ← đọc IDNhanKhau
				m.setMaKhoanThu  (rs.getInt("MaKhoanThu"));
				m.setMaDotThu    (rs.getInt("MaDotThu"));
				m.setNgayThu     (rs.getDate("NgayThu"));
				m.setSoTienDaNop (rs.getDouble("SoTienDaNop"));
				m.setTrangThai   (rs.getString("TrangThai"));
				list.add(m);
			}
		}
		return list;
	}
}
