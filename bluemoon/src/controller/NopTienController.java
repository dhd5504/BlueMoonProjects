package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.KhoanThuModel;
import models.NhanKhauModel;
import models.NopTienModel;
import services.KhoanThuService;
import services.NhanKhauService;
import services.NopTienService;

public class NopTienController implements Initializable {
	@FXML private TableView<NopTienModel> tvNopTien;
	@FXML private TableColumn<NopTienModel, String> tbcTenNguoi;
	@FXML private TableColumn<NopTienModel, String> tbcTenKhoanThu;
	@FXML private TableColumn<NopTienModel, String> tbcNgayThu;
	@FXML private TableColumn<NopTienModel, String> tbcSoTienDaNop;
	@FXML private TableColumn<NopTienModel, String> tbcTrangThai;
	@FXML private ComboBox<String> cbChooseSearch;
	@FXML private TextField tfSearch;
	@FXML private TableColumn<NopTienModel, String> tbcSoTienCanNop;

	private ObservableList<NopTienModel> listValueTableView;
	private List<NopTienModel>       listNopTien;
	private List<NhanKhauModel>      listNhanKhau;
	private List<KhoanThuModel>      listKhoanThu;
	private Map<Integer,String>      mapIdToTen;
	private Map<Integer,String>      mapIdToTenKhoanThu;

	public void showNopTien() throws ClassNotFoundException, SQLException {
		// 1. Load data
		listNopTien = new NopTienService().getListNopTien();
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		listNhanKhau = new NhanKhauService().getListNhanKhau();
		listValueTableView = FXCollections.observableArrayList(listNopTien);

		tbcSoTienCanNop.setCellValueFactory(p ->
				new ReadOnlyStringWrapper(String.format("%.0f", p.getValue().getSoTienCanNop()))
		);
		// 2. Build maps
		mapIdToTen = new HashMap<>();
		for (NhanKhauModel nk : listNhanKhau) {
			mapIdToTen.put(nk.getId(), nk.getTen());
		}
		mapIdToTenKhoanThu = new HashMap<>();
		for (KhoanThuModel kt : listKhoanThu) {
			mapIdToTenKhoanThu.put(kt.getMaKhoanThu(), kt.getTenKhoanThu());
		}

		// 3. Set up columns
		tbcTenNguoi.setCellValueFactory((CellDataFeatures<NopTienModel, String> p) ->
				new ReadOnlyStringWrapper(
						mapIdToTen.get(p.getValue().getIdNhanKhau())    // <-- dùng getIdNhanKhau()
				)
		);

		tbcTenKhoanThu.setCellValueFactory((CellDataFeatures<NopTienModel, String> p) ->
				new ReadOnlyStringWrapper(
						mapIdToTenKhoanThu.get(p.getValue().getMaKhoanThu())
				)
		);

		tbcNgayThu.setCellValueFactory(new PropertyValueFactory<>("ngayThu"));
		tbcSoTienDaNop.setCellValueFactory(new PropertyValueFactory<>("soTienDaNop"));
		tbcTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

		tvNopTien.setItems(listValueTableView);

		// 4. Search combo
		ObservableList<String> options = FXCollections.observableArrayList(
				"Tên người nộp", "Tên khoản thu"
		);
		cbChooseSearch.setItems(options);
		cbChooseSearch.setValue("Tên người nộp");
	}

	public void searchNopTien() {
		String key  = tfSearch.getText().trim();
		String type = cbChooseSearch.getValue();

		if (key.isEmpty()) {
			showAlert("Hãy nhập thông tin tìm kiếm!", AlertType.WARNING);
			tvNopTien.setItems(listValueTableView);
			return;
		}

		List<NopTienModel> filtered = new ArrayList<>();
		switch (type) {
			case "Tên người nộp":
				for (NopTienModel nt : listNopTien) {
					String tenNguoi = mapIdToTen.get(nt.getIdNhanKhau());
					if (tenNguoi != null && tenNguoi.contains(key)) {
						filtered.add(nt);
					}
				}
				break;
			case "Tên khoản thu":
				for (NopTienModel nt : listNopTien) {
					String tenKt = mapIdToTenKhoanThu.get(nt.getMaKhoanThu());
					if (tenKt != null && tenKt.contains(key)) {
						filtered.add(nt);
					}
				}
				break;
		}

		if (filtered.isEmpty()) {
			showAlert("Không tìm thấy thông tin!", AlertType.INFORMATION);
			tvNopTien.setItems(listValueTableView);
		} else {
			tvNopTien.setItems(FXCollections.observableArrayList(filtered));
		}
	}

	public void addNopTien(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/views/noptien/AddNopTien.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 800, 600));
		stage.setResizable(false);
		stage.showAndWait();
		showNopTien();
	}

	public void delNopTien(ActionEvent event) throws ClassNotFoundException, SQLException {
		NopTienModel selected = tvNopTien.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Chọn khoản nộp để xóa!", AlertType.WARNING);
			return;
		}
		Alert cfm = new Alert(AlertType.CONFIRMATION,
				"Bạn có chắc chắn muốn xóa?",
				ButtonType.YES, ButtonType.NO);
		cfm.setHeaderText(null);
		Optional<ButtonType> res = cfm.showAndWait();
		if (res.isPresent() && res.get() == ButtonType.YES) {
			new NopTienService()
					.del(selected.getIdNopTien(), selected.getMaKhoanThu());
			showNopTien();
		}
	}

	public void updateNopTien(ActionEvent event) throws IOException {
		NopTienModel selected = tvNopTien.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Hãy chọn dòng muốn cập nhật!", AlertType.WARNING);
			return;
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/noptien/UpdateNopTien.fxml"));
		Parent root = loader.load();

		// Gửi dữ liệu sang controller cập nhật
		controller.noptien.UpdateNopTien controller = loader.getController();
		controller.setData(selected);  // truyền model được chọn

		Stage stage = new Stage();
		stage.setScene(new Scene(root, 800, 600));
		stage.setTitle("Cập nhật nộp tiền");
		stage.setResizable(false);
		stage.showAndWait();

		// Sau khi đóng form, load lại dữ liệu
		try {
			showNopTien();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			showAlert("Lỗi khi tải lại danh sách sau cập nhật!", Alert.AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			showNopTien();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showAlert(String msg, AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
