package models;

import java.util.Date;

public class DotThuModel {
    private int maDotThu;
    private String tenDotThu;
    private int maKhoanThu; // liên kết tới Khoản Thu
    private Date ngayBatDau;
    private Date ngayKetThuc;

    public DotThuModel() {}

    public DotThuModel(int maDotThu, String tenDotThu, int maKhoanThu, Date ngayBatDau, Date ngayKetThuc) {
        this.maDotThu    = maDotThu;
        this.tenDotThu   = tenDotThu;
        this.maKhoanThu  = maKhoanThu;
        this.ngayBatDau  = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public int getMaDotThu() {
        return maDotThu;
    }
    public void setMaDotThu(int maDotThu) {
        this.maDotThu = maDotThu;
    }

    public int getMaKhoanThu() {
        return maKhoanThu;
    }
    public void setMaKhoanThu(int maKhoanThu) {
        this.maKhoanThu = maKhoanThu;
    }

    public String getTenDotThu() {
        return tenDotThu;
    }
    public void setTenDotThu(String tenDotThu) {
        this.tenDotThu = tenDotThu;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }
    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }
    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    @Override
    public String toString() {
        // hiển thị trong ComboBox
        return String.format("%s (%tF → %tF)", tenDotThu, ngayBatDau, ngayKetThuc);
    }
}
