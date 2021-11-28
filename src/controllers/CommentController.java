package controllers;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import models.CommentModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

//for user comments view 
public class CommentController {
	
	static int user_id;
	
	static int is_admin = 0;
	
	NoticeListItemChangeListener commentsListener;

	@FXML
	private ListView<HBoxCell> commentsList;
	@FXML
	private Label lblTip;
	
	private CommentModel model;
	
	public CommentController() {
		model = new CommentModel();
		initPage();
	}
	
	public void initPage() {
		// Create a Runnable
		Runnable task = new Runnable()
		{
			public void run()
			{
				handleCommentsList();
				handleLblTitle();
			}
		};
		Thread backgroundThread = new Thread(task);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}
	
	public static void setUserId(int id) {
		user_id = id;
	}
	
	public static void setIsAdmin(int isAdmin) {
		is_admin = isAdmin;
	}
	
	// show the delete tip if it's admin
	public void handleLblTitle() {
		Platform.runLater(() -> {
			lblTip.setText(is_admin == 1 ? "Click to delete the review" : "");
		});
	}
	
	// get comments list
	public void handleCommentsList() {
		// remove old event listener if there is
		if (commentsListener != null) commentsList.getSelectionModel().selectedItemProperty().removeListener(commentsListener);
		ArrayList<ArrayList<Object>> data = model.getComments(user_id);
        List<HBoxCell> list = new ArrayList<>();
        
		Platform.runLater(() -> {	
	        for (int i = 0; i < data.size(); i++) {
	        	int id = (int)data.get(i).get(0);
	        	String title = (String)data.get(i).get(1);
	        	String content = (String)data.get(i).get(2);
	        	Date createTime = (Date)data.get(i).get(3);
	    		Format formatter = new SimpleDateFormat("HH:mm MM/dd/yyyy");
	    		list.add(new HBoxCell(id, title, content, formatter.format(createTime)));
	        }
			ObservableList<HBoxCell> strList = FXCollections.observableArrayList(list);
			commentsList.setItems(strList);
			// add event listener
			commentsListener = new NoticeListItemChangeListener();
			commentsList.getSelectionModel().selectedItemProperty().addListener(commentsListener);
		});
	}
	
	private class NoticeListItemChangeListener implements ChangeListener<Object> {

        public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
            ((HBoxCell) newValue).click();
        }
	}
	
    public class HBoxCell extends HBox {
        Label label = new Label();
        int id;
        // comment item
        HBoxCell(int commentId, String title, String content, String date) {
             super();
             id = commentId;
             label.setText("Review on " + title + ", at " + date + " says \r\n" + content);
             label.setMaxWidth(470);
             label.setWrapText(true);
             HBox.setHgrow(label, Priority.ALWAYS);
             this.getChildren().addAll(label);
        }
        
        //delete comment if the user is admin
        public void click() {
        	if (is_admin != 1) return;
        	// show confirm dialog
    		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete this review?", ButtonType.YES, ButtonType.NO);
    		alert.setTitle("Films/TV Series Archives");
    		alert.setHeaderText("Confirm");
    		alert.showAndWait();
    		if (alert.getResult() == ButtonType.YES) {
    			Boolean isDone = model.deleteReview(id);
    			if (isDone) initPage();// after delete, refresh the list
    		}
        }
   }

}
