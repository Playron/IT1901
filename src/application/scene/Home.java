package application.scene;

import java.util.ArrayList;
import java.util.Optional;

import application.database.Content;
import application.database.CurrentUser;
import application.database.DB;
import application.database.Login;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Home {

	/**
	 * This is the name of the website. This will show up in the adressbar.
	 */
	static String website = "          https://www.contentmanagementsystem.com/home";

	/**
	 * This will represent the width of the maximised stage
	 */
	static double w;

	/**
	 * This will represent the height of the maximised stage
	 */
	static double h;
	
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
	 * This is a int that keeps track of which post the editor wants to edit. 
	 * This is only a global variable because the variable has to be global to work as intended.
	 */
	static int postToEdit = -1;

	/**
	 * This is a String that holds the username that is currently being searched for.
	 */
	static String search = "";

	/**
	 * The is a String that holds the ending to the adressbar based on the value of the search-String.
	 */
	static String searchFull = "";

	private static void updateSearch(String newSearch)
	{
		search = newSearch;
		if (search.length() > 0)
			searchFull = "/results?search_query=" + newSearch;
		else
			searchFull = "";
	}


	/**
	 * @param contentPane  The Pane in which we want to add content.
	 * @param addressField The Field which holds the "address" of the pane
	 * @param subSite      The text appended to the address to give proper TODO fix javadoc
	 * @author Torleif Hensvold
	 */
	private static void addContentToPane(Pane contentPane, TextField addressField, String subSite)
	{
		int i = 0;
		contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
		addressField.setText(website + subSite + searchFull);
		for (Label label : Posts.getLabels(search))
		{
			contentPane.getChildren().add(label);
			label.setLayoutX(80);
			label.setLayoutY(40 + (200 * i));
			i++;
		}
	}

	/**
	 * Contains all the buttons and panes you can see on the homescreen,
	 * and this is where you would see your feed (content).
	 *
	 * @param stage is the primaryStage passed along from Main
	 * @param w is the width of the maximised stage
	 * @param h is the height of the maximised stage
	 *
	 * @author Niklas S&oslash;lvberg
	 */
	public static void showHome(Stage stage, double width, double height) {
		w = width;
		h = height;

		Pane root = new Pane();

		TextField searchField = new TextField();
		searchField.setPromptText("Search");
		
		Pane topPane = new Pane();
		root.getChildren().add(topPane);

		ImageView background = new ImageView(new Image("application/library/images/background.png"));
		topPane.getChildren().add(background);

		ImageView buttons = new ImageView(new Image("application/library/images/buttons.png"));
		topPane.getChildren().add(buttons);

		TextField adressField = new TextField(website + "/published_content" + searchFull);
		topPane.getChildren().add(adressField);
		adressField.setFocusTraversable(false);
		adressField.setEditable(false);

		ImageView lock = new ImageView(new Image("application/library/images/lock.png"));
		topPane.getChildren().add(lock);
		
		Pane optionsPane = new Pane();
		root.getChildren().add(optionsPane);
		optionsPane.setStyle("-fx-background-color: #444444;");
		optionsPane.getChildren().add(searchField);
		
		Label loggedInLabel = new Label();
		optionsPane.getChildren().add(loggedInLabel);
		if (CurrentUser.getUsername() == null)
			loggedInLabel.setText("You are not logged in");
		else 
			loggedInLabel.setText("Logged in as:\n\t" + CurrentUser.getUsername());
		loggedInLabel.setTextFill(Color.web("#ffffff"));
		loggedInLabel.setFont(Font.font(10));
		
		Pane contentPane = new Pane();
		ScrollPane rightScroll = new ScrollPane(contentPane);
		rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		root.getChildren().add(rightScroll);
		int i = 0;
		if (showContent == 0)
		{
			contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
			adressField.setText(website + "/all_content" + searchFull);
			for (Label label : Posts.getLabels(search)) {
				contentPane.getChildren().add(label);
				label.setLayoutX(80);
				label.setLayoutY(40 + (200 * i));
				i++;
			}
		} else if (showContent == 1)
		{
			contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels(search).size()));
			adressField.setText(website + "/published_content" + searchFull);
			for (Label label : Posts.getPublishedLabels(search)) {
				contentPane.getChildren().add(label);
				label.setLayoutX(80);
				label.setLayoutY(40 + (200 * i));
				i++;
			}
		} else if (showContent == 2)
		{
			contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
			adressField.setText(website + "/submitted_content" + searchFull);
			for (Label label : Posts.getSubmittedLabels(search)) {
				contentPane.getChildren().add(label);
				label.setLayoutX(80);
				label.setLayoutY(40 + (200 * i));
				i++;
			}
		}

		// Button not visible. Still here as the onAction property still has uses
		Button refreshButton = new Button("O");
		topPane.getChildren().add(refreshButton);
		refreshButton.setVisible(false);
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				contentPane.getChildren().clear();
				int i = 0;
				if (showContent == 0)
				{
					contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
					adressField.setText(website + "/all_content" + searchFull);
					for (Label label : Posts.getLabels(search)) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				} else if (showContent == 1)
				{
					contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels(search).size()));
					adressField.setText(website + "/published_content" + searchFull);
					for (Label label : Posts.getPublishedLabels(search)) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				} else if (showContent == 2)
				{
					contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
					adressField.setText(website + "/submitted_content" + searchFull);
					for (Label label : Posts.getSubmittedLabels(search)) {
						contentPane.getChildren().add(label);
						label.setLayoutX(80);
						label.setLayoutY(40 + (200 * i));
						i++;
					}
				}
			}
		});

		Button createButton = new Button("Create content");
		optionsPane.getChildren().add(createButton);
		if (!CurrentUser.isRegistered())
			createButton.setDisable(true);
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
					contentArea.textProperty().addListener((observable, oldValue, newValue) ->
					{
						headerField.textProperty().addListener((observable1, oldValue1, newValue1) ->
						{
							submitButton.setDisable(newValue.trim().isEmpty() || newValue1.trim().isEmpty());
						});
					});
					headerField.textProperty().addListener((observable, oldValue, newValue) ->
					{
						contentArea.textProperty().addListener((observable1, oldValue1, newValue1) ->
						{
							submitButton.setDisable(newValue.trim().isEmpty() || newValue1.trim().isEmpty());
						});
					});
					Node publishButton = dialog.getDialogPane().lookupButton(publishButtonType);
					publishButton.setDisable(true);
					contentArea.textProperty().addListener((observable, oldValue, newValue) ->
					{
						headerField.textProperty().addListener((observable1, oldValue1, newValue1) ->
						{
							publishButton.setDisable(newValue.trim().isEmpty() || newValue1.trim().isEmpty());
						});
					});
					headerField.textProperty().addListener((observable, oldValue, newValue) ->
					{
						contentArea.textProperty().addListener((observable1, oldValue1, newValue1) ->
						{
							publishButton.setDisable(newValue.trim().isEmpty() || newValue1.trim().isEmpty());
						});
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
					if (showContent == 0)
					{
						contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
						adressField.setText(website + "/all_content" + searchFull);
						for (Label label : Posts.getLabels(search)) {
							contentPane.getChildren().add(label);
							label.setLayoutX(80);
							label.setLayoutY(40 + (200 * i));
							i++;
						}
					} else if (showContent == 1)
					{
						contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels(search).size()));
						adressField.setText(website + "/published_content" + searchFull);
						for (Label label : Posts.getPublishedLabels(search)) {
							contentPane.getChildren().add(label);
							label.setLayoutX(80);
							label.setLayoutY(40 + (200 * i));
							i++;
						}
					} else if (showContent == 2)
					{
						contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
						adressField.setText(website + "/submitted_content" + searchFull);
						for (Label label : Posts.getSubmittedLabels(search)) {
							contentPane.getChildren().add(label);
							label.setLayoutX(80);
							label.setLayoutY(40 + (200 * i));
							i++;
						}
					}
				}
			}
		});
		
		Button showAllButton = new Button("View all content");
		optionsPane.getChildren().add(showAllButton);
		if (!CurrentUser.hasEditorRights())
			showAllButton.setDisable(true);
		showAllButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 0;
				adressField.setText(website + "/all_content" + searchFull);
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
				for (Label label : Posts.getLabels(search)) {
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		});

		Button showSubmittedButton = new Button("View submitted content" + searchFull);
		optionsPane.getChildren().add(showSubmittedButton);
		if (!CurrentUser.hasEditorRights())
			showSubmittedButton.setDisable(true);
		showSubmittedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 2;
				adressField.setText(website + "/submitted_content" + searchFull);
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
				for (Label label : Posts.getSubmittedLabels(search)) {
					postToEdit = i;
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					label.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent me) {
							Post post = Posts.getSubmittedPosts().get(contentPane.getChildren().indexOf(label));
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
								Content.updateContent(post.getID(), text.get(0), text.get(1), text.get(2), CurrentUser.getUsername());
							});
							contentPane.getChildren().clear();
							int i = 0;
							if (showContent == 0)
							{
								contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
								adressField.setText(website + "/all_content" + searchFull);
								for (Label label : Posts.getLabels(search)) {
									contentPane.getChildren().add(label);
									label.setLayoutX(80);
									label.setLayoutY(40 + (200 * i));
									i++;
								}
							} else if (showContent == 1)
							{
								contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels(search).size()));
								adressField.setText(website + "/published_content" + searchFull);
								for (Label label : Posts.getPublishedLabels(search)) {
									contentPane.getChildren().add(label);
									label.setLayoutX(80);
									label.setLayoutY(40 + (200 * i));
									i++;
								}
							} else if (showContent == 2)
							{
								contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
								adressField.setText(website + "/submitted_content" + searchFull);
								for (Label label : Posts.getSubmittedLabels(search)) {
									contentPane.getChildren().add(label);
									label.setLayoutX(80);
									label.setLayoutY(40 + (200 * i));
									i++;
								}
							}
							showAllButton.fire();
							showSubmittedButton.fire();
						}
					});
					i++;
				}
			}
		});
		
		Button showPublishedButton = new Button("View published content");
		optionsPane.getChildren().add(showPublishedButton);
		showPublishedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 1;
				adressField.setText(website + "/published_content" + searchFull);
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels(search).size()));
				for (Label label : Posts.getPublishedLabels(search)) {
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		});

		searchField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				updateSearch(searchField.getText());
				refreshButton.fire();
			}
		});


		Scene scene = new Scene(root, w, h);
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
//		stage.setMaximized(true);
		
		refreshButton.requestFocus();


		w = stage.getWidth();
		h = stage.getHeight();

		Button adminToolButton = new Button("Admin tool");
		optionsPane.getChildren().add(adminToolButton);
		if (!CurrentUser.hasAdminRights())
			adminToolButton.setVisible(false);
		adminToolButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				AdminTool.showAdminTool(stage, w, h);
			}
		});

		Button loginButton = new Button();
		if (CurrentUser.getUsername() == null)
			loginButton.setText("Log in / Register");
		else loginButton.setText("Log out");
		optionsPane.getChildren().add(loginButton);
		loginButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				if (CurrentUser.getUsername() == null)
					LoginScreen.showLoginScreen(stage, w, h);
				else
				{
					Login.logout();
					loggedInLabel.setText("You are not logged in");
					loginButton.setText("Log in / Register");
				}
			}
		});
		
		
		
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h / 12);

		background.setLayoutX(0);
		background.setLayoutY(0);
		
		refreshButton.setLayoutX(2*(w/42));
		refreshButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		refreshButton.setPrefSize(w/42, adressField.getHeight());

		adressField.setLayoutX(w/12);
		adressField.setLayoutY(((h / 12) - adressField.getHeight()) / 2);
		adressField.setPrefWidth(w - (w / 6));

		buttons.setLayoutX((adressField.getLayoutX() - 78) / 2);
		buttons.setLayoutY(adressField.getLayoutY() + ((adressField.getHeight() - 14) / 2));

		lock.setLayoutX(adressField.getLayoutX() + 20);
		lock.setLayoutY(adressField.getLayoutY() + ((adressField.getHeight() - 13) / 2));
		
		optionsPane.setLayoutX(0);
		optionsPane.setLayoutY(h/12);
		optionsPane.setPrefSize(w/6, h-(h/12)-22);
		
		loggedInLabel.setLayoutX(6);
		loggedInLabel.setLayoutY(6);

		searchField.setLayoutX(5);
		searchField.setLayoutY(110);
		searchField.setPrefSize((w/6)-10, 30);
		
		createButton.setLayoutX(0);
		createButton.setLayoutY(50);
		createButton.setPrefSize(w/6, 50);
		
		showAllButton.setLayoutX(0);
		showAllButton.setLayoutY(150);
		showAllButton.setPrefSize(w/6, 50);
		
		showSubmittedButton.setLayoutX(0);
		showSubmittedButton.setLayoutY(200);
		showSubmittedButton.setPrefSize(w/6, 50);
		
		showPublishedButton.setLayoutX(0);
		showPublishedButton.setLayoutY(250);
		showPublishedButton.setPrefSize(w / 6, 50);

		adminToolButton.setLayoutX(0);
		adminToolButton.setLayoutY(350);
		adminToolButton.setPrefSize(w / 6, 50);

		loginButton.setLayoutX(0);
		loginButton.setLayoutY(450);
		loginButton.setPrefSize(w / 6, 50);
		
		rightScroll.setLayoutX(w/6);
		rightScroll.setLayoutY(h/12);
		rightScroll.setPrefSize(w-(w/6), h-(h/12)-22);
	}
	
}