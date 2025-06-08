package models;

import java.sql.Date;

public class TamTruModel {
    private int id;
    private int idNhanKhau;
    private String diaChiTamTru;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String ghiChu;

    public TamTruModel() {}

    public TamTruModel(int id, int idNhanKhau, String diaChiTamTru, Date ngayBatDau, Date ngayKetThuc, String ghiChu) {
        this.id = id;
        this.idNhanKhau = idNhanKhau;
        this.diaChiTamTru = diaChiTamTru;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.ghiChu = ghiChu;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdNhanKhau() { return idNhanKhau; }
    public void setIdNhanKhau(int idNhanKhau) { this.idNhanKhau = idNhanKhau; }

    public String getDiaChiTamTru() { return diaChiTamTru; }
    public void setDiaChiTamTru(String diaChiTamTru) { this.diaChiTamTru = diaChiTamTru; }

    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
