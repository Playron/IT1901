package application.scene;

import java.util.ArrayList;
import java.util.Optional;

import application.database.Content;
import application.database.CurrentUser;
import application.database.DB;
import application.logic.Posts;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Home {

	/**
	 * Contains all the buttons and panes you can see on the homescreen,
	 * and this is where you would see your feed (content).
	 * 
	 * @param stage is the primaryStage passed along from Main.java
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void showHome(Stage stage) {
		Pane root = new Pane();
		
		Pane topPane = new Pane();
		root.getChildren().add(topPane);
		
		TextField adressField = new TextField("www.contentmanagementsystem.com");
		topPane.getChildren().add(adressField);
		adressField.setFocusTraversable(false);
		
		Pane optionsPane = new Pane();
		ScrollPane leftScroll = new ScrollPane(optionsPane);
		leftScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		leftScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		root.getChildren().add(leftScroll);
		leftScroll.setStyle("-fx-background-color: #444444;");
		
		Pane contentPane = new Pane();
		ScrollPane rightScroll = new ScrollPane(contentPane);
		rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		root.getChildren().add(rightScroll);
		contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
		int i = 0;
		for (Label label : Posts.getLabels()) {
			contentPane.getChildren().add(label);
			label.setLayoutX(80);
			label.setLayoutY(40 + (200 * i));
			i++;
		}
		
		Button backButton = new Button("<");
		topPane.getChildren().add(backButton);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				// TODO: add functionality to this button if there's time; otherwise, remove button entirely
			}
		});
		
		Button forwardButton = new Button(">");
		topPane.getChildren().add(forwardButton);
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				// TODO: add functionality to this button if there's time; otherwise, remove button entirely
			}
		});
		
		Button refreshButton = new Button("O");
		topPane.getChildren().add(refreshButton);
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				contentPane.getChildren().clear();
				contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
				int i = 0;
				for (Label label : Posts.getLabels()) {
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		});
		

		
		// Button available for registered users, such that they can create content
		
		Button createButton = new Button("Create");
		optionsPane.getChildren().add(createButton);
		createButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (CurrentUser.isRegistered()) {
					Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
					dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.setTitle("Make new post");
					dialog.setHeaderText(null);
					ButtonType publishButtonType = new ButtonType("Publish", ButtonData.OK_DONE);
					ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().setAll(publishButtonType, submitButtonType, ButtonType.CANCEL);
					Pane dialogPane = new Pane();
					dialogPane.setPrefSize(300, 300);
					Label headerLabel = new Label("Header:");
					headerLabel.setLayoutX(20);
					headerLabel.setLayoutY(20);
					TextField headerField = new TextField();
					headerField.setLayoutX(20);
					headerField.setLayoutY(50);
					headerField.setPrefSize(260, 25);
					Label contentLabel = new Label("Content:");
					contentLabel.setLayoutX(20);
					contentLabel.setLayoutY(90);
					TextArea contentArea = new TextArea();
					contentArea.setLayoutX(20);
					contentArea.setLayoutY(120);
					contentArea.setPrefSize(260, 160);
					contentArea.setWrapText(true);
					dialogPane.getChildren().setAll(headerLabel, headerField, contentLabel, contentArea);
					Node submitButton = dialog.getDialogPane().lookupButton(submitButtonType);
					submitButton.setDisable(true);
					contentArea.textProperty().addListener((observable, oldValue, newValue) -> {
						submitButton.setDisable(newValue.trim().isEmpty());
					});
					Node publishButton = dialog.getDialogPane().lookupButton(publishButtonType);
					publishButton.setDisable(true);
					contentArea.textProperty().addListener((observable, oldValue, newValue) -> {
						publishButton.setDisable(newValue.trim().isEmpty());
					});
					dialog.getDialogPane().setContent(dialogPane);
					dialog.setResultConverter(dialogButton -> {
						if (dialogButton == submitButtonType) {
							ArrayList<String> list = new ArrayList<String>();
							list.add(headerField.getText());
							list.add(contentArea.getText());
							list.add("submitted");
							return list;
						}
						else if (dialogButton == publishButtonType) {
							ArrayList<String> list = new ArrayList<String>();
							list.add(headerField.getText());
							list.add(contentArea.getText());
							list.add("published");
							return list;
						}
						return null;
					});
					Optional<ArrayList<String>> result = dialog.showAndWait();
					result.ifPresent(text -> {
						Content.addContent(text.get(0), text.get(1), text.get(2));
					});
					contentPane.getChildren().clear();
					contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
					int i = 0;
					for (Label label : Posts.getLabels()) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				}
			}
		});
		
		
		
		Scene scene = new Scene(root, 200, 200);
		scene.getStylesheets().add("application/library/stylesheets/basic.css");
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ESCAPE) {
					stage.close();
					DB.disconnect();
				}
			}
		});
		
		stage.setScene(scene);
		stage.show();
		
		refreshButton.requestFocus();
		
		
		
		double w = stage.getWidth();
		double h = stage.getHeight();
		
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h/12);
		
		backButton.setLayoutX(0);
		backButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		backButton.setPrefSize(w/42, adressField.getHeight());
		
		forwardButton.setLayoutX(w/42);
		forwardButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		forwardButton.setPrefSize(w/42, adressField.getHeight());
		
		refreshButton.setLayoutX(2*(w/42));
		refreshButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		refreshButton.setPrefSize(w/42, adressField.getHeight());
		
		adressField.setLayoutX(w/14);
		adressField.setLayoutY(((h/12)-adressField.getHeight())/2);
		adressField.setPrefWidth(w-(w/7));
		
		leftScroll.setLayoutX(0);
		leftScroll.setLayoutY(h/12);
		leftScroll.setPrefSize(w/6, h-(h/12)-38);
		
		createButton.setLayoutX(0);
		createButton.setLayoutY(0);
		createButton.setPrefSize(w/6, 50);
		
		rightScroll.setLayoutX(w/6);
		rightScroll.setLayoutY(h/12);
		rightScroll.setPrefSize(w-(w/6)-17, h-(h/12)-38);
	}
	
}
