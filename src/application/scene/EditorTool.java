package application.scene;

import application.Main;
import application.database.Content;
import application.database.CurrentUser;
import application.database.DB;
import application.database.Login;
import application.logic.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.util.ArrayList;
import java.util.Optional;

public class EditorTool
{
	
	private static String searchFull = "";
	private static String search = "";
	private static Byte showContent = 0;
	private static int postToEdit = -1;
	
	private static void updateSearch(String newSearch)
	{
		search = newSearch;
		if (search.length() > 0)
		{
			searchFull = "/results?search_query=" + newSearch;
		}
		else
		{
			searchFull = "";
		}
	}
	
	
	/**
	 * This is the path to our application library
	 */
	private static String library = "application/library";

	/**
	 * This is the path to our application image library
	 */
	private static String images = library + "/images";

	/**
	 * This is the path to our application stylesheet library
	 */
	private static String stylesheets = library + "/stylesheets";

	/**
	 * This is the name of the website. This will show up in the adressbar.
	 */
	static String website = "          https://www.contentmanagementsystem.com/edit_tool";
	
	/**
	 * This will represent the width of the maximised stage
	 */
	static double w;
	
	/**
	 * This will represent the height of the maximised stage
	 */
	static double h;
	
	/**
	 * Method for adding views to a pane.
	 *
	 * @param parentPane The parent pane of the view
	 * @param imagePath  The path of the image we want to display
	 * @param x          Layout x-value for topmost left corner
	 * @param y          Layout y-value for topmost left corner
	 * @param width      Width of the Viewport we want to view the image through
	 * @param height     Height of the Viewport we want to view the image through
	 */
	private static void addViews(Pane parentPane, String imagePath, double x, double y, double width, double height)
	{
		ImageView iv = new ImageView(new Image(imagePath));
		iv.setLayoutX(x);
		iv.setLayoutY(y);
		iv.setViewport(new Rectangle2D(0, 0, width, height));
		parentPane.getChildren().add(iv);
	}
	
	/**
	 * @param contentPane  The Pane in which we want to add content.
	 * @param addressField The Field which holds the "address" of the pane
	 * @param subSite      The text appended to the address to give proper TODO fix javadoc
	 * @author Torleif Hensvold
	 * @author Niklas Sølvberg
	 */
	private static void addContentToPane(Pane contentPane, TextField addressField, String subSite)
	{
		int i = 0;
		contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
		addressField.setText(website + subSite + searchFull);
		for (Label label : Posts.getLabels(search))
		{
			addLabels(contentPane, label, 80, 40, 200, i);
			i++;
		}
	}
	
	/**
	 * @param contentPane The Pane in which we want to add Labels
	 * @param label       The label we want to add
	 * @param x           The TODO find out what this actually is!
	 * @param y           The TODO Find out what this actually is!
	 * @param dy          The TODO Find out what this actually is!
	 * @param i           Variable to keep count of iterations.
	 * @author Torleif Hensvold
	 * @author Niklas Sølvberg
	 */
	private static void addLabels(Pane contentPane, Label label, int x, int y, int dy, int i)
	{
		contentPane.getChildren().add(label);
		label.setLayoutX(x);
		label.setLayoutY(y + (dy * i));
	}
	
	
	/**
	 * @param contentPane  The Pane in which we want to add content
	 * @param addressField The address field of the scene.
	 * @author Torleif Hensvold
	 * @author Niklas Sølvberg
	 */
	private static void populateContent(Pane contentPane, TextField addressField)
	{
		if (showContent == 0)
		{
			addContentToPane(contentPane, addressField, "/all_content");
		}
		else if (showContent == 1)
		{
			addContentToPane(contentPane, addressField, "/published_content");
		}
		else if (showContent == 2)
		{
			addContentToPane(contentPane, addressField, "submitted_content");
		}
	}
	
	/**
	 * Contains all the buttons and panes you can see in the admin tool,
	 * and this is where an admin would assign and manage the roles of users on the site.
	 *
	 * @param stage  is the primaryStage passed along from Main
	 * @param width  is the width of the maximised stage
	 * @param height is the height of the maximised stage
	 * @author Torleif Hensvold
	 */
	public static void showEditTool(Stage stage, double width, double height)
	{
		w = width;
		h = height;
		
		/*double stageHeight = stage.getHeight();
		double stageWidth = stage.getWidth();*/
		System.out.println(w + " " + h);
		Pane root = new Pane();
		
		TextField searchField = new TextField();
		searchField.setPromptText("Search");
		
		Pane topPane = new Pane();
		root.getChildren().add(topPane);
		
		ImageView background = new ImageView(new Image("application/library/images/background.png"));
		background.setViewport(new Rectangle2D(0,0,w,h/25));
		topPane.getChildren().add(background);
		
		ImageView buttons = new ImageView(new Image("application/library/images/buttons.png"));
		topPane.getChildren().add(buttons);
		
		TextField addressField = new TextField(website);
		topPane.getChildren().add(addressField);
		addressField.setFocusTraversable(false);
		addressField.setEditable(false);
		
		ImageView lock = new ImageView(new Image("application/library/images/lock.png"));
		topPane.getChildren().add(lock);
		
		/*Pane optionsPane = new Pane();
		root.getChildren().add(optionsPane);
		optionsPane.setStyle("-fx-background-color: #444444;");
		optionsPane.getChildren().add(searchField);
		
		Label loggedInLabel = new Label();
		optionsPane.getChildren().add(loggedInLabel);
		if (CurrentUser.getUsername() == null)
		{
			loggedInLabel.setText("You are not logged in");
		}
		else
		{
			loggedInLabel.setText("Logged in as:\n\t" + CurrentUser.getUsername());
		}
		loggedInLabel.setTextFill(Color.web("#ffffff"));
		loggedInLabel.setFont(Font.font(10));
		
		Pane contentPane = new Pane();
		ScrollPane rightScroll = new ScrollPane(contentPane);
		rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		root.getChildren().add(rightScroll);
		populateContent(contentPane, addressField);*/
		
		
		/*// Button not visible. Still here as the onAction property still has uses
		Button refreshButton = new Button("O");
		topPane.getChildren().add(refreshButton);
		refreshButton.setVisible(false);
		refreshButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				contentPane.getChildren().clear();
				int i = 0;
				populateContent(contentPane, addressField);
			}
		});*/
		
		/*Button createButton = new Button("Create content");
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
					ButtonType publishButtonType = new ButtonType("Publish", ButtonBar.ButtonData.OK_DONE);
					ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
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
					populateContent(contentPane, addressField);
				}
			}
		});*/
		
		/*Button showAllButton = new Button("View all content");
		optionsPane.getChildren().add(showAllButton);
		if (!CurrentUser.hasEditorRights())
		{
			showAllButton.setDisable(true);
		}
		showAllButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 0;
				addressField.setText(website + "/all_content" + searchFull);
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
				for (Label label : Posts.getLabels(search))
				{
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		});*/
		
		/*Button showSubmittedButton = new Button("View submitted content" + searchFull);
		optionsPane.getChildren().add(showSubmittedButton);
		if (!CurrentUser.hasEditorRights())
		{
			showSubmittedButton.setDisable(true);
		}*/
		/*showSubmittedButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 2;
				addressField.setText(website + "/submitted_content" + searchFull);
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
				for (Label label : Posts.getSubmittedLabels(search))
				{
					postToEdit = i;
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					label.setOnMouseClicked(new EventHandler<MouseEvent>()
					{
						@Override
						public void handle(MouseEvent me)
						{
							Post post = Posts.getSubmittedPosts().get(contentPane.getChildren().indexOf(label));
							Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
							dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
							dialog.initModality(Modality.APPLICATION_MODAL);
							dialog.setTitle("Make new post");
							dialog.setHeaderText(null);
							ButtonType publishButtonType = new ButtonType("Publish", ButtonBar.ButtonData.OK_DONE);
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
							contentArea.textProperty().addListener((observable, oldValue, newValue) ->
							{
								publishButton.setDisable(newValue.trim().isEmpty());
							});
							dialog.getDialogPane().setContent(dialogPane);
							dialog.setResultConverter(dialogButton ->
							{
								if (dialogButton == publishButtonType)
								{
									ArrayList<String> list = new ArrayList<String>();
									list.add(headerField.getText());
									list.add(contentArea.getText());
									list.add("published");
									return list;
								}
								return null;
							});
							Optional<ArrayList<String>> result = dialog.showAndWait();
							result.ifPresent(text ->
							{
								Content.updateContent(post.getID(), text.get(0), text.get(1), text.get(2), CurrentUser.getUsername());
							});
							contentPane.getChildren().clear();
							populateContent(contentPane, addressField);
							showAllButton.fire();
							showSubmittedButton.fire();
						}
					});
					i++;
				}
			}
		});*/
		
		/*Button showPublishedButton = new Button("View published content");
		optionsPane.getChildren().add(showPublishedButton);
		showPublishedButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 1;
				addressField.setText(website + "/published_content" + searchFull);
				contentPane.getChildren().clear();
				int i = 0;
				contentPane.setPrefHeight(40 + (200 * Posts.getPublishedLabels(search).size()));
				for (Label label : Posts.getPublishedLabels(search))
				{
					contentPane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		});*/
		
		/*searchField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				updateSearch(searchField.getText());
				refreshButton.fire();
			}
		});
		*/
		
		Scene scene = new Scene(root, w, h);
		scene.getStylesheets().add("application/library/stylesheets/basic.css");
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode() == KeyCode.ESCAPE)
				{
					stage.close();
					DB.disconnect();
				}
			}
		});
		
		stage.setScene(scene);
		stage.show();
//		stage.setMaximized(true);
		
		/*refreshButton.requestFocus();*/
		
		
		w = stage.getWidth();
		h = stage.getHeight();
		
		/*Button adminToolButton = new Button("Admin tool");
		optionsPane.getChildren().add(adminToolButton);
		if (!CurrentUser.hasAdminRights())
		{
			adminToolButton.setVisible(false);
		}
		adminToolButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				AdminTool.showAdminTool(stage, w, h);
			}
		});*/
		
		/*Button loginButton = new Button();
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
		});*/
		
		
		topPane.setPrefSize(w, 100);
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setMaxHeight(h/12);
		
		background.setLayoutX(0);
		background.setLayoutY(0);
		
		
		addressField.setLayoutX(w / 12);
		addressField.setLayoutY(((h / 12) - addressField.getHeight()) / 2);
		addressField.setPrefWidth(w - (w / 6));
		
		buttons.setLayoutX((addressField.getLayoutX() - 78) / 2);
		buttons.setLayoutY(addressField.getLayoutY() + ((addressField.getHeight() - 14) / 2));
		
		lock.setLayoutX(addressField.getLayoutX() + 20);
		lock.setLayoutY(addressField.getLayoutY() + ((addressField.getHeight() - 13) / 2));
		
		/*optionsPane.setLayoutX(0);
		optionsPane.setLayoutY(h / 12);
		optionsPane.setPrefSize(w / 6, h - (h / 12) - 22);
		
		loggedInLabel.setLayoutX(6);
		loggedInLabel.setLayoutY(6);*/
		
		searchField.setLayoutX(5);
		searchField.setLayoutY(110);
		searchField.setPrefSize((w / 6) - 10, 30);
		
		/*createButton.setLayoutX(0);
		createButton.setLayoutY(50);
		createButton.setPrefSize(w/6, 50);*/
		
		/*showAllButton.setLayoutX(0);
		showAllButton.setLayoutY(150);
		showAllButton.setPrefSize(w / 6, 50);*/
		
	/*	showSubmittedButton.setLayoutX(0);
		showSubmittedButton.setLayoutY(200);
		showSubmittedButton.setPrefSize(w / 6, 50);
		
		showPublishedButton.setLayoutX(0);
		showPublishedButton.setLayoutY(250);
		showPublishedButton.setPrefSize(w / 6, 50);
		
		adminToolButton.setLayoutX(0);
		adminToolButton.setLayoutY(350);
		adminToolButton.setPrefSize(w / 6, 50);*/
		
		/*loginButton.setLayoutX(0);
		loginButton.setLayoutY(450);
		loginButton.setPrefSize(w / 6, 50);*/
		
		/*rightScroll.setLayoutX(w / 6);
		rightScroll.setLayoutY(h / 12);
		rightScroll.setPrefSize(w-(w/6), h-(h/12)-22);*/
		
		
		
		/*w = width;
		h = height;

		System.out.println("Started Edit tool");

		Pane root = new Pane();                             // Creates a root in which we hang everything else in the scene
		root.setId("root");                                 // Allows searching the scene for this pane.
		Scene editScene = new Scene(root, width, height);   // Creates a new Scene using the root and given dimensions
		stage.setScene(editScene);                          // Instructs the single Stage to prepare the Scene
		stage.show();                                       // Instructs the single Stage to be shown. Necessary!

		editScene.getStylesheets().add(stylesheets + "/basic.css");

		escapeKeyHandler(editScene);                        // Handles the escape key

		Pane topPane = new Pane();
		topPane.setId("topPane");
		root.getChildren().add(topPane);
		//addTopBar(topPane);

		addViews(topPane, images + "/background.png", 0, 0);
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h / 12);



		Pane optionsPane = new Pane();
		optionsPane.setId("optionsPane");
		root.getChildren().add(optionsPane);
		optionsPane.setStyle("-fx-background-color: #444444;");

		optionsPane.setLayoutX(0);
		optionsPane.setLayoutY(h / 12);
		optionsPane.setPrefSize(w / 6, h - (h / 12) - 22);
		optionsPane.getChildren().add(new TextField());

		for (Object o : root.getChildren())
		{
			System.out.println(o.toString());
		}
*/
		//Main.disconnectDB();
		
		/*
		Pane root = new Pane();
		Scene editScene = new Scene(root, width, height);
		stage.setScene(editScene);
		stage.show();
		
		Pane topPane = new Pane();
		
		root.getChildren().add(topPane);
		
		System.out.println("adding view1");
		addViews(topPane, images + "/background.png");
		
		System.out.println("adding view2");
		addViews(topPane, images + "buttons.png");
		*/
		
		/*Pane root = new Pane();
		
		Pane topPane = new Pane();
		root.getChildren().add(topPane);
		
		ImageView background = new ImageView(new Image("application/library/images/background.png"));
		topPane.getChildren().add(background);
		
		ImageView buttons = new ImageView(new Image("application/library/images/buttons.png"));
		topPane.getChildren().add(buttons);
		
		TextField addressField = new TextField(website);
		topPane.getChildren().add(addressField);
		addressField.setFocusTraversable(false);
		addressField.setEditable(false);
		
		ImageView lock = new ImageView(new Image("application/library/images/lock.png"));
		topPane.getChildren().add(lock);
		
		Pane usersPane = new Pane();
		ScrollPane scrollPane = new ScrollPane(usersPane);
		root.getChildren().add(scrollPane);
		int i = 0;
		for (Label label : Users.getLabelsNotCurrent())
		{
			usersPane.getChildren().add(label);
			label.setLayoutX(200);
			label.setLayoutY(160 + (60 * i));
			label.setFont(Font.font("Andale Mono", 16));
			i++;
		}
		
		
		Scene scene = new Scene(root, w, h);
		scene.getStylesheets().add("application/library/stylesheets/basic.css");
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode() == KeyCode.ESCAPE)
				{
					stage.close();
					DB.disconnect();
				}
			}
		});
		
		stage.setScene(scene);
		stage.show();
		
		
		Button backToHomeButton = new Button("<-- back to the feed");
		root.getChildren().add(backToHomeButton);
		backToHomeButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				Home.showHome(stage, w, h);
			}
		});
		
		
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h / 12);
		
		background.setLayoutX(0);
		background.setLayoutY(0);
		
		addressField.setLayoutX(w / 12);
		addressField.setLayoutY(((h / 12) - addressField.getHeight()) / 2);
		addressField.setPrefWidth(w - (w / 6));
		
		buttons.setLayoutX((addressField.getLayoutX() - 78) / 2);
		buttons.setLayoutY(addressField.getLayoutY() + ((addressField.getHeight() - 14) / 2));
		
		lock.setLayoutX(addressField.getLayoutX() + 20);
		lock.setLayoutY(addressField.getLayoutY() + ((addressField.getHeight() - 13) / 2));
		
		backToHomeButton.setLayoutX(0);
		backToHomeButton.setLayoutY(h / 12);
		backToHomeButton.setPrefSize(w, 50);
		
		scrollPane.setLayoutX(0);
		scrollPane.setLayoutY((h / 12) + 50);
		scrollPane.setPrefSize(w, h - (h / 12) - 72);
		*/
	}

	/**
	 * This is a method to handle the use of the escape key in the application
	 *
	 * @param scene The Scene which we want to handle the KeyEvent
	 */
	private static void escapeKeyHandler(Scene scene)
	{
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode() == KeyCode.ESCAPE)
				{
					Platform.exit();
					//Main.disconnectDB();
				}
			}
		});
	}

	public static void addTopBar(Pane topPane)
	{
		addViews(topPane, images + "/background.png", 0, 0);
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h / 12);
	}
}