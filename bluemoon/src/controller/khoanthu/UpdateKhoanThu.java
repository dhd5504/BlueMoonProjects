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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.DotThuModel;
import models.KhoanThuModel;
import services.DotThuService;
import services.KhoanThuService;

public class UpdateKhoanThu implements Initializable {
	@FXML private TextField tfTenKhoanThu;
	@FXML private TextField tfSoTien;
	@FXML private ComboBox<String> cbLoaiKhoanThu;
	@FXML private ComboBox<String> cbCachTinh;
	@FXML private TextField tfDonViTinh;
	@FXML private TextArea taMoTa;
	// Các control mới cho Đợt Thu
	@FXML private TextField tfTenDotThu;
	@FXML private DatePicker dpNgayBatDau;
	@FXML private DatePicker dpNgayKetThuc;

	private KhoanThuModel khoanThuModel;
	private DotThuModel dotThuModel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> loaiList = FXCollections.observableArrayList("Bắt buộc", "Tự nguyện");
		cbLoaiKhoanThu.setItems(loaiList);

		ObservableList<String> cachList = FXCollections.observableArrayList("Theo hộ", "Theo người", "Tự nguyện");
		cbCachTinh.setItems(cachList);
	}

	/**
	 * Gán model KhoanThu và load dữ liệu kèm đợt thu đầu tiên
	 */
	public void setKhoanThuModel(KhoanThuModel model) {
		this.khoanThuModel = model;
		tfTenKhoanThu.setText(model.getTenKhoanThu());
		tfSoTien.setText(String.valueOf(model.getSoTien()));
		cbLoaiKhoanThu.setValue(model.getLoaiKhoanThu() == 1 ? "Bắt buộc" : "Tự nguyện");
		String ct = model.getCachTinh();
		cbCachTinh.setValue(ct.equals("TheoHo") ? "Theo hộ"
				: ct.equals("TheoNguoi") ? "Theo người" : "Tự nguyện");
		tfDonViTinh.setText(model.getDonViTinh());
		taMoTa.setText(model.getMoTa());

		try {
			dotThuModel = new DotThuService()
					.getDotThuByKhoan(model.getMaKhoanThu())
					.stream().findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dotThuModel != null) {
			tfTenDotThu.setText(dotThuModel.getTenDotThu());
			dpNgayBatDau.setValue(new java.sql.Date(dotThuModel.getNgayBatDau().getTime()).toLocalDate());
			if (dotThuModel.getNgayKetThuc() != null) {
				dpNgayKetThuc.setValue(new java.sql.Date(dotThuModel.getNgayKetThuc().getTime()).toLocalDate());
			}
		}
	}

	@FXML
	public void updateKhoanThu(ActionEvent event) throws ClassNotFoundException, SQLException {
		// validate các trường KhoảnThu như trước
		String ten = tfTenKhoanThu.getText().trim();
		String soTienStr = tfSoTien.getText().trim();
		String loai = cbLoaiKhoanThu.getValue();
		String cach = cbCachTinh.getValue();
		String donVi = tfDonViTinh.getText().trim();
		String moTa = taMoTa.getText().trim();
		if (ten.isEmpty() || ten.length() > 50) { showAlert("Tên khoản thu phải từ 1 đến 50 ký tự!", AlertType.WARNING); return; }
		if (!Pattern.matches("^[0-9]+(\\.[0-9]+)?$", soTienStr)) { showAlert("Số tiền không hợp lệ!", AlertType.WARNING); return; }
		if (donVi.isEmpty()) { showAlert("Đơn vị tính không được để trống!", AlertType.WARNING); return; }

		// cập nhật model KhoanThu
		khoanThuModel.setTenKhoanThu(ten);
		khoanThuModel.setSoTien(Double.parseDouble(soTienStr));
		khoanThuModel.setLoaiKhoanThu(loai.equals("Bắt buộc") ? 1 : 0);
		khoanThuModel.setCachTinh(cach.equals("Theo hộ") ? "TheoHo" : cach.equals("Theo người") ? "TheoNguoi" : "TuNguyen");
		khoanThuModel.setDonViTinh(donVi);
		khoanThuModel.setMoTa(moTa);
		new KhoanThuService().update(khoanThuModel);

		// cập nhật đợt thu
		if (dotThuModel != null) {
			dotThuModel.setTenDotThu(tfTenDotThu.getText().trim());
			dotThuModel.setNgayBatDau(java.sql.Date.valueOf(dpNgayBatDau.getValue()));
			if (dpNgayKetThuc.getValue() != null) {
				dotThuModel.setNgayKetThuc(java.sql.Date.valueOf(dpNgayKetThuc.getValue()));
			}
			new DotThuService().updateDotThu(dotThuModel);
		}

		Stage stage = (Stage) tfTenKhoanThu.getScene().getWindow();
		stage.close();
	}

	private void showAlert(String msg, AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
