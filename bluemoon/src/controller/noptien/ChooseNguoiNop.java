package controller.noptien;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

import models.NhanKhauModel;
import models.QuanHeModel;
import services.NhanKhauService;
import services.QuanHeService;

public class ChooseNguoiNop implements Initializable {
	@FXML private TableView<NhanKhauModel> tvNhanKhau;
	@FXML private TableColumn<NhanKhauModel, String> colMaNhanKhau;
	@FXML private TableColumn<NhanKhauModel, String> colTen;
	@FXML private TableColumn<NhanKhauModel, String> colTuoi;
	@FXML private TableColumn<NhanKhauModel, String> colCMND;
	@FXML private TableColumn<NhanKhauModel, String> colSDT;
	@FXML private TableColumn<NhanKhauModel, String> colMaHo;
	@FXML private TextField tfSearch;
	@FXML private ComboBox<String> cbChooseSearch;

	private NhanKhauModel nhanKhauChoose;
	private List<NhanKhauModel> listNhanKhau;
	private ObservableList<NhanKhauModel> listValueTableView;

	public NhanKhauModel getNhanKhauChoose() {
		return nhanKhauChoose;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			loadNhanKhau();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadNhanKhau() throws ClassNotFoundException, SQLException {
		listNhanKhau = new NhanKhauService().getListNhanKhau();
		listValueTableView = FXCollections.observableArrayList(listNhanKhau);

		// Map ID -> MaHo via QuanHe
		Map<Integer, Integer> mapIdToMaHo = new HashMap<>();
		for (QuanHeModel qh : new QuanHeService().getListQuanHe()) {
			mapIdToMaHo.put(qh.getIdThanhVien(), qh.getMaHo());
		}

		colMaNhanKhau.setCellValueFactory(new PropertyValueFactory<>("id"));
		colTen.setCellValueFactory(new PropertyValueFactory<>("ten"));
		colTuoi.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
		colCMND.setCellValueFactory(new PropertyValueFactory<>("cmnd"));
		colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
		colMaHo.setCellValueFactory(
				(CellDataFeatures<NhanKhauModel, String> p) ->
						new ReadOnlyStringWrapper(
								mapIdToMaHo.getOrDefault(p.getValue().getId(), 0).toString()
						)
		);

		tvNhanKhau.setItems(listValueTableView);

		ObservableList<String> options = FXCollections.observableArrayList("Tên", "Tuổi", "ID");
		cbChooseSearch.setItems(options);
		cbChooseSearch.setValue("Tên");
	}

	public void searchNhanKhau() {
		String key = tfSearch.getText().trim();
		String type = cbChooseSearch.getValue();

		if (key.isEmpty()) {
			showAlert("Hãy nhập thông tin tìm kiếm!", AlertType.WARNING);
			tvNhanKhau.setItems(listValueTableView);
			return;
		}

		List<NhanKhauModel> filtered = new ArrayList<>();
		switch (type) {
			case "Tên":
				for (NhanKhauModel nk : listNhanKhau) {
					if (nk.getTen().contains(key)) filtered.add(nk);
				}
				break;
			case "Tuổi":
				if (!Pattern.matches("\\d+", key)) {
					showAlert("Tuổi phải là số!", AlertType.WARNING);
					return;
				}
				int tuoi = Integer.parseInt(key);
				for (NhanKhauModel nk : listNhanKhau) {
					if (nk.getTuoi() == tuoi) filtered.add(nk);
				}
				break;
			case "ID":
				if (!Pattern.matches("\\d+", key)) {
					showAlert("ID phải là số!", AlertType.WARNING);
					return;
				}
				int id = Integer.parseInt(key);
				for (NhanKhauModel nk : listNhanKhau) {
					if (nk.getId() == id) filtered.add(nk);
				}
				break;
			default:
		}

		if (filtered.isEmpty()) {
			showAlert("Không tìm thấy thông tin!", AlertType.INFORMATION);
			tvNhanKhau.setItems(listValueTableView);
		} else {
			tvNhanKhau.setItems(FXCollections.observableArrayList(filtered));
		}
	}

	public void xacnhan(ActionEvent event) {
		nhanKhauChoose = tvNhanKhau.getSelectionModel().getSelectedItem();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}

	private void showAlert(String msg, AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
