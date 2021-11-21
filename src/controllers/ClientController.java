package controllers;

import java.sql.SQLException;
import java.sql.Statement;

import Dao.DBConnect;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class ClientController {

	private static int user_id;
	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public ClientController() {
		conn = new DBConnect();
	}
	
	public static void setUserid(int id) {
		user_id = id;
	}
	
	public void logout() {
		// System.exit(0);
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



}
