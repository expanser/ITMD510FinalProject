package controllers;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import models.MediaAddModel;

// for add item view
public class MediaAddController {
	
	static int media_id = 0;
	
	static Stage current_stage;
	
    final ToggleGroup radiogGroup = new ToggleGroup();
	
    //movie genre
	final String[] genreArr = new String[]{"Action", "Drama", "Adventure", "Comedy", "Animation", "Sci-Fi", "Fantasy", "Crime", "Thriller", "Sport", "War", "History", "Documentary"};
	
	@FXML
	private Label lblTitle;
	
	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtName;

	@FXML
	private TextField txtDirector;

	@FXML
	private ChoiceBox<String> choiceGenre;
	
	@FXML
	private RadioButton radMov;
	
	@FXML
	private RadioButton radTV;
	
	@FXML
	private DatePicker dateRelease;
	
	private MediaAddModel model;
	
	private String selectedGenre;
	
	private String selectedType;
	
	public MediaAddController() {
		model = new MediaAddModel();
		// Create a Runnable
		Runnable task = new Runnable()
		{
			public void run()
			{
				initChoiceBox();
				initRadioBtn();
				initAddOrUpdate();
			}
		};
		Thread backgroundThread = new Thread(task);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}
	
	//initial choiceBox width options
	public void initChoiceBox() {
		Platform.runLater(() -> {
			ObservableList<String> list = FXCollections.observableArrayList(genreArr);
			choiceGenre.setItems(list);
			// get value from changed event
			choiceGenre.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Object>() {
				@Override
				public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
					selectedGenre = genreArr[(int) newValue];
				}
			});
		});
	}
	
	//initial radioButton width options
	public void initRadioBtn() {
		Platform.runLater(() -> {
           
            radMov.setToggleGroup(radiogGroup);
            radMov.setUserData("Films");

            radTV.setToggleGroup(radiogGroup);
            radTV.setUserData("TV Series");
            // get value from changed event
            radiogGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            	@Override
                public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                    if (radiogGroup.getSelectedToggle() != null) {
                    	selectedType = (String) radiogGroup.getSelectedToggle().getUserData();
                    }
                }
            });
		});
	}
	
	// get media detail if it's for update
	public void initAddOrUpdate() {
		Platform.runLater(() -> {
			if (media_id == 0) {
				lblTitle.setText("Add Films/TV Series");//show right title
				return;
			}
			lblTitle.setText("Update Films/TV Series");
			ArrayList<Object> data = model.getMediaDetail(media_id);
			String genre = (String)data.get(1);
			String type = (String)data.get(3);
			String title = (String)data.get(4);
			String director = (String)data.get(5);
			Date releaseDate = (Date)data.get(6);
			// transfer to LocalDate for Date Picker
			Format formatter = new SimpleDateFormat("yyyy/MM/dd/");
			String dateArr[] = formatter.format(releaseDate).split("/");
			LocalDate localDate = LocalDate.of(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
			// put the detail into interface
			txtName.setText(title);
			txtDirector.setText(director);
			dateRelease.setValue(localDate);
			selectedGenre = genre;
			selectedType = type;
			// for choiceBox and radioButton to show the old value
			choiceGenre.setValue(genre);
			radiogGroup.selectToggle(type.equals("Films") ? radMov : radTV);
		});
	}
	
	public static void setMediaId(int id) {
		media_id = id;
	}
	
	public static void setStage(Stage stage) {
		current_stage = stage;
	}
	
	//verify the submit data
	public void submitMedia() {
		lblError.setText("");
		String title = txtName.getText();
		String director = txtDirector.getText();
		LocalDate release_date = dateRelease.getValue();
		// Validations
		if (title == null || title.trim().equals("")) {
			lblError.setText("title Cannot be empty or spaces");
			return;
		}
		if (director == null || director.trim().equals("")) {
			lblError.setText("director Cannot be empty or spaces");
			return;
		}
		if (selectedGenre == null) {
			lblError.setText("genre Cannot be empty");
			return;
		}
		if (release_date == null ) {
			lblError.setText("release date Cannot be empty");
		return;
		}
		if (selectedType == null ) {
			lblError.setText("type Cannot be empty");
			return;
		}
		// transfer date format to string
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String releaseDate = release_date.format(dateTimeFormatter);
		
		handleSubmit(title, selectedGenre, selectedType, director, releaseDate);
	}

	//add or update media
	public void handleSubmit(String title, String genre, String media_type, String director, String release_date) {
		Boolean isDone;
		Boolean isAdd = media_id == 0;//if media_id is 0,add a new one
		if (isAdd) isDone = model.addMedia(title, genre, media_type, director, release_date);
		else isDone = model.UpdateMedia(media_id, title, genre, media_type, director, release_date);
		
		if (isDone) {
			// show success dialog
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Films/TV Series Archives");
			alert.setHeaderText(isAdd ? "Add item success!" : "Update item success!");
			alert.showAndWait();
			if (isAdd) {// if it's for add, clear all input field
				txtName.setText("");
				txtDirector.setText("");
				dateRelease.setValue(null);
				selectedGenre = null;
				selectedType = null;
				choiceGenre.setValue(null);
				radiogGroup.selectToggle(null);
			} else {// if it's for update, close the stage
				current_stage.close();
			}
		}
	}
	
	

}
