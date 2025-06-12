package models;

public class UsersModel {
	private int id;
	private String username;
	private String passwd;
	private String vaiTro;  // ➕ THÊM: Vai trò người dùng (Admin, Ke toan, To truong)

	public UsersModel() {}

	public UsersModel(String username, String passwd) {
		this.username = username;
		this.passwd = passwd;
	}

	// Getter & Setter cho ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	// Getter & Setter cho Username
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	// Getter & Setter cho Password
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	// Getter & Setter cho Vai trò
	public String getVaiTro() {
		return vaiTro;
	}
	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}
}
