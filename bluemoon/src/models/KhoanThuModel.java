package models;

public class KhoanThuModel {
	private int maKhoanThu;
	private String tenKhoanThu;
	private double soTien;
	private int loaiKhoanThu;  // 1 = Bắt buộc, 0 = Tự nguyện
	private String cachTinh;
	private String donViTinh;
	private String moTa;
	private String ngayTao;

	public KhoanThuModel() {}

	public KhoanThuModel(int maKhoanThu,
						 String tenKhoanThu,
						 double soTien,
						 int loaiKhoanThu,
						 String cachTinh,
						 String donViTinh,
						 String moTa,
						 String ngayTao) {
		this.maKhoanThu  = maKhoanThu;
		this.tenKhoanThu = tenKhoanThu;
		this.soTien      = soTien;
		this.loaiKhoanThu= loaiKhoanThu;
		this.cachTinh    = cachTinh;
		this.donViTinh   = donViTinh;
		this.moTa        = moTa;
		this.ngayTao     = ngayTao;
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
	public double getSoTien() {
		return soTien;
	}
	public void setSoTien(double soTien) {
		this.soTien = soTien;
	}
	public int getLoaiKhoanThu() {
		return loaiKhoanThu;
	}
	public void setLoaiKhoanThu(int loaiKhoanThu) {
		this.loaiKhoanThu = loaiKhoanThu;
	}
	public String getCachTinh() {
		return cachTinh;
	}
	public void setCachTinh(String cachTinh) {
		this.cachTinh = cachTinh;
	}
	public String getDonViTinh() {
		return donViTinh;
	}
	public void setDonViTinh(String donViTinh) {
		this.donViTinh = donViTinh;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	public String getNgayTao() {
		return ngayTao;
	}
	public void setNgayTao(String ngayTao) {
		this.ngayTao = ngayTao;
	}

	@Override
	public String toString() {
		return String.format("%s [%,.0f]", tenKhoanThu, soTien);
	}
}
