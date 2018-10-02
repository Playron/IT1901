package application.scene;

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
import javafx.util.Pair;

public class Home {

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
		leftScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		root.getChildren().add(leftScroll);
		
		Pane contentPane = new Pane();
		ScrollPane rightScroll = new ScrollPane(contentPane);
		rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		root.getChildren().add(rightScroll);
		contentPane.setPrefHeight(20 + (50 * Posts.getLabels().size()));
		for (Label label : Posts.getLabels()) {
			contentPane.getChildren().add(label);
			label.setLayoutX(50);
			label.setLayoutY(20);
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
				// TODO: add functionality to this button if there's time; otherwise, remove button entirely
			}
		});
		

		
		// Button available for registered users, such that they can create content
		
		Button createContent = new Button("Create");
		optionsPane.getChildren().add(createContent);
		createContent.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (CurrentUser.isRegistered()) {
					Dialog<Pair<String, String>> dialog = new Dialog<Pair<String, String>>();
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.setTitle("Content Creator");
					dialog.setHeaderText(null);
					ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().setAll(submitButtonType, ButtonType.CANCEL);
					Pane dialogPane = new Pane();
					dialogPane.setPrefSize(200, 260);
					Label headerLabel = new Label("Header:");
					headerLabel.setLayoutX(20);
					headerLabel.setLayoutY(20);
					TextField headerField = new TextField();
					headerField.setLayoutX(20);
					headerField.setLayoutY(50);
					headerField.setPrefSize(160, 25);
					Label contentLabel = new Label("Content:");
					contentLabel.setLayoutX(20);
					contentLabel.setLayoutY(90);
					TextArea contentArea = new TextArea();
					contentArea.setLayoutX(20);
					contentArea.setLayoutY(120);
					contentArea.setPrefSize(160, 120);
					dialogPane.getChildren().setAll(headerLabel, headerField, contentLabel, contentArea);
					Node submitButton = dialog.getDialogPane().lookupButton(submitButtonType);
					submitButton.setDisable(true);
					contentArea.textProperty().addListener((observable, oldValue, newValue) -> {
						submitButton.setDisable(newValue.trim().isEmpty());
					});
					dialog.getDialogPane().setContent(dialogPane);
					dialog.setResultConverter(dialogButton -> {
						if (dialogButton == submitButtonType) {
							return new Pair<>(headerField.getText(), contentArea.getText());
						}
						return null;
					});
					Optional<Pair<String, String>> result = dialog.showAndWait();
					result.ifPresent(text -> {
						Content.addContent(text.getKey(), text.getValue());
					});
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
		leftScroll.setPrefSize(w/6, h-(h/12));
		
		rightScroll.setLayoutX(w/6);
		rightScroll.setLayoutY(h/12);
		rightScroll.setPrefSize(w-(w/6), h-(h/12));
	}
	
}
