package controllers;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.CommentModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class CommentController {
	
	static int user_id;

	@FXML
	private ListView<HBoxCell> commentsList;
	
	private CommentModel model;
	
	public CommentController() {
		model = new CommentModel();
		// Create a Runnable
		Runnable task = new Runnable()
		{
			public void run()
			{
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
	
	public static void setUserId(int id) {
		user_id = id;
	}
	
	public void handleCommentsList() {
		ArrayList<ArrayList<Object>> data = model.getComments(user_id);
        List<HBoxCell> list = new ArrayList<>();
        
		Platform.runLater(() -> {
	        for (int i = 0; i < data.size(); i++) {
	        	String title = (String)data.get(i).get(0);
	        	String content = (String)data.get(i).get(1);
	        	Date createTime = (Date)data.get(i).get(2);
	    		Format formatter = new SimpleDateFormat("HH:mm MM/dd/yyyy");
	    		list.add(new HBoxCell(title, content, formatter.format(createTime)));
	        }
			ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
			commentsList.setItems(strList);
		});
	}
	
    public static class HBoxCell extends HBox {
        Label label = new Label();
        HBoxCell(String title, String content, String date) {
             super();
             label.setText("Review on " + title + ", at " + date + " says \r\n" + content);
             label.setMaxWidth(470);
             label.setWrapText(true);
             HBox.setHgrow(label, Priority.ALWAYS);
             this.getChildren().addAll(label);
        }
   }

}
