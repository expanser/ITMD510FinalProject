package controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.RegisterModel;
import javafx.fxml.FXMLLoader;

public class MediaController {
	
	private static int media_id;

	@FXML
	private TextField txtUsername;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private Label lblError;
	
	private RegisterModel model;
	
	public MediaController() {
		model = new RegisterModel();
	}

	public static void setMediaId(int id) {
		media_id = id;
	}
	
	public void goBack() {
		// System.exit(0);
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);
			Main.stage.setTitle("Client View");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}
}
