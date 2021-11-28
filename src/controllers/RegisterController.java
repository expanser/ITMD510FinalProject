package controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.RegisterModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

// for register view
public class RegisterController {

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Label lblError;
	
	private RegisterModel model;
	
	public RegisterController() {
		model = new RegisterModel();
	}

	// go to login
	public void cancel() {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Login");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}
	
	// verify the data
	public void register() {
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
		

		handleRegister(username, password);
	}

	// register 
	public void handleRegister(String username, String password) {
		// user name check
		Boolean isOccupied = model.checkUserOccupied(username);
		if (isOccupied) {// if it's been used,stop register
			lblError.setText("username has been used!");
			return;
		}
		Boolean isDone = model.addUser(username, password);
		if (isDone) {// show success dialog
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Films/TV Series Archives");
			alert.setHeaderText("Register success!");
			alert.showAndWait();
			cancel();// go back to login
		}
	}

}
