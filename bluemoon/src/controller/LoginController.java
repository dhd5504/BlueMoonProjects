package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.UsersModel;
import services.UsersService;

public class LoginController {

	@FXML
	private TextField tfUsername;
	@FXML
	private PasswordField tfPassword;

	public void Login(ActionEvent event) throws IOException {
		String username = tfUsername.getText().trim();
		String password = tfPassword.getText().trim();

		try {
			UsersModel user = new UsersService().login(username, password);
			if (user == null) {
				showAlert("Sai tên đăng nhập hoặc mật khẩu.");
				return;
			}

			String role = user.getVaiTro();
			String fxmlPath = switch (role) {
				case "Admin"     -> "/views/admin/HomeAdmin.fxml";
				case "Ke toan"   -> "/views/ketoan/HomeKeToan.fxml";
				case "To Truong" -> "/views/totruong/HomeToTruong.fxml";
				default          -> null;
			};

			if (fxmlPath == null) {
				showAlert("Không xác định vai trò người dùng.");
				return;
			}

			Parent home = FXMLLoader.load(getClass().getResource(fxmlPath));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(home, 800, 600));
			stage.setResizable(false);
			stage.show();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			showAlert("Lỗi kết nối CSDL: " + e.getMessage());
		}
	}

	private void showAlert(String msg) {
		Alert alert = new Alert(AlertType.WARNING, msg, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}
}
