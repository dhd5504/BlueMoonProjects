package controller.noptien;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.DotThuModel;
import models.KhoanThuModel;
import models.NhanKhauModel;
import models.NopTienModel;
import services.DotThuService;
import services.NopTienService;

public class AddNopTien implements Initializable {
	@FXML private TextField tfTenKhoanThu;
	@FXML private TextField tfTenNguoiNop;
	@FXML private TextField tfSoTienDaNop;
	@FXML private ComboBox<String>   cbTrangThai;
	@FXML private ComboBox<DotThuModel> cbDotThu;

	private KhoanThuModel khoanThuModel;
	private NhanKhauModel  nhanKhauModel;
	private DotThuModel    dotThuModel;

	@Override
	public void initialize(java.net.URL url, ResourceBundle rb) {
		cbTrangThai.getItems().addAll("Da dong","Chua dong","Dong mot phan");
		cbTrangThai.setValue("Da dong");

		cbDotThu.setDisable(true);
		cbDotThu.setOnAction(evt -> dotThuModel = cbDotThu.getValue());
	}

	/** Mở dialog chọn Khoản Thu */
	public void chooseKhoanThu() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/views/noptien/ChooseKhoanNop.fxml")
		);
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 800, 600));
		stage.setResizable(false);
		stage.showAndWait();

		KhoanThuModel sel = loader.<controller.noptien.ChooseKhoanNop>getController()
				.getKhoanthuChoose();
		if (sel != null) {
			khoanThuModel = sel;
			tfTenKhoanThu.setText(khoanThuModel.getTenKhoanThu());

			// Load các đợt thu hiện có
			try {
				List<DotThuModel> ds = new DotThuService()
						.getDotThuByKhoan(khoanThuModel.getMaKhoanThu());
				cbDotThu.getItems().setAll(ds);
				cbDotThu.setDisable(ds.isEmpty());
				if (!ds.isEmpty()) {
					cbDotThu.getSelectionModel().selectFirst();
					dotThuModel = cbDotThu.getValue();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				showAlert("Lỗi khi tải danh sách đợt thu:\n" + ex.getMessage(),
						AlertType.ERROR);
			}
		}
	}

	/** Mở dialog chọn Người Nộp */
	public void chooseNguoiNop() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/views/noptien/ChooseNguoiNop.fxml")
		);
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 800, 600));
		stage.setResizable(false);
		stage.showAndWait();

		NhanKhauModel sel = loader.<controller.noptien.ChooseNguoiNop>getController()
				.getNhanKhauChoose();
		if (sel != null) {
			nhanKhauModel = sel;
			tfTenNguoiNop.setText(nhanKhauModel.getTen());
		}
	}

	/** Thực thi thêm Nộp Tiền */
	public void addNopTien(ActionEvent event) {
		if (khoanThuModel == null || nhanKhauModel == null || dotThuModel == null) {
			showAlert("Vui lòng chọn đủ: khoản thu, đợt thu và người nộp!", AlertType.WARNING);
			return;
		}
		String soTienStr = tfSoTienDaNop.getText().trim();
		if (!Pattern.matches("^[0-9]+(\\.[0-9]+)?$", soTienStr)) {
			showAlert("Số tiền không hợp lệ!", AlertType.WARNING);
			return;
		}
		double soTien = Double.parseDouble(soTienStr);
		String trangThai = cbTrangThai.getValue();

		try {
			List<NopTienModel> existing = new NopTienService().getListNopTien();
			for (NopTienModel m : existing) {
				if (m.getIdNhanKhau()    == nhanKhauModel.getId()
						&& m.getMaKhoanThu()    == khoanThuModel.getMaKhoanThu()
						&& m.getMaDotThu()      == dotThuModel.getMaDotThu()) {
					showAlert("Người này đã nộp khoản phí này trong đợt thu đã chọn!",
							AlertType.WARNING);
					return;
				}
			}

			NopTienModel newM = new NopTienModel();
			newM.setIdNhanKhau(nhanKhauModel.getId());
			newM.setMaKhoanThu(khoanThuModel.getMaKhoanThu());
			newM.setMaDotThu(dotThuModel.getMaDotThu());
			newM.setSoTienDaNop(soTien);
			newM.setTrangThai(trangThai);
			new NopTienService().add(newM);

			((Stage)((Node)event.getSource()).getScene().getWindow()).close();
		} catch (Exception ex) {
			ex.printStackTrace();
			showAlert("Lỗi khi lưu nộp tiền:\n" + ex.getMessage(), AlertType.ERROR);
		}
	}

	private void showAlert(String msg, AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
