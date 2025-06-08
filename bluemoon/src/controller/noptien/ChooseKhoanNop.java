package controller.noptien;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import models.KhoanThuModel;
import services.KhoanThuService;

public class ChooseKhoanNop implements Initializable {
	@FXML private TableView<KhoanThuModel> tvKhoanPhi;
	@FXML private TableColumn<KhoanThuModel, String> colMaKhoanPhi;
	@FXML private TableColumn<KhoanThuModel, String> colTenKhoanThu;
	@FXML private TableColumn<KhoanThuModel, String> colSoTien;
	@FXML private TableColumn<KhoanThuModel, String> colLoaiKhoanThu;
	@FXML private TextField tfSearch;
	@FXML private ComboBox<String> cbChooseSearch;

	private KhoanThuModel khoanthuChoose;
	private List<KhoanThuModel> listKhoanThu;
	private ObservableList<KhoanThuModel> listValueTableView;

	public KhoanThuModel getKhoanthuChoose() {
		return khoanthuChoose;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			showKhoanThu();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showKhoanThu() throws ClassNotFoundException, SQLException {
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		listValueTableView = FXCollections.observableArrayList(listKhoanThu);

		colMaKhoanPhi.setCellValueFactory(new PropertyValueFactory<>("maKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<>("tenKhoanThu"));
		colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTien"));
		colLoaiKhoanThu.setCellValueFactory(
				(CellDataFeatures<KhoanThuModel, String> p) ->
						new ReadOnlyStringWrapper(
								p.getValue().getLoaiKhoanThu() == 1 ? "Bắt buộc" : "Tự nguyện"
						)
		);

		tvKhoanPhi.setItems(listValueTableView);

		ObservableList<String> options = FXCollections.observableArrayList("Tên khoản thu", "Mã khoản thu");
		cbChooseSearch.setItems(options);
		cbChooseSearch.setValue("Tên khoản thu");
	}

	public void searchKhoanThu() {
		String key = tfSearch.getText().trim();
		String type = cbChooseSearch.getValue();

		if (key.isEmpty()) {
			showAlert("Vui lòng nhập thông tin tìm kiếm!", AlertType.WARNING);
			tvKhoanPhi.setItems(listValueTableView);
			return;
		}

		List<KhoanThuModel> filtered = new ArrayList<>();
		switch (type) {
			case "Tên khoản thu":
				for (KhoanThuModel kt : listKhoanThu) {
					if (kt.getTenKhoanThu().contains(key)) filtered.add(kt);
				}
				break;
			case "Mã khoản thu":
				if (!Pattern.matches("\\d+", key)) {
					showAlert("Mã phải là số!", AlertType.WARNING);
					return;
				}
				for (KhoanThuModel kt : listKhoanThu) {
					if (kt.getMaKhoanThu() == Integer.parseInt(key)) filtered.add(kt);
				}
				break;
			default:
		}

		if (filtered.isEmpty()) {
			showAlert("Không tìm thấy thông tin!", AlertType.INFORMATION);
			tvKhoanPhi.setItems(listValueTableView);
		} else {
			tvKhoanPhi.setItems(FXCollections.observableArrayList(filtered));
		}
	}

	public void xacnhan(ActionEvent event) {
		khoanthuChoose = tvKhoanPhi.getSelectionModel().getSelectedItem();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}

	private void showAlert(String msg, AlertType type) {
		Alert alert = new Alert(type, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
