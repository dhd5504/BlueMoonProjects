package controller.tamtru;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.TamTruModel;
import services.TamTruService;

import java.sql.Date;
import java.time.LocalDate;

public class AddTamTruController {

    @FXML
    private TextField tfDiaChi;

    @FXML
    private DatePicker dpNgayBatDau;

    @FXML
    private DatePicker dpNgayKetThuc;

    @FXML
    private TextArea taGhiChu;

    private int nhanKhauId;  // dùng 1 biến duy nhất

    public void setNhanKhauId(int nhanKhauId) {
        this.nhanKhauId = nhanKhauId;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String diaChi = tfDiaChi.getText().trim();
        LocalDate ngayBatDau = dpNgayBatDau.getValue();
        LocalDate ngayKetThuc = dpNgayKetThuc.getValue();
        String ghiChu = taGhiChu.getText().trim();

        if (diaChi.isEmpty() || ngayBatDau == null) {
            showAlert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ địa chỉ và ngày bắt đầu.");
            return;
        }

        // Tạo đối tượng model
        TamTruModel tamTru = new TamTruModel();
        tamTru.setIdNhanKhau(nhanKhauId);
        tamTru.setDiaChiTamTru(diaChi);
        tamTru.setNgayBatDau(Date.valueOf(ngayBatDau));
        tamTru.setNgayKetThuc(ngayKetThuc != null ? Date.valueOf(ngayKetThuc) : null);
        tamTru.setGhiChu(ghiChu);

        // Gọi service lưu
        boolean result = new TamTruService().add(tamTru);
        if (result) {
            showAlert(Alert.AlertType.INFORMATION, "Thêm thông tin tạm trú thành công!");
            closeWindow(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Thêm thông tin thất bại!");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
