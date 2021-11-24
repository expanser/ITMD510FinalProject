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

public class MediaController {
	
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
	
	public MediaController() {
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

		// Run the task in a background thread
		Thread backgroundThread = new Thread(task);
		// Terminate the running thread if the application exits
		backgroundThread.setDaemon(true);
		// Start the thread
		backgroundThread.start();
		
	}
	
	public void handleGetDetail() {
		ArrayList<Object> data = model.getMediaDetail(media_id);
		String genre = (String)data.get(1);
		String type = (String)data.get(3);
		String title = (String)data.get(4);
		String director = (String)data.get(5);
		Date releaseDate = (Date)data.get(6);
		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		Platform.runLater(() -> {
			lblTitle.setText(title);
			lblGenre.setText(genre);
			lblType.setText(type);
			lblDirector.setText(director);
			lblReleaseDate.setText(formatter.format(releaseDate));
		});
	}
	
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
        HBoxCell(String name, String content, String date) {
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
	
	public void postComment() {
		int user_id = ClientController.user_id;
		String txtComment = this.txtComment.getText();
		if (txtComment == null || txtComment.trim().equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Movie/TV series Archives");
			alert.setHeaderText("Review Cannot be empty or spaces!");
			alert.showAndWait();
		return;
		}
		Boolean isDone = model.postComment(user_id, media_id, txtComment);
		if (isDone) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Movie/TV series Archives");
			alert.setHeaderText("Post review success!");
			alert.showAndWait();
			this.txtComment.setText("");
			handleCommentsList();
		}
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
