package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import controller.khoanthu.UpdateKhoanThu;
import models.KhoanThuModel;
import services.KhoanThuService;

public class KhoanThuController implements Initializable {
	@FXML private TableView<KhoanThuModel> tvKhoanPhi;
	@FXML private TableColumn<KhoanThuModel, String> colMaKhoanPhi;
	@FXML private TableColumn<KhoanThuModel, String> colTenKhoanThu;
	@FXML private TableColumn<KhoanThuModel, String> colSoTien;
	@FXML private TableColumn<KhoanThuModel, String> colLoaiKhoanThu;
	@FXML private TableColumn<KhoanThuModel, String> colCachTinh;
	@FXML private TableColumn<KhoanThuModel, String> colDonViTinh;
	@FXML private TableColumn<KhoanThuModel, String> colMoTa;
	@FXML private TableColumn<KhoanThuModel, String> colNgayTao;

	@FXML private TextField tfSearch;
	@FXML private ComboBox<String> cbChooseSearch;

	private List<KhoanThuModel> listKhoanThu;
	private ObservableList<KhoanThuModel> listValueTableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			showKhoanThu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showKhoanThu() throws ClassNotFoundException, SQLException {
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		listValueTableView = FXCollections.observableArrayList(listKhoanThu);

		colMaKhoanPhi.setCellValueFactory(new PropertyValueFactory<>("maKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<>("tenKhoanThu"));
		colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTien"));

		Map<Integer, String> mapLoai = Map.of(1, "Bắt buộc", 0, "Tự nguyện");
		colLoaiKhoanThu.setCellValueFactory((CellDataFeatures<KhoanThuModel, String> p) ->
				new ReadOnlyStringWrapper(mapLoai.getOrDefault(p.getValue().getLoaiKhoanThu(), "")));

		colCachTinh.setCellValueFactory(new PropertyValueFactory<>("cachTinh"));
		colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
		colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
		colNgayTao.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

		tvKhoanPhi.setItems(listValueTableView);

		ObservableList<String> options = FXCollections.observableArrayList(
				"Tên khoản thu", "Mã khoản thu", "Loại", "Cách tính"
		);
		cbChooseSearch.setItems(options);
		cbChooseSearch.setValue("Tên khoản thu");
	}

	public void searchKhoanThu() {
		String key = tfSearch.getText().trim();
		String type = cbChooseSearch.getValue();
		if (key.isEmpty()) {
			showAlert("Hãy nhập thông tin tìm kiếm!", AlertType.WARNING);
			tvKhoanPhi.setItems(listValueTableView);
			return;
		}

		List<KhoanThuModel> filtered = new ArrayList<>();
		switch (type) {
			case "Tên khoản thu":
				for (KhoanThuModel k : listKhoanThu)
					if (k.getTenKhoanThu().toLowerCase().contains(key.toLowerCase()))
						filtered.add(k);
				break;
			case "Mã khoản thu":
				if (!key.matches("\\d+")) {
					showAlert("Bạn phải nhập số!", AlertType.WARNING);
					return;
				}
				int id = Integer.parseInt(key);
				for (KhoanThuModel k : listKhoanThu)
					if (k.getMaKhoanThu() == id)
						filtered.add(k);
				break;
			case "Loại":
				for (KhoanThuModel k : listKhoanThu) {
					if ((k.getLoaiKhoanThu() == 1 && "bắt buộc".equalsIgnoreCase(key)) ||
							(k.getLoaiKhoanThu() == 0 && "tự nguyện".equalsIgnoreCase(key))) {
						filtered.add(k);
					}
				}
				break;
			case "Cách tính":
				for (KhoanThuModel k : listKhoanThu)
					if (k.getCachTinh().equalsIgnoreCase(key))
						filtered.add(k);
				break;
		}

		if (filtered.isEmpty()) {
			showAlert("Không tìm thấy kết quả!", AlertType.INFORMATION);
			tvKhoanPhi.setItems(listValueTableView);
		} else {
			tvKhoanPhi.setItems(FXCollections.observableArrayList(filtered));
		}
	}

	public void addKhoanThu() throws IOException, ClassNotFoundException, SQLException {
		Parent root = FXMLLoader.load(getClass().getResource("/views/khoanthu/AddKhoanThu.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 800, 600));
		stage.setResizable(false);
		stage.showAndWait();
		showKhoanThu();
	}

	public void delKhoanThu() throws Exception {
		KhoanThuModel selected = tvKhoanPhi.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Chọn khoản thu để xóa!", AlertType.WARNING);
			return;
		}
		Alert cfm = new Alert(AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa?", ButtonType.YES, ButtonType.NO);
		cfm.setHeaderText(null);
		Optional<ButtonType> res = cfm.showAndWait();
		if (res.isPresent() && res.get() == ButtonType.YES) {
			new KhoanThuService().del(selected.getMaKhoanThu());
			showKhoanThu();
		}
	}

	public void updateKhoanThu() throws Exception {
		KhoanThuModel selected = tvKhoanPhi.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Chọn khoản thu để sửa!", AlertType.WARNING);
			return;
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/khoanthu/UpdateKhoanThu.fxml"));
		Parent root = loader.load();
		UpdateKhoanThu controller = loader.getController();
		controller.setKhoanThuModel(selected);
		Stage stage = new Stage();
		stage.setScene(new Scene(root, 800, 600));
		stage.setResizable(false);
		stage.showAndWait();
		showKhoanThu();
	}

	private void showAlert(String msg, AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
