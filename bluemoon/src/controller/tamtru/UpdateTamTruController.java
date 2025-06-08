package controller.tamtru;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.TamTruModel;
import services.TamTruService;

import java.time.LocalDate;
import java.sql.Date;

public class UpdateTamTruController {

    @FXML private TextField tfDiaChi;
    @FXML private DatePicker dpNgayBatDau;
    @FXML private DatePicker dpNgayKetThuc;
    @FXML private TextArea taGhiChu;

    private TamTruModel currentTamTru;

    public void loadData(TamTruModel tamTru) {
        this.currentTamTru = tamTru;
        tfDiaChi.setText(tamTru.getDiaChiTamTru());
        dpNgayBatDau.setValue(tamTru.getNgayBatDau().toLocalDate());
        if (tamTru.getNgayKetThuc() != null) {
            dpNgayKetThuc.setValue(tamTru.getNgayKetThuc().toLocalDate());
        }
        taGhiChu.setText(tamTru.getGhiChu() != null ? tamTru.getGhiChu() : "");
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String diaChi = tfDiaChi.getText().trim();
        LocalDate ngayBatDau = dpNgayBatDau.getValue();
        LocalDate ngayKetThuc = dpNgayKetThuc.getValue();
        String ghiChu = taGhiChu.getText().trim();

        if (diaChi.isEmpty() || ngayBatDau == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập địa chỉ và ngày bắt đầu.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        currentTamTru.setDiaChiTamTru(diaChi);
        currentTamTru.setNgayBatDau(Date.valueOf(ngayBatDau));
        currentTamTru.setNgayKetThuc(ngayKetThuc != null ? Date.valueOf(ngayKetThuc) : null);
        currentTamTru.setGhiChu(ghiChu);

        boolean result = new TamTruService().update(currentTamTru);
        if (result) {
            new Alert(Alert.AlertType.INFORMATION, "Cập nhật thành công!").showAndWait();
            closeWindow(event);
        } else {
            new Alert(Alert.AlertType.ERROR, "Cập nhật thất bại!").showAndWait();
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
