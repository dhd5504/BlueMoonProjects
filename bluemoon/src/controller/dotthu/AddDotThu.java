package controller.dotthu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.DotThuModel;
import services.DotThuService;

public class AddDotThu {
    @FXML private TextField tfTenDot;
    @FXML private DatePicker dpNgayBD;
    @FXML private DatePicker dpNgayKT;

    // Mã khoản thu để gán vào dot thu
    private int maKhoanThu;

    /**
     * Thiết lập MaKhoanThu từ controller gọi
     */
    public void setMaKhoanThu(int maKhoanThu) {
        this.maKhoanThu = maKhoanThu;
    }

    public void saveDotThu(ActionEvent event) {
        try {
            String ten = tfTenDot.getText().trim();
            java.util.Date bd = java.sql.Date.valueOf(dpNgayBD.getValue());
            java.util.Date kt = null;
            if (dpNgayKT.getValue() != null) {
                kt = java.sql.Date.valueOf(dpNgayKT.getValue());
            }
            // Gán luôn MaKhoanThu
            DotThuModel dot = new DotThuModel(0, ten, maKhoanThu, bd, kt);
            new DotThuService().addDotThu(dot);
            // đóng dialog
            Stage s = (Stage) tfTenDot.getScene().getWindow();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
