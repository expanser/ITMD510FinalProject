package controllers;

import java.io.IOException;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.LoginModel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// for login views
public class LoginController {

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Label lblError;

	private LoginModel model;

	public LoginController() {
		model = new LoginModel();
	}
	
	public void login() throws NoSuchAlgorithmException {
		
		lblError.setText("");
		String username = this.txtUsername.getText();
		String password = this.txtPassword.getText();

		// Validations
		if (username == null || username.trim().equals("")) {
			lblError.setText("Username Cannot be empty or spaces");
			return;
		}
		if (password == null || password.trim().equals("")) {
			lblError.setText("Password Cannot be empty or spaces");
			return;
		}
		if (username == null || username.trim().equals("") && (password == null || password.trim().equals(""))) {
			lblError.setText("User name / Password Cannot be empty or spaces");
			return;
		}

		// authentication check
		checkCredentials(username, password);

	}

	// login
	public void checkCredentials(String username, String password) throws NoSuchAlgorithmException {
		
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
		
		Boolean isValid = model.getCredentials(username, sb.toString());
		if (!isValid) {
			lblError.setText("User or password error!");
			return;
		}
		try {
			AnchorPane root;
			if (model.isAdmin() && isValid) {
				// If user is admin, inflate admin view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/AdminView.fxml"));
				Main.stage.setTitle("Admin View");

			} else {
				// If user is customer, inflate customer view
				int user_id = model.getId();
				ClientController.setUserid(user_id);
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
				Main.stage.setTitle("Client View");
			}

			Scene scene = new Scene(root);
			if (model.isAdmin() && isValid) {
				scene.getStylesheets().add(getClass().getResource("/css/AdminView.css").toExternalForm());
			} else {
				scene.getStylesheets().add(getClass().getResource("/css/ClientView.css").toExternalForm());
			}
			Main.stage.setScene(scene);
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}

	}
	
	// go to register view
	public void register() {
		AnchorPane root;
		try {
			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/RegisterView.fxml"));
			Main.stage.setTitle("Register View");
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/RegisterView.css").toExternalForm());
			Main.stage.setScene(scene);
		} catch (IOException e) {
			System.out.println("Error occured while inflating view: " + e);
		}

	}
	
}
