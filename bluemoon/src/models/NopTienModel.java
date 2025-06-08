package models;

import java.util.Date;

public class NopTienModel {
	private int idNopTien;
	private int idNhanKhau;       // mã người nộp
	private int maKhoanThu;
	private int maDotThu;
	private Date ngayThu;
	private double soTienDaNop;
	private String trangThai;

	public NopTienModel() {}

	// Constructor đầy đủ, có idNhanKhau
	public NopTienModel(int idNopTien,
						int idNhanKhau,
						int maKhoanThu,
						int maDotThu,
						Date ngayThu,
						double soTienDaNop,
						String trangThai) {
		this.idNopTien   = idNopTien;
		this.idNhanKhau  = idNhanKhau;
		this.maKhoanThu  = maKhoanThu;
		this.maDotThu    = maDotThu;
		this.ngayThu     = ngayThu;
		this.soTienDaNop = soTienDaNop;
		this.trangThai   = trangThai;
	}

	// getter / setter IDNopTien
	public int getIdNopTien() {
		return idNopTien;
	}
	public void setIdNopTien(int idNopTien) {
		this.idNopTien = idNopTien;
	}

	// getter / setter IDNhanKhau
	public int getIdNhanKhau() {
		return idNhanKhau;
	}
	public void setIdNhanKhau(int idNhanKhau) {
		this.idNhanKhau = idNhanKhau;
	}

	// getter / setter MaKhoanThu
	public int getMaKhoanThu() {
		return maKhoanThu;
	}
	public void setMaKhoanThu(int maKhoanThu) {
		this.maKhoanThu = maKhoanThu;
	}

	// getter / setter MaDotThu
	public int getMaDotThu() {
		return maDotThu;
	}
	public void setMaDotThu(int maDotThu) {
		this.maDotThu = maDotThu;
	}

	// getter / setter NgayThu
	public Date getNgayThu() {
		return ngayThu;
	}
	public void setNgayThu(Date ngayThu) {
		this.ngayThu = ngayThu;
	}

	// getter / setter SoTienDaNop
	public double getSoTienDaNop() {
		return soTienDaNop;
	}
	public void setSoTienDaNop(double soTienDaNop) {
		this.soTienDaNop = soTienDaNop;
	}

	// getter / setter TrangThai
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
}
