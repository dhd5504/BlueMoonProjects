package services;

import models.ThongKeModel;
import models.KhoanThuModel;
import models.DotThuModel;
import services.KhoanThuService;
import services.DotThuService;
import services.MysqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service xử lý logic thống kê cho các khoản thu.
 */
public class ThongKeService {

    /**
     * Lấy danh sách tất cả các Khoản thu để thiết lập bộ lọc.
     */
    public List<KhoanThuModel> getListKhoanThu() throws ClassNotFoundException, SQLException {
        return new KhoanThuService().getListKhoanThu();
    }

    /**
     * Lấy danh sách tất cả các Đợt thu để thiết lập bộ lọc.
     */
    public List<DotThuModel> getListDotThu() throws ClassNotFoundException, SQLException {
        return new DotThuService().getAll();
    }

    /**
     * Đếm tổng số hộ gia đình.
     */
    public long countHo() throws ClassNotFoundException, SQLException {
        String sql = "SELECT COUNT(*) FROM ho_khau";
        try (Connection conn = MysqlConnection.getMysqlConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    /**
     * Đếm tổng số nhân khẩu.
     */
    public long countNhanKhau() throws ClassNotFoundException, SQLException {
        String sql = "SELECT COUNT(*) FROM nhan_khau";
        try (Connection conn = MysqlConnection.getMysqlConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    /**
     * Lấy danh sách thống kê tổng hợp cho mỗi Khoản thu,
     * có thể lọc theo Khoản thu, Đợt thu và Trạng thái.
     *
     * @param maKhoan   mã Khoản thu (null = tất cả)
     * @param maDotThu  mã Đợt thu (null = tất cả)
     * @param trangThai trạng thái ('Da dong','Chua dong',...) hoặc 'Tất cả'
     * @return danh sách ThongKeModel
     */
    public List<ThongKeModel> getThongKeTongHop(Integer maKhoan, Integer maDotThu, String trangThai)
            throws ClassNotFoundException, SQLException {
        List<ThongKeModel> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder()
                .append("SELECT kt.MaKhoanThu, kt.TenKhoanThu, kt.LoaiKhoanThu, ")
                .append("COUNT(DISTINCT CASE WHEN nt.TrangThai='Da dong' THEN nt.IDNhanKhau END) AS soHoDaNop, ")
                .append("COUNT(DISTINCT CASE WHEN nt.TrangThai!='Da dong' THEN nt.IDNhanKhau END) AS soHoChuaNop, ")
                .append("SUM(CASE WHEN nt.TrangThai='Da dong' THEN nt.SoTienDaNop ELSE 0 END) AS tongDaThu ")
                .append("FROM khoan_thu kt ")
                .append("LEFT JOIN nop_tien nt ON kt.MaKhoanThu = nt.MaKhoanThu ");

        boolean whereAdded = false;
        if (maKhoan != null) {
            sql.append("WHERE kt.MaKhoanThu = ? ");
            whereAdded = true;
        }
        if (maDotThu != null) {
            sql.append(whereAdded ? "AND " : "WHERE ")
                    .append("nt.MaDotThu = ? ");
            whereAdded = true;
        }
        if (trangThai != null && !"Tất cả".equals(trangThai)) {
            sql.append(whereAdded ? "AND " : "WHERE ")
                    .append("nt.TrangThai = ? ");
        }
        sql.append("GROUP BY kt.MaKhoanThu, kt.TenKhoanThu, kt.LoaiKhoanThu");

        try (Connection conn = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (maKhoan != null)  ps.setInt(idx++, maKhoan);
            if (maDotThu != null) ps.setInt(idx++, maDotThu);
            if (trangThai != null && !"Tất cả".equals(trangThai)) ps.setString(idx++, trangThai);

            try (ResultSet rs = ps.executeQuery()) {
                KhoanThuService ktService = new KhoanThuService();
                while (rs.next()) {
                    int ma = rs.getInt("MaKhoanThu");
                    String ten = rs.getString("TenKhoanThu");
                    String loai = "BatBuoc".equals(rs.getString("LoaiKhoanThu")) ? "Bắt buộc" : "Tự nguyện";
                    long da = rs.getLong("soHoDaNop");
                    long chua = rs.getLong("soHoChuaNop");
                    double thu = rs.getDouble("tongDaThu");

                    KhoanThuModel kt = ktService.findById(ma);
                    double soTien = kt.getSoTien();
                    long base = "TheoHo".equals(kt.getCachTinh()) ? countHo() : countNhanKhau();
                    double duKien = soTien * base;

                    list.add(new ThongKeModel(ma, ten, loai, da, chua, thu, duKien));
                }
            }
        }
        return list;
    }
}
