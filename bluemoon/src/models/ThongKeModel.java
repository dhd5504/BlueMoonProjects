package models;

/**
 * Model biểu diễn kết quả thống kê tổng hợp cho mỗi khoản thu.
 */
public class ThongKeModel {
    private int maKhoanThu;
    private String tenKhoanThu;
    private String loaiKhoan;        // "Bắt buộc" hoặc "Tự nguyện"
    private long soHoDaNop;
    private long soHoChuaNop;
    private double tongDaThu;
    private double tongDuKien;

    public ThongKeModel() {
    }

    public ThongKeModel(int maKhoanThu, String tenKhoanThu, String loaiKhoan,
                        long soHoDaNop, long soHoChuaNop,
                        double tongDaThu, double tongDuKien) {
        this.maKhoanThu = maKhoanThu;
        this.tenKhoanThu = tenKhoanThu;
        this.loaiKhoan = loaiKhoan;
        this.soHoDaNop = soHoDaNop;
        this.soHoChuaNop = soHoChuaNop;
        this.tongDaThu = tongDaThu;
        this.tongDuKien = tongDuKien;
    }

    public int getMaKhoanThu() {
        return maKhoanThu;
    }

    public void setMaKhoanThu(int maKhoanThu) {
        this.maKhoanThu = maKhoanThu;
    }

    public String getTenKhoanThu() {
        return tenKhoanThu;
    }

    public void setTenKhoanThu(String tenKhoanThu) {
        this.tenKhoanThu = tenKhoanThu;
    }

    public String getLoaiKhoan() {
        return loaiKhoan;
    }

    public void setLoaiKhoan(String loaiKhoan) {
        this.loaiKhoan = loaiKhoan;
    }

    public long getSoHoDaNop() {
        return soHoDaNop;
    }

    public void setSoHoDaNop(long soHoDaNop) {
        this.soHoDaNop = soHoDaNop;
    }

    public long getSoHoChuaNop() {
        return soHoChuaNop;
    }

    public void setSoHoChuaNop(long soHoChuaNop) {
        this.soHoChuaNop = soHoChuaNop;
    }

    public double getTongDaThu() {
        return tongDaThu;
    }

    public void setTongDaThu(double tongDaThu) {
        this.tongDaThu = tongDaThu;
    }

    public double getTongDuKien() {
        return tongDuKien;
    }

    public void setTongDuKien(double tongDuKien) {
        this.tongDuKien = tongDuKien;
    }
}
