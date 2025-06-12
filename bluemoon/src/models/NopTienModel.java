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
	private double soTienCanNop;

	// Thêm 2 thuộc tính phục vụ giao diện
	private String tenNguoiNop;
	private String tenKhoanThu;

	public NopTienModel() {}

	// Constructor đầy đủ
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

	// Getter/Setter IDNopTien
	public int getIdNopTien() {
		return idNopTien;
	}
	public void setIdNopTien(int idNopTien) {
		this.idNopTien = idNopTien;
	}

	// Getter/Setter IDNhanKhau
	public int getIdNhanKhau() {
		return idNhanKhau;
	}
	public void setIdNhanKhau(int idNhanKhau) {
		this.idNhanKhau = idNhanKhau;
	}

	// Getter/Setter MaKhoanThu
	public int getMaKhoanThu() {
		return maKhoanThu;
	}
	public void setMaKhoanThu(int maKhoanThu) {
		this.maKhoanThu = maKhoanThu;
	}

	// Getter/Setter MaDotThu
	public int getMaDotThu() {
		return maDotThu;
	}
	public void setMaDotThu(int maDotThu) {
		this.maDotThu = maDotThu;
	}

	// Getter/Setter NgayThu
	public Date getNgayThu() {
		return ngayThu;
	}
	public void setNgayThu(Date ngayThu) {
		this.ngayThu = ngayThu;
	}

	// Getter/Setter SoTienDaNop
	public double getSoTienDaNop() {
		return soTienDaNop;
	}
	public void setSoTienDaNop(double soTienDaNop) {
		this.soTienDaNop = soTienDaNop;
	}

	// Getter/Setter TrangThai
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	// Getter/Setter TenNguoiNop
	public String getTenNguoiNop() {
		return tenNguoiNop;
	}
	public void setTenNguoiNop(String tenNguoiNop) {
		this.tenNguoiNop = tenNguoiNop;
	}

	// Getter/Setter TenKhoanThu
	public String getTenKhoanThu() {
		return tenKhoanThu;
	}
	public void setTenKhoanThu(String tenKhoanThu) {
		this.tenKhoanThu = tenKhoanThu;
	}

	public double getSoTienCanNop() {
		return soTienCanNop;
	}
	public void setSoTienCanNop(double soTienCanNop) {
		this.soTienCanNop = soTienCanNop;
	}

}
