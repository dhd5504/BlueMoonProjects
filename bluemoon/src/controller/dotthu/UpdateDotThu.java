package controller.dotthu;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.DotThuModel;
import services.DotThuService;

public class UpdateDotThu implements Initializable {

    @FXML private TextField tfTenDot;
    @FXML private DatePicker dpNgayBD;
    @FXML private DatePicker dpNgayKT;

    private DotThuModel dotThuModel;

    /**
     * Gọi từ controller cha để truyền model cần update
     */
    public void setDotThuModel(DotThuModel model) {
        this.dotThuModel = model;
        // Đổ dữ liệu lên form
        tfTenDot.setText(model.getTenDotThu());
        dpNgayBD.setValue(new java.sql.Date(model.getNgayBatDau().getTime()).toLocalDate());
        if (model.getNgayKetThuc() != null) {
            dpNgayKT.setValue(new java.sql.Date(model.getNgayKetThuc().getTime()).toLocalDate());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Không cần khởi tạo gì thêm, các control đã được bind qua FXML
    }

    @FXML
    public void updateDotThu(ActionEvent event) {
        try {
            // Validate tên đợt
            String ten = tfTenDot.getText().trim();
            if (ten.isEmpty() || ten.length() > 100) {
                showAlert("Tên đợt thu phải từ 1 đến 100 ký tự!", Alert.AlertType.WARNING);
                return;
            }

            // Lấy ngày bắt đầu
            LocalDate bdDate = dpNgayBD.getValue();
            if (bdDate == null) {
                showAlert("Hãy chọn Ngày Bắt Đầu!", Alert.AlertType.WARNING);
                return;
            }
            java.util.Date bd = java.sql.Date.valueOf(bdDate);

            // Lấy ngày kết thúc (có thể null)
            java.util.Date kt = null;
            LocalDate ktDate = dpNgayKT.getValue();
            if (ktDate != null) {
                kt = java.sql.Date.valueOf(ktDate);
                // Kiểm tra ngày kết thúc không trước ngày bắt đầu
                if (kt.before(bd)) {
                    showAlert("Ngày Kết Thúc phải sau hoặc bằng Ngày Bắt Đầu!", Alert.AlertType.WARNING);
                    return;
                }
            }

            // Cập nhật model và gọi service
            dotThuModel.setTenDotThu(ten);
            dotThuModel.setNgayBatDau(bd);
            dotThuModel.setNgayKetThuc(kt);

            new DotThuService().updateDotThu(dotThuModel);

            // Đóng dialog
            Stage stage = (Stage) tfTenDot.getScene().getWindow();
            stage.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert("Lỗi khi cập nhật đợt thu:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
