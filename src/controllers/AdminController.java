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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import models.AdminModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

// for admin view
public class AdminController {
	
	@FXML
	private Pane paneUserList;
	@FXML
	private Pane paneUpdateList;
	@FXML
	private Pane paneDeleteList;
	@FXML
	private ListView<HBoxCell> userList;
	@FXML
	private ListView<HBoxCell> updateList;
	@FXML
	private ListView<HBoxCell> deleteList;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtUpdate;
	@FXML
	private TextField txtDelete;
	
	// add event listener after get data 
	NoticeListItemChangeListener userListener;
	
	NoticeListItemChangeListener updateListener;
	
	NoticeListItemChangeListener deleteListener;

	private AdminModel model;

	public AdminController() {
		model = new AdminModel();
	}

	public void viewUsers() {
		// show pane and clear input
		txtUsername.setText("");
		paneUserList.setVisible(true);
		paneUpdateList.setVisible(false);
		paneDeleteList.setVisible(false);
	}

	public void updateRec() {
		// show pane and clear input
		txtUpdate.setText("");
		paneUserList.setVisible(false);
		paneUpdateList.setVisible(true);
		paneDeleteList.setVisible(false);
	}

	public void addRec() {
		// go to additem
		try {
			MediaAddController.setMediaId(0);
			Stage stage = new Stage();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/AddItem.fxml"));
			stage.setTitle("AddMedia View");
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/AddItem.css").toExternalForm());
			stage.setScene(scene);
			MediaAddController.setStage(stage);
			stage.showAndWait();
		} catch (IOException e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}
	
	public void deleteRec() {
		// show pane and clear input
		txtDelete.setText("");
		paneUserList.setVisible(false);
		paneUpdateList.setVisible(false);
		paneDeleteList.setVisible(true);
	}
	
	public void searchUser() {
		// get user list
		if (userListener != null) userList.getSelectionModel().selectedItemProperty().removeListener(userListener);
		String keyword = this.txtUsername.getText();
		ArrayList<ArrayList<Object>> data = model.searchUser(keyword);

        List<HBoxCell> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
        	int id = (int)data.get(i).get(0);
        	String name = (String)data.get(i).get(1);
        	int isAdmin = (int)data.get(i).get(3);
    		list.add(new HBoxCell(name, id, isAdmin));
        }
		
		ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
		userList.setItems(strList);
		// add event listener
		userListener = new NoticeListItemChangeListener();
		userList.getSelectionModel().selectedItemProperty().addListener(userListener);
	}
	
	// event listener class 
	private class NoticeListItemChangeListener implements ChangeListener<Object> {

        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            ((HBoxCell) newValue).click();
        }
	}
	
    public class HBoxCell extends HBox {
        Label label = new Label();
        int id;
        // clickEvent: 0 for user detail, 1 for media update, 2 for media delete
        int clickEvent;
        String mediaTitle;
        
        //for user list
        HBoxCell(String name, int userId, int isAdmin) {
             super();
             id = userId;
             clickEvent = 0;
             String lblAdmin = "";
             if (isAdmin > 0) lblAdmin = "(admin)";//add admin tip if the user is
             label.setText(name.concat(lblAdmin));
             label.setMaxWidth(Double.MAX_VALUE);
             HBox.setHgrow(label, Priority.ALWAYS);
             this.getChildren().addAll(label);
        }
        
        //for media list
        HBoxCell(String title, int mediaId, boolean isUpdate) {
            super();
            id = mediaId;
            mediaTitle = title;
            clickEvent = isUpdate ? 1 : 2;
            label.setText(title);
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            this.getChildren().addAll(label);
        }
        
        // different event classify by clickEvent
        public void click() {
        	if (clickEvent == 0) {
        		handleGotoUser(id);
        	} else if (clickEvent == 1) {
        		handleUpdateMedia(id);
        	} else if (clickEvent == 2) {
        		handleDeleteMedia(mediaTitle, id);
        	}
        }
    }
    
    // go to user comment list
    public void handleGotoUser(int id){
		try {
			CommentController.setIsAdmin(1);// only admin can delete reviews
			CommentController.setUserId(id);
			Stage stage = new Stage();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/UserComments.fxml"));
			stage.setTitle("UserReviews View");
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/UserComments.css").toExternalForm());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			System.out.println("Error occured while inflating view: " + e);
		}
    }
    
    // go to update media view
    public void handleUpdateMedia(int id){
		try {
			MediaAddController.setMediaId(id);
			Stage stage = new Stage();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/AddItem.fxml"));
			stage.setTitle("UpdateMedia View");
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/AddItem.css").toExternalForm());
			stage.setScene(scene);
			MediaAddController.setStage(stage);
			stage.showAndWait();
		} catch (IOException e) {
			System.out.println("Error occured while inflating view: " + e);
		}
    }
    
    // delete media
    public void handleDeleteMedia(String title, int id) {
    	// show confirm dialog
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + title + " ?", ButtonType.YES, ButtonType.NO);
		alert.setTitle("Films/TV Series Archives");
		alert.setHeaderText("Confirm");
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			Boolean isDone = model.deleteMedia(id);
			if (isDone) searchDelete();//after delete,refresh the list
		}
    }
    
    // get media list for update
    public void searchUpdate() {
    	// remove old event listener if there is
		if (updateListener != null) updateList.getSelectionModel().selectedItemProperty().removeListener(updateListener);
		String keyword = this.txtUpdate.getText();
		ArrayList<ArrayList<Object>> data = model.searchMedia(keyword);

        List<HBoxCell> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
        	String title = (String)data.get(i).get(4);
        	int id = (int)data.get(i).get(0);
    		list.add(new HBoxCell(title, id, true));
        }

		ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
		updateList.setItems(strList);
		// add event listener
		updateListener = new NoticeListItemChangeListener();
		updateList.getSelectionModel().selectedItemProperty().addListener(updateListener);
    }
    
    // get media list for delete
    public void searchDelete() {
    	// remove old event listener if there is
		if (deleteListener != null) deleteList.getSelectionModel().selectedItemProperty().removeListener(deleteListener);
		String keyword = this.txtDelete.getText();
		ArrayList<ArrayList<Object>> data = model.searchMedia(keyword);

        List<HBoxCell> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
        	String title = (String)data.get(i).get(4);
        	int id = (int)data.get(i).get(0);
    		list.add(new HBoxCell(title, id, false));
        }
		
		ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
		deleteList.setItems(strList);
		// add event listener
		deleteListener = new NoticeListItemChangeListener();
		deleteList.getSelectionModel().selectedItemProperty().addListener(deleteListener);
    }
	
    //go to login view
	public void logout() {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/LoginView.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Films/TV Series Archives");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}

}
