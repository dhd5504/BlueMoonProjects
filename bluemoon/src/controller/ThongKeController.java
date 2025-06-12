package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import models.NopTienModel;
import services.NopTienService;
import services.QuanHeService;

public class ThongKeController implements Initializable {
	@FXML private Label lblTongTienDaThu;
	@FXML private Label lblSoHoDaNop;
	@FXML private TableView<NopTienModel> tvThongKe;
	@FXML private TableColumn<NopTienModel, String> colHoGiaDinh;
	@FXML private TableColumn<NopTienModel, String> colSoTienNop;
	@FXML private TableColumn<NopTienModel, String> colNgayNop;

	private QuanHeService quanHeService = new QuanHeService();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Load danh sách nộp tiền
		List<NopTienModel> list;
		try {
			list = new NopTienService().getListNopTien();
		} catch (ClassNotFoundException | SQLException ex) {
			showAlert("Lỗi tải dữ liệu nộp tiền: " + ex.getMessage());
			return;
		}

		// Cấu hình các cột
		colHoGiaDinh.setCellValueFactory(cell -> {
			int maHo;
			try {
				maHo = quanHeService.findHoByNhanKhau(cell.getValue().getIdNhanKhau());
			} catch (ClassNotFoundException | SQLException e) {
				maHo = -1;
			}
			return new ReadOnlyStringWrapper(maHo > 0 ? String.valueOf(maHo) : "?");
		});

		colSoTienNop.setCellValueFactory(cell ->
				new ReadOnlyStringWrapper(
						String.format("%,.0f", cell.getValue().getSoTienDaNop())
				)
		);

		colNgayNop.setCellValueFactory(cell -> {
			LocalDate d = new java.sql.Date(cell.getValue().getNgayThu().getTime()).toLocalDate();
			return new ReadOnlyStringWrapper(d.toString());
		});

		// Đưa dữ liệu lên TableView
		tvThongKe.setItems(FXCollections.observableArrayList(list));

		// Tính tổng tiền
		double total = list.stream()
				.mapToDouble(NopTienModel::getSoTienDaNop)
				.sum();
		lblTongTienDaThu.setText(String.format("%,.0f VNĐ", total));

		// Tính số hộ đã nộp
		Set<Integer> hoSet = new HashSet<>();
		for (NopTienModel nt : list) {
			try {
				int maHo = quanHeService.findHoByNhanKhau(nt.getIdNhanKhau());
				if (maHo > 0) hoSet.add(maHo);
			} catch (Exception e) {
				// bỏ qua lỗi
			}
		}
		lblSoHoDaNop.setText(String.valueOf(hoSet.size()));
	}

	@FXML private void loc() {
		// Có thể triển khai logic lọc tại đây nếu cần
	}

	private void showAlert(String msg) {
		Alert alert = new Alert(AlertType.ERROR, msg);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
