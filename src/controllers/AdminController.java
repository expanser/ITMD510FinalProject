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

public class AdminController {
	
	@FXML
	private Pane paneUserList;
	@FXML
	private Pane paneUserDetail;
	@FXML
	private Pane paneUpdateList;
	@FXML
	private Pane paneUpdateDetail;
	@FXML
	private Pane paneAddDetail;
	@FXML
	private Pane paneDeleteList;
	
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtRegion;

	// Declare DB objects
	DBConnect conn = null;
	Statement stmt = null;

	public AdminController() {
		conn = new DBConnect();
	}

	public void viewUsers() {

		paneUserList.setVisible(true);
		paneUpdateList.setVisible(false);
		paneDeleteList.setVisible(false);

	}

	public void updateRec() {

		paneUserList.setVisible(false);
		paneUpdateList.setVisible(true);
		paneDeleteList.setVisible(false);
	}

	public void addRec() {

		paneUserList.setVisible(false);
		paneUpdateList.setVisible(false);
		paneDeleteList.setVisible(true);

	}
	
	public void logout() {
		// System.exit(0);
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Login");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}

	public void submitBank() {

		// INSERT INTO BANK TABLE
		try {
			// Execute a query
			System.out.println("Inserting records into the table...");
			stmt = conn.getConnection().createStatement();
			String sql = null;

			// Include all object data to the database table

			sql = "insert into jpapa_bank(name,address) values ('" + txtName.getText() + "','" + txtAddress.getText()
					+ "')";
			stmt.executeUpdate(sql);
			System.out.println("Bank Record created");

			conn.getConnection().close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	public void submitUpdate() {

		System.out.println("Update Submit button pressed");

	}

	public void submitDelete() {

		System.out.println("Delete Submit button pressed");

	}

}
