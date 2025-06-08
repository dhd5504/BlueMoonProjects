package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.DotThuModel;
import models.KhoanThuModel;
import models.NopTienModel;
import services.DotThuService;
import services.KhoanThuService;
import services.NopTienService;

public class ThongKeController implements Initializable {
	@FXML private ComboBox<KhoanThuModel> cbKhoanThu;
	@FXML private ComboBox<DotThuModel>   cbDotThu;
	@FXML private TableView<KhoanThuModel> tvThongKe;
	@FXML private TableColumn<KhoanThuModel, String> colTenPhi;
	@FXML private TableColumn<KhoanThuModel, String> colTongSoTien;

	private List<KhoanThuModel> listKhoanThu;
	private List<NopTienModel>  listNopTien;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			listKhoanThu = new KhoanThuService().getListKhoanThu();
			listNopTien  = new NopTienService().getListNopTien();
		} catch (SQLException | ClassNotFoundException e) {
			showAlert("Lỗi khởi tạo dữ liệu thống kê:\n" + e.getMessage());
			return;
		}

		// Thiết lập ComboBox
		cbKhoanThu.setItems(FXCollections.observableArrayList(listKhoanThu));
		loadDotThu(null); // no filter dt

		// Cột Tên phí
		colTenPhi.setCellValueFactory(cell ->
				new ReadOnlyStringWrapper(cell.getValue().getTenKhoanThu())
		);

		// Cột Tổng số tiền
		colTongSoTien.setCellValueFactory(cell ->
				new ReadOnlyStringWrapper(
						String.format("%,.0f", computeSum(cell.getValue(), cbDotThu.getValue()))
				)
		);

		// Hiển thị tất cả ban đầu
		reloadTable();
	}

	/** Called when nhấn "Lọc" */
	@FXML
	private void loc(ActionEvent event) {
		// Nạp lại dữ liệu nộp tiền mới nhất
		try {
			listNopTien = new NopTienService().getListNopTien();
		} catch (Exception e) {
			showAlert("Lỗi tải dữ liệu mới:\n" + e.getMessage());
			return;
		}
		// Nếu chọn khoản thu, load đợt thu tương ứng
		KhoanThuModel selKt = cbKhoanThu.getValue();
		if (selKt != null) {
			loadDotThu(selKt.getMaKhoanThu());
		} else {
			loadDotThu(null);
		}
		// Lọc và hiển thị
		reloadTable();
	}

	/** Load đợt thu; nếu maKhoanThu==null thì clear cbDotThu và giữ null */
	private void loadDotThu(Integer maKhoanThu) {
		try {
			if (maKhoanThu != null) {
				List<DotThuModel> ds = new DotThuService().getDotThuByKhoan(maKhoanThu);
				cbDotThu.setItems(FXCollections.observableArrayList(ds));
				if (!ds.isEmpty()) cbDotThu.getSelectionModel().selectFirst();
			} else {
				cbDotThu.getItems().clear();
			}
		} catch (Exception e) {
			showAlert("Lỗi tải đợt thu:\n" + e.getMessage());
		}
	}

	/** Tính tổng tiền đã nộp */
	private double computeSum(KhoanThuModel kt, DotThuModel dt) {
		return listNopTien.stream()
				.filter(n -> n.getMaKhoanThu() == kt.getMaKhoanThu()
						&& (dt == null || n.getMaDotThu() == dt.getMaDotThu()))
				.mapToDouble(NopTienModel::getSoTienDaNop)
				.sum();
	}

	/** Reload table: nếu có chọn khoản thu thì chỉ show row đó, ngược lại show tất cả */
	private void reloadTable() {
		List<KhoanThuModel> rows = new ArrayList<>();
		KhoanThuModel selKt = cbKhoanThu.getValue();
		if (selKt != null) {
			rows.add(selKt);
		} else {
			rows.addAll(listKhoanThu);
		}
		tvThongKe.setItems(FXCollections.observableArrayList(rows));
		tvThongKe.refresh();
	}

	private void showAlert(String msg) {
		Alert alert = new Alert(Alert.AlertType.ERROR, msg);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
