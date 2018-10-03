package application.scene;

import java.util.ArrayList;
import java.util.Optional;

import application.database.Content;
import application.database.CurrentUser;
import application.database.DB;
import application.logic.Post;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Home {
	
//	showContent == 0 => show all content
//	showContent == 1 => show published content
//	showContent == 2 => show submitted (editable) content
	/**
	 * This is a byte-value that decides what content is being viewed.
	 * <br><br>The different values represents:
	 * <ol start="0">
	 * <li>Show all content</li>
	 * <li>Show published content</li>
	 * <li>Show submitted (editable) content (only for editor-level access and above)</li>
	 * </ol>
	 */
	static byte showContent = 1;
	
	/**
	 * This is a static variable that keeps track of which post the editor wants to edit. 
	 * This is only a global variable because the variable has to be global to work as intended.
	 */
	static int postToEdit = -1;

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
		root.getChildren().add(optionsPane);
		optionsPane.setStyle("-fx-background-color: #444444;");
		
		Pane contentPane = new Pane();
		ScrollPane rightScroll = new ScrollPane(contentPane);
		rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		root.getChildren().add(rightScroll);
		int i = 0;
		if (showContent == 0) {
			contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
			for (Label label : Posts.getLabels()) {
				contentPane.getChildren().add(label);
				label.setLayoutX(80);
				label.setLayoutY(40 + (200 * i));
				i++;
			}
		}
		else if (showContent == 1) {
			contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels().size()));
			for (Label label : Posts.getPublishedLabels()) {
				contentPane.getChildren().add(label);
				label.setLayoutX(80);
				label.setLayoutY(40 + (200 * i));
				i++;
			}
		}
		else if (showContent == 2) {
			contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels().size()));
			for (Label label : Posts.getSubmittedLabels()) {
				contentPane.getChildren().add(label);
				label.setLayoutX(80);
				label.setLayoutY(40 + (200 * i));
				i++;
			}
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
				int i = 0;
				if (showContent == 0) {
					contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
					for (Label label : Posts.getLabels()) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				}
				else if (showContent == 1) {
					contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels().size()));
					for (Label label : Posts.getPublishedLabels()) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				}
				else if (showContent == 2) {
					contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels().size()));
					for (Label label : Posts.getSubmittedLabels()) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				}
			}
		});
		
		// Button available for registered users, such that they can create content
		
		Button createButton = new Button("Create content");
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
						Content.addContent(text.get(0), text.get(1), text.get(2), null);
					});
					contentPane.getChildren().clear();
					int i = 0;
					if (showContent == 0) {
						contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
						for (Label label : Posts.getLabels()) {
							contentPane.getChildren().add(label);
							label.setLayoutX(80);
							label.setLayoutY(40 + (200 * i));
							i++;
						}
					}
					else if (showContent == 1) {
						contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels().size()));
						for (Label label : Posts.getPublishedLabels()) {
							contentPane.getChildren().add(label);
							label.setLayoutX(80);
							label.setLayoutY(40 + (200 * i));
							i++;
						}
					}
					else if (showContent == 2) {
						contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels().size()));
						for (Label label : Posts.getSubmittedLabels()) {
							contentPane.getChildren().add(label);
							label.setLayoutX(80);
							label.setLayoutY(40 + (200 * i));
							i++;
						}
					}
				}
			}
		});
		
		Button showSubmittedButton = new Button("Submitted content");
		optionsPane.getChildren().add(showSubmittedButton);
		if (!CurrentUser.hasEditorRights())
			showSubmittedButton.setDisable(true);
		showSubmittedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showContent = 2;
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels().size()));
				for (Label label : Posts.getSubmittedLabels()) {
					postToEdit = i;
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					label.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent me) {
							Post post = Posts.getSubmittedPosts().get(postToEdit);
							Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
							dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
							dialog.initModality(Modality.APPLICATION_MODAL);
							dialog.setTitle("Make new post");
							dialog.setHeaderText(null);
							ButtonType publishButtonType = new ButtonType("Publish", ButtonData.OK_DONE);
							dialog.getDialogPane().getButtonTypes().setAll(publishButtonType, ButtonType.CANCEL);
							Pane dialogPane = new Pane();
							dialogPane.setPrefSize(300, 300);
							Label headerLabel = new Label("Header:");
							headerLabel.setLayoutX(20);
							headerLabel.setLayoutY(20);
							TextField headerField = new TextField(post.getHeader());
							headerField.setLayoutX(20);
							headerField.setLayoutY(50);
							headerField.setPrefSize(260, 25);
							Label contentLabel = new Label("Content:");
							contentLabel.setLayoutX(20);
							contentLabel.setLayoutY(90);
							TextArea contentArea = new TextArea(post.getBody());
							contentArea.setLayoutX(20);
							contentArea.setLayoutY(120);
							contentArea.setPrefSize(260, 160);
							contentArea.setWrapText(true);
							dialogPane.getChildren().setAll(headerLabel, headerField, contentLabel, contentArea);
							Node publishButton = dialog.getDialogPane().lookupButton(publishButtonType);
							publishButton.setDisable(true);
							contentArea.textProperty().addListener((observable, oldValue, newValue) -> {
								publishButton.setDisable(newValue.trim().isEmpty());
							});
							dialog.getDialogPane().setContent(dialogPane);
							dialog.setResultConverter(dialogButton -> {
								if (dialogButton == publishButtonType) {
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
								Content.addContent(text.get(0), text.get(1), text.get(2), CurrentUser.getUsername());
							});
							contentPane.getChildren().clear();
							int i = 0;
							if (showContent == 0) {
								contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
								for (Label label : Posts.getLabels()) {
									contentPane.getChildren().add(label);
									label.setLayoutX(80);
									label.setLayoutY(40 + (200 * i));
									i++;
								}
							}
							else if (showContent == 1) {
								contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels().size()));
								for (Label label : Posts.getPublishedLabels()) {
									contentPane.getChildren().add(label);
									label.setLayoutX(80);
									label.setLayoutY(40 + (200 * i));
									i++;
								}
							}
							else if (showContent == 2) {
								contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels().size()));
								for (Label label : Posts.getSubmittedLabels()) {
									contentPane.getChildren().add(label);
									label.setLayoutX(80);
									label.setLayoutY(40 + (200 * i));
									i++;
								}
							}
						}
					});
					i++;
				}
			}
		});
		
		Button showPublishedButton = new Button("Published content");
		optionsPane.getChildren().add(showPublishedButton);
		showPublishedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showContent = 1;
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels().size()));
				for (Label label : Posts.getPublishedLabels()) {
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		});
		
		Button showAllButton = new Button("All content");
		optionsPane.getChildren().add(showAllButton);
		if (!CurrentUser.hasEditorRights())
			showAllButton.setDisable(true);
		showAllButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showContent = 0;
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getLabels().size()));
				for (Label label : Posts.getLabels()) {
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
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
		
		optionsPane.setLayoutX(0);
		optionsPane.setLayoutY(h/12);
		optionsPane.setPrefSize(w/6, h-(h/12)-38);
		
		showAllButton.setLayoutX(0);
		showAllButton.setLayoutY(0);
		showAllButton.setPrefSize(w/6, 50);
		
		showSubmittedButton.setLayoutX(0);
		showSubmittedButton.setLayoutY(50);
		showSubmittedButton.setPrefSize(w/6, 50);
		
		showPublishedButton.setLayoutX(0);
		showPublishedButton.setLayoutY(100);
		showPublishedButton.setPrefSize(w/6, 50);
		
		createButton.setLayoutX(0);
		createButton.setLayoutY(0);
		createButton.setPrefSize(w/6, 50);
		
		rightScroll.setLayoutX(w/6);
		rightScroll.setLayoutY(h/12);
		rightScroll.setPrefSize(w-(w/6)-17, h-(h/12)-38);
	}
	
}
