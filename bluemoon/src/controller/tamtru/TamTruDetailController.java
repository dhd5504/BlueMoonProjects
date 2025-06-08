package controller.tamtru;

import controller.tamtru.UpdateTamTruController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.TamTruModel;
import services.TamTruService;

import java.io.IOException;
import java.util.Optional;

public class TamTruDetailController {

    @FXML
    private Label lbDiaChi;
    @FXML
    private Label lbNgayBatDau;
    @FXML
    private Label lbNgayKetThuc;
    @FXML
    private TextArea taGhiChu;

    private TamTruModel currentTamTru;
    private int currentNhanKhauId;

    public void loadData(int idNhanKhau) {
        this.currentNhanKhauId = idNhanKhau;

        TamTruService tamTruService = new TamTruService();
        TamTruModel tamTru = tamTruService.getByNhanKhauId(idNhanKhau);
        this.currentTamTru = tamTru;

        if (tamTru == null) {
            lbDiaChi.setText("Không có thông tin tạm trú.");
            lbNgayBatDau.setText("-");
            lbNgayKetThuc.setText("-");
            taGhiChu.setText("Không có ghi chú.");
        } else {
            lbDiaChi.setText(tamTru.getDiaChiTamTru());
            lbNgayBatDau.setText(tamTru.getNgayBatDau().toString());
            lbNgayKetThuc.setText(
                    tamTru.getNgayKetThuc() != null ? tamTru.getNgayKetThuc().toString() : "Chưa xác định"
            );
            taGhiChu.setText(tamTru.getGhiChu() != null ? tamTru.getGhiChu() : "");
        }
    }

    @FXML
    private void handleCapNhat(ActionEvent event) {
        if (currentTamTru == null) {
            // Hiển thị thông báo hỏi thêm mới
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Chưa có thông tin tạm trú. Bạn có muốn thêm mới?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                openAddTamTruForm(currentNhanKhauId);
            }
        } else {
            openUpdateTamTruForm(currentTamTru);
        }
    }

    private void openAddTamTruForm(int idNhanKhau) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tamtru/AddTamTru.fxml"));
            Parent root = loader.load();
            AddTamTruController controller = loader.getController();
            controller.setNhanKhauId(idNhanKhau);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Thêm thông tin tạm trú");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateTamTruForm(TamTruModel tamTru) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tamtru/UpdateTamTru.fxml"));
            Parent root = loader.load();
            UpdateTamTruController controller = loader.getController();
            controller.loadData(tamTru);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cập nhật thông tin tạm trú");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
