package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.ClientModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ClientController {

	static int user_id;

	@FXML
	private ListView<HBoxCell> mediaList;
	@FXML
	private TextField txtKeyword;
	
	NoticeListItemChangeListener movieListener;

	private ClientModel model;

	public ClientController() {
		model = new ClientModel();
	}
	
	public static void setUserid(int id) {
		user_id = id;
	}
	
	public void search() {
		if (movieListener != null) mediaList.getSelectionModel().selectedItemProperty().removeListener(movieListener);
		String keyword = this.txtKeyword.getText();
		ArrayList<ArrayList<Object>> data = model.searchMedia(keyword);

        List<HBoxCell> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
        	String title = (String)data.get(i).get(4);
        	int id = (int)data.get(i).get(0);
    		list.add(new HBoxCell(title, id));
        }
		
		ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
		mediaList.setItems(strList);
		movieListener = new NoticeListItemChangeListener();
		mediaList.getSelectionModel().selectedItemProperty().addListener(movieListener);
	}
	
	private class NoticeListItemChangeListener implements ChangeListener<Object> {

        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            ((HBoxCell) newValue).click();
        }
	}
	
    public static class HBoxCell extends HBox {
        Label label = new Label();
        int id;
        HBoxCell(String labelText, int mediaId) {
             super();
             id = mediaId;
             label.setText(labelText);
             label.setMaxWidth(Double.MAX_VALUE);
             HBox.setHgrow(label, Priority.ALWAYS);
             this.getChildren().addAll(label);
        }
        
        public void click() {
        	MediaDetailController.setMediaId(id);
    		AnchorPane root;
    		try {
    			root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/mediaDetail.fxml"));
    			Main.stage.setTitle("Media View");
    			Scene scene = new Scene(root);
    			Main.stage.setScene(scene);
    		} catch (IOException e) {
    			System.out.println("Error occured while inflating view: " + e);
    		}
        }
   }
	
	public void viewComments() {
		try {
			CommentController.setUserId(user_id);
			Stage stage = new Stage();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/UserComments.fxml"));
			stage.setTitle("UserComments View");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			System.out.println("Error occured while inflating view: " + e);
		}

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
