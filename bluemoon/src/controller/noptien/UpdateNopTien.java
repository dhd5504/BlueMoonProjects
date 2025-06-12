package controller.noptien;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.NopTienModel;
import models.KhoanThuModel;
import services.NopTienService;
import services.KhoanThuService;
import services.QuanHeService;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateNopTien implements Initializable {

    @FXML private Label lblTenNguoi;
    @FXML private Label lblTenKhoanThu;
    @FXML private DatePicker dpNgayThu;
    @FXML private TextField tfSoTien;
    @FXML private ComboBox<String> cbTrangThai;
    @FXML private Label lblSoTienCanNop;

    private NopTienModel currentModel;
    private double soTienCanNop;

    public void setData(NopTienModel model) {
        this.currentModel = model;

        lblTenNguoi.setText(model.getTenNguoiNop());
        lblTenKhoanThu.setText(model.getTenKhoanThu());

        dpNgayThu.setValue(
                new java.sql.Date(model.getNgayThu().getTime()).toLocalDate()
        );
        tfSoTien.setText(String.valueOf(model.getSoTienDaNop()));
        cbTrangThai.setValue(model.getTrangThai());

        try {
            KhoanThuModel kt = new KhoanThuService().findById(model.getMaKhoanThu());
            double soTien = kt.getSoTien();
            String cachTinh = kt.getCachTinh();

            QuanHeService qhService = new QuanHeService();
            int maHo = qhService.findHoByNhanKhau(model.getIdNhanKhau());
            int soNguoi = qhService.countNhanKhauInHo(maHo);

            soTienCanNop = "TheoNguoi".equalsIgnoreCase(cachTinh) ? soTien * soNguoi : soTien;
            lblSoTienCanNop.setText(String.format("%.0f VNĐ", soTienCanNop));
        } catch (Exception e) {
            lblSoTienCanNop.setText("Lỗi dữ liệu");
        }
    }

    @FXML
    public void handleUpdate() {
        try {
            double soTien = Double.parseDouble(tfSoTien.getText());
            LocalDate ngay = dpNgayThu.getValue();
            String trangThai = cbTrangThai.getValue();

            if (soTien > soTienCanNop) {
                showAlert("Số tiền đã nộp vượt quá mức cần nộp!", Alert.AlertType.ERROR);
                return;
            }

            currentModel.setSoTienDaNop(soTien);
            currentModel.setNgayThu(java.sql.Date.valueOf(ngay));
            currentModel.setTrangThai(trangThai);

            new NopTienService().update(currentModel);
            closeStage();
        } catch (Exception e) {
            showAlert("Lỗi cập nhật: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleCancel() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) lblTenNguoi.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbTrangThai.getItems().addAll("Da dong", "Dong mot phan", "Chua dong");
    }
}
