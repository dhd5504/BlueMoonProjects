package controller.khoanthu;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import models.KhoanThuModel;
import models.DotThuModel;
import services.KhoanThuService;
import services.DotThuService;

public class AddKhoanThu implements Initializable {
	@FXML private TextField tfTenKhoanThu;
	@FXML private TextField tfSoTien;
	@FXML private ComboBox<String> cbLoaiKhoanThu;
	@FXML private ComboBox<String> cbCachTinh;
	@FXML private TextField tfDonViTinh;
	@FXML private TextArea taMoTa;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> loaiList = FXCollections.observableArrayList("Bắt buộc", "Tự nguyện");
		cbLoaiKhoanThu.setItems(loaiList);
		cbLoaiKhoanThu.setValue("Bắt buộc");

		ObservableList<String> cachList = FXCollections.observableArrayList("Theo hộ", "Theo người", "Tự nguyện");
		cbCachTinh.setItems(cachList);
		cbCachTinh.setValue("Theo hộ");
	}

	@FXML
	private void addKhoanThu(ActionEvent event) {
		String ten       = tfTenKhoanThu.getText().trim();
		String soTienStr = tfSoTien.getText().trim();
		String loai      = cbLoaiKhoanThu.getValue();
		String cach      = cbCachTinh.getValue();
		String donVi     = tfDonViTinh.getText().trim();
		String moTa      = taMoTa.getText().trim();

		if (ten.isEmpty() || ten.length() > 50) {
			showAlert("Tên khoản thu phải từ 1 đến 50 ký tự!", Alert.AlertType.WARNING);
			return;
		}
		if (!Pattern.matches("^[0-9]+(\\.[0-9]+)?$", soTienStr)) {
			showAlert("Số tiền không hợp lệ!", Alert.AlertType.WARNING);
			return;
		}
		if (donVi.isEmpty()) {
			showAlert("Hãy nhập đơn vị tính!", Alert.AlertType.WARNING);
			return;
		}

		double soTien = Double.parseDouble(soTienStr);
		int loaiInt   = loai.equals("Bắt buộc") ? 1 : 0;
		String keyCach = cach.equals("Theo hộ")    ? "TheoHo"
				: cach.equals("Theo người") ? "TheoNguoi"
				: "TuNguyen";

		KhoanThuModel model = new KhoanThuModel();
		model.setTenKhoanThu(ten);
		model.setSoTien(soTien);
		model.setLoaiKhoanThu(loaiInt);
		model.setCachTinh(keyCach);
		model.setDonViTinh(donVi);
		model.setMoTa(moTa);

		try {
			// Thêm Khoản Thu
			new KhoanThuService().add(model);
			// Tạo đợt thu đầu tiên mặc định
			DotThuModel firstDot = new DotThuModel();
			firstDot.setTenDotThu("Đợt đầu: " + model.getTenKhoanThu());
			firstDot.setMaKhoanThu(model.getMaKhoanThu());
			java.util.Date today = new java.util.Date();
			firstDot.setNgayBatDau(today);
			firstDot.setNgayKetThuc(null);
			new DotThuService().addDotThu(firstDot);

			closeWindow();
		} catch (ClassNotFoundException | SQLException e) {
			showAlert("Lỗi khi thêm khoản thu hoặc đợt thu: " + e.getMessage(), Alert.AlertType.ERROR);
		}
	}

	private void closeWindow() {
		Stage stage = (Stage) tfTenKhoanThu.getScene().getWindow();
		stage.close();
	}

	private void showAlert(String msg, Alert.AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
