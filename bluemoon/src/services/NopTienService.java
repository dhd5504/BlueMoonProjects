package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import models.NopTienModel;

public class NopTienService {

	/**
	 * Thêm mới nộp tiền.
	 */
	public boolean add(NopTienModel model) throws ClassNotFoundException, SQLException {
		String sql = "INSERT INTO nop_tien (IDNhanKhau, MaKhoanThu, MaDotThu, NgayThu, SoTienDaNop, TrangThai) " +
				"VALUES (?, ?, ?, NOW(), ?, ?)";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setInt(1, model.getIdNhanKhau());
			stmt.setInt(2, model.getMaKhoanThu());
			stmt.setInt(3, model.getMaDotThu());
			stmt.setDouble(4, model.getSoTienDaNop());
			stmt.setString(5, model.getTrangThai());

			stmt.executeUpdate();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					model.setIdNopTien(keys.getInt(1));
				}
			}
			return true;
		}
	}

	/**
	 * Xoá nộp tiền.
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
	 * Cập nhật số tiền, trạng thái, ngày thu theo IDNopTien.
	 */
	public boolean update(NopTienModel model) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE nop_tien SET SoTienDaNop = ?, TrangThai = ?, NgayThu = ? WHERE IDNopTien = ?";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setDouble(1, model.getSoTienDaNop());
			stmt.setString(2, model.getTrangThai());
			stmt.setDate(3, new java.sql.Date(model.getNgayThu().getTime()));
			stmt.setInt(4, model.getIdNopTien());

			int rows = stmt.executeUpdate();
			return rows > 0;
		}
	}

	/**
	 * Lấy danh sách nộp tiền và tính số tiền cần nộp.
	 */
	public List<NopTienModel> getListNopTien() throws ClassNotFoundException, SQLException {
		List<NopTienModel> list = new ArrayList<>();

		String sql = """
            SELECT nt.IDNopTien, nt.IDNhanKhau, nt.MaKhoanThu, nt.MaDotThu,
                   nt.NgayThu, nt.SoTienDaNop, nt.TrangThai,
                   kt.SoTien, kt.CachTinh, qh.MaHo
            FROM nop_tien nt
            JOIN khoan_thu kt ON nt.MaKhoanThu = kt.MaKhoanThu
            JOIN quan_he qh ON nt.IDNhanKhau = qh.IDThanhVien
        """;

		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				NopTienModel m = new NopTienModel();
				m.setIdNopTien(rs.getInt("IDNopTien"));
				m.setIdNhanKhau(rs.getInt("IDNhanKhau"));
				m.setMaKhoanThu(rs.getInt("MaKhoanThu"));
				m.setMaDotThu(rs.getInt("MaDotThu"));
				m.setNgayThu(rs.getDate("NgayThu"));
				m.setSoTienDaNop(rs.getDouble("SoTienDaNop"));
				m.setTrangThai(rs.getString("TrangThai"));

				// Tính số tiền cần nộp
				double soTien = rs.getDouble("SoTien");
				String cachTinh = rs.getString("CachTinh");
				int maHo = rs.getInt("MaHo");

				double soTienCanNop = "TheoNguoi".equalsIgnoreCase(cachTinh)
						? getSoNhanKhauTrongHo(maHo) * soTien
						: soTien;

				m.setSoTienCanNop(soTienCanNop);
				list.add(m);
			}
		}

		return list;
	}

	/**
	 * Lấy số nhân khẩu thuộc 1 hộ.
	 */
	private int getSoNhanKhauTrongHo(int maHo) throws ClassNotFoundException, SQLException {
		String sql = "SELECT COUNT(*) AS SoNguoi FROM quan_he WHERE MaHo = ?";
		try (Connection conn = MysqlConnection.getMysqlConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, maHo);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("SoNguoi");
				}
			}
		}
		return 0;
	}
}
