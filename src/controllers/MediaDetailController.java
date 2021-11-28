package controllers;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import models.MediaDetailModel;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// for media detail view
public class MediaDetailController {
	
	private static int media_id;
	
	@FXML
	private ListView<HBoxCell> commentsList;
	@FXML
	private TextArea txtComment;

	@FXML
	private Label lblTitle;

	@FXML
	private Label lblGenre;

	@FXML
	private Label lblType;
	
	@FXML
	private Label lblDirector;
	
	@FXML
	private Label lblReleaseDate;
	
	private MediaDetailModel model;
	
	public MediaDetailController() {
		model = new MediaDetailModel();
		// Create a Runnable
		Runnable task = new Runnable()
		{
			public void run()
			{
				handleGetDetail();
				handleCommentsList();
			}
		};
		Thread backgroundThread = new Thread(task);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}
	
	// get media detail
	public void handleGetDetail() {
		ArrayList<Object> data = model.getMediaDetail(media_id);
		String genre = (String)data.get(1);
		String type = (String)data.get(3);
		String title = (String)data.get(4);
		String director = (String)data.get(5);
		Date releaseDate = (Date)data.get(6);
		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		// put information into interface
		Platform.runLater(() -> {
			lblTitle.setText(title);
			lblGenre.setText(genre);
			lblType.setText(type);
			lblDirector.setText(director);
			lblReleaseDate.setText(formatter.format(releaseDate));
		});
	}
	
	//get comments list
	public void handleCommentsList() {
		ArrayList<ArrayList<Object>> data = model.getComments(media_id);
        List<HBoxCell> list = new ArrayList<>();
        
		Platform.runLater(() -> {
	        for (int i = 0; i < data.size(); i++) {
	        	String name = (String)data.get(i).get(0);
	        	String content = (String)data.get(i).get(1);
	        	Date createTime = (Date)data.get(i).get(2);
	    		Format formatter = new SimpleDateFormat("HH:mm MM/dd/yyyy");
	    		list.add(new HBoxCell(name, content, formatter.format(createTime)));
	        }
			ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
			commentsList.setItems(strList);
		});
	}
	
    public static class HBoxCell extends HBox {
        Label label = new Label();
        HBoxCell(String name, String content, String date) {// comment item
             super();
             label.setText(name + " at " + date + " says \r\n" + content);
             label.setMaxWidth(470);
             label.setWrapText(true);
             HBox.setHgrow(label, Priority.ALWAYS);
             this.getChildren().addAll(label);
        }
   }

	public static void setMediaId(int id) {
		media_id = id;
	}
	
	// post new comment
	public void postComment() {
		int user_id = ClientController.user_id;
		String txtComment = this.txtComment.getText();
		// if comment is empty,show alert
		if (txtComment == null || txtComment.trim().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Films/TV Series Archives");
			alert.setHeaderText("Review Cannot be empty or spaces!");
			alert.showAndWait();
			return;
		}
		Boolean isDone = model.postComment(user_id, media_id, txtComment);
		if (isDone) {
			// show success dialog
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Films/TV Series Archives");
			alert.setHeaderText("Post review success!");
			alert.showAndWait();
			this.txtComment.setText("");
			handleCommentsList();// after post, refresh comment list 
		}
	}
	
	// go to client view
	public void goBack() {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/ClientView.css").toExternalForm());
			Main.stage.setScene(scene);
			Main.stage.setTitle("Client View");
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}
	}
}
