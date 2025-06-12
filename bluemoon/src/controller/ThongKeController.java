package controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.ThongKeModel;
import models.KhoanThuModel;
import models.DotThuModel;
import services.ThongKeService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller cho giao diện Thống kê - Tổng hợp
 */
public class ThongKeController implements Initializable {
	@FXML private ComboBox<KhoanThuModel> cbKhoanThu;
	@FXML private ComboBox<DotThuModel>    cbDotThu;
	@FXML private ComboBox<String>         cbTrangThai;

	@FXML private Label lblTongSoHo;
	@FXML private Label lblSoHoDaNop;
	@FXML private Label lblSoHoChuaNop;
	@FXML private Label lblTongTienDaThu;
	@FXML private Label lblTongTienDuKien;

	@FXML private TableView<ThongKeModel> tvThongKeTongHop;
	@FXML private TableColumn<ThongKeModel,String> colKhoanThu;
	@FXML private TableColumn<ThongKeModel,String> colLoaiKhoan;
	@FXML private TableColumn<ThongKeModel,String> colSoHoDaNopTong;
	@FXML private TableColumn<ThongKeModel,String> colSoHoChuaNopTong;
	@FXML private TableColumn<ThongKeModel,String> colTongDaThu;
	@FXML private TableColumn<ThongKeModel,String> colTongDuKien;

	private ThongKeService thongKeService = new ThongKeService();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// Khởi tạo bộ lọc trong combobox
			ObservableList<KhoanThuModel> khoanList = FXCollections.observableArrayList(
					thongKeService.getListKhoanThu()
			);
			cbKhoanThu.setItems(khoanList);
			cbKhoanThu.getItems().add(0, null);
			cbKhoanThu.setPromptText("Tất cả");

			ObservableList<DotThuModel> dotList = FXCollections.observableArrayList(
					thongKeService.getListDotThu()
			);
			cbDotThu.setItems(dotList);
			cbDotThu.getItems().add(0, null);
			cbDotThu.setPromptText("Tất cả");

			cbTrangThai.setItems(FXCollections.observableArrayList(
					"Tất cả", "Da dong", "Chua dong", "Dong mot phan"
			));
			cbTrangThai.getSelectionModel().selectFirst();

			// Thiết lập columns
			setupColumns();

			// Đọc dữ liệu lần đầu
			applyFilter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupColumns() {
		colKhoanThu.setCellValueFactory(data ->
				new ReadOnlyStringWrapper(data.getValue().getTenKhoanThu()));
		colLoaiKhoan.setCellValueFactory(data ->
				new ReadOnlyStringWrapper(data.getValue().getLoaiKhoan()));
		colSoHoDaNopTong.setCellValueFactory(data ->
				new ReadOnlyStringWrapper(String.valueOf(data.getValue().getSoHoDaNop())));
		colSoHoChuaNopTong.setCellValueFactory(data ->
				new ReadOnlyStringWrapper(String.valueOf(data.getValue().getSoHoChuaNop())));
		colTongDaThu.setCellValueFactory(data ->
				new ReadOnlyStringWrapper(String.format("%.0f", data.getValue().getTongDaThu())));
		colTongDuKien.setCellValueFactory(data ->
				new ReadOnlyStringWrapper(String.format("%.0f", data.getValue().getTongDuKien())));
	}

	@FXML
	private void loc() {
		applyFilter();
	}

	private void applyFilter() {
		try {
			Integer maDot = cbDotThu.getValue() != null ? cbDotThu.getValue().getMaDotThu() : null;
			Integer maKhoan = cbKhoanThu.getValue() != null ? cbKhoanThu.getValue().getMaKhoanThu() : null;
			String tt = cbTrangThai.getValue();

			// Lấy kết quả thống kê từ service
			List<ThongKeModel> data = thongKeService.getThongKeTongHop(maKhoan, maDot, tt);
			ObservableList<ThongKeModel> items = FXCollections.observableArrayList(data);
			tvThongKeTongHop.setItems(items);

			// Cập nhật summary
			long totalHo  = thongKeService.countHo();
			long daNop     = data.stream().mapToLong(ThongKeModel::getSoHoDaNop).sum();
			double tongThu= data.stream().mapToDouble(ThongKeModel::getTongDaThu).sum();
			double duKien = data.stream().mapToDouble(ThongKeModel::getTongDuKien).sum();

			lblTongSoHo.setText(String.valueOf(totalHo));
			lblSoHoDaNop.setText(String.valueOf(daNop));
			lblSoHoChuaNop.setText(String.valueOf(totalHo - daNop));
			lblTongTienDaThu.setText(String.format("%.0f", tongThu));
			lblTongTienDuKien.setText(String.format("%.0f", duKien));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
