package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import models.DotThuModel;
import models.HoKhauModel;
import models.KhoanThuModel;
import services.DotThuService;
import services.HoKhauService;
import services.KhoanThuService;

public class MainController implements Initializable {
	@FXML private Label lbSoHoKhau;
	@FXML private Label lbSoKhoanThu;
	@FXML private Label lbSoDotThu;
	@FXML private Label lbTongSoTienThu;

	@FXML private AreaChart<String, Number> chartDoanhThuTheoThang;
	@FXML private PieChart chartTiLeKhoanThu;
	@FXML private TableView<DotThuModel> tableDotThuSapHetHan;
	@FXML private TableColumn<DotThuModel, String> colTenDot;
	@FXML private TableColumn<DotThuModel, LocalDate> colNgayBD, colNgayKT;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			// -- Tính tổng số hộ khẩu
			List<HoKhauModel> listHoKhau = new HoKhauService().getListHoKhau();
			lbSoHoKhau.setText(String.valueOf(listHoKhau.size()));

			// -- Tính tổng số khoản thu
			List<KhoanThuModel> listKhoanThu = new KhoanThuService().getListKhoanThu();
			lbSoKhoanThu.setText(String.valueOf(listKhoanThu.size()));

			// -- Tính tổng số đợt thu
			List<DotThuModel> listDotThu = new DotThuService().getAll();
			lbSoDotThu.setText(String.valueOf(listDotThu.size()));

			// -- Tính tổng tiền thu
			double total = 0;
			for (DotThuModel dt : listDotThu) {
				KhoanThuModel kt = new KhoanThuService().findById(dt.getMaKhoanThu());
				if (kt != null) total += kt.getSoTien();
			}
			lbTongSoTienThu.setText(String.format("%.0f", total));

			// -- Tính doanh thu theo tháng cho AreaChart
			Map<String, Double> doanhThuTheoThang = listDotThu.stream()
					.collect(Collectors.groupingBy(
							dt -> {
								LocalDate bd = new java.sql.Date(dt.getNgayBatDau().getTime()).toLocalDate();
								return String.valueOf(bd.getMonthValue());
							},
							Collectors.summingDouble(dt -> {
								try {
									return new KhoanThuService().findById(dt.getMaKhoanThu()).getSoTien();
								} catch (Exception e) {
									return 0;
								}
							})
					));

			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.setName("Doanh thu");
			doanhThuTheoThang.forEach((month, amt) -> {
				XYChart.Data<String, Number> data = new XYChart.Data<>(month, amt);
				series.getData().add(data);
			});
			chartDoanhThuTheoThang.getData().setAll(series);

			for (XYChart.Data<String, Number> d : series.getData()) {
				Tooltip.install(d.getNode(), new Tooltip(d.getXValue() + ": " + d.getYValue()));
			}

			// -- Tỉ lệ khoản thu cho PieChart
			long batBuoc = listKhoanThu.stream().filter(kt -> kt.getLoaiKhoanThu() == 1).count();
			long tuNguyen = listKhoanThu.size() - batBuoc;
			chartTiLeKhoanThu.getData().setAll(
					new PieChart.Data("Bắt buộc", batBuoc),
					new PieChart.Data("Tự nguyện", tuNguyen)
			);

			// -- Đợt thu sắp đến hạn cho TableView
			colTenDot.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTenDotThu()));
			colNgayBD.setCellValueFactory(c -> {
				LocalDate d = new java.sql.Date(c.getValue().getNgayBatDau().getTime()).toLocalDate();
				return new SimpleObjectProperty<>(d);
			});
			colNgayKT.setCellValueFactory(c -> {
				java.util.Date dt = c.getValue().getNgayKetThuc();
				LocalDate d = (dt != null) ? new java.sql.Date(dt.getTime()).toLocalDate() : null;
				return new SimpleObjectProperty<>(d);
			});

			LocalDate today = LocalDate.now();
			List<DotThuModel> sapHetHan = listDotThu.stream()
					.filter(dt -> dt.getNgayKetThuc() != null)
					.filter(dt -> {
						LocalDate kt = new java.sql.Date(dt.getNgayKetThuc().getTime()).toLocalDate();
						return !kt.isBefore(today) && !kt.isAfter(today.plusDays(7));
					})
					.collect(Collectors.toList());
			tableDotThuSapHetHan.getItems().setAll(sapHetHan);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	// Thêm xử lý nút Đăng xuất
	@FXML
	private void handleLogout(ActionEvent event) throws IOException {
		Parent login = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(login, 800, 600));
		stage.show();
	}
}
