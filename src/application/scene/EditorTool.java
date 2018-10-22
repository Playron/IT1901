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

// TODO Fix the whole clustertruck!
// FIXME: This is by no means finished, but will be part of the development-branch so that the other changes made to this branch will be added, so that we can progress further

public class EditorTool
{
	
	private static String searchFull = "";
	private static String search = "";
	private static Byte showContent = 0;
	private static int postToEdit = -1;

	private static Stage stage;
	private static Scene editScene;

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
	 * Contains all the buttons and panes you can see in the admin tool,
	 * and this is where an admin would assign and manage the roles of users on the site.
	 *
	 * @param staged  is the primaryStage passed along from Main
	 * @param width  is the width of the maximised stage
	 * @param height is the height of the maximised stage
	 * @author Torleif Hensvold
	 */
	public static void showEditTool(Stage staged, double width, double height)
	{
		stage = staged;
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
		Home.populateContent(contentPane, adressField);


		// Button not visible. Still here as the onAction property still has uses
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
				Home.populateContent(contentPane, adressField);
			}
		});

		Button createButton = new Button("Create content");
		optionsPane.getChildren().add(createButton);
		if (!CurrentUser.isRegistered())
			createButton.setDisable(true);
		Home.createButtonOnAction(createButton, contentPane, adressField);


		Button showAllButton = new Button("View all content");
		optionsPane.getChildren().add(showAllButton);
		if (!CurrentUser.hasEditorRights())
			showAllButton.setDisable(true);
		Home.showAllButtonOnAction(showAllButton, adressField, contentPane);



		Button showSubmittedButton = new Button("View submitted content" + searchFull);
		optionsPane.getChildren().add(showSubmittedButton);
		if (!CurrentUser.hasEditorRights())
			showSubmittedButton.setDisable(true);
		Home.showSubmittedButtonOnAction(showSubmittedButton, adressField, contentPane, showAllButton);


		Button showPublishedButton = new Button("View published content");
		optionsPane.getChildren().add(showPublishedButton);
		Home.showPublishedButtonOnAction(showPublishedButton, adressField, contentPane);

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
		Home.loginButton(loginButton, loggedInLabel);



		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h / 12);

		background.setLayoutX(0);
		background.setLayoutY(0);

		refreshButton.setLayoutX(2 * (w / 42));
		refreshButton.setLayoutY(((h / 12) - adressField.getHeight()) / 2);
		refreshButton.setPrefSize(w / 42, adressField.getHeight());

		adressField.setLayoutX(w / 12);
		adressField.setLayoutY(((h / 12) - adressField.getHeight()) / 2);
		adressField.setPrefWidth(w - (w / 6));

		buttons.setLayoutX((adressField.getLayoutX() - 78) / 2);
		buttons.setLayoutY(adressField.getLayoutY() + ((adressField.getHeight() - 14) / 2));

		lock.setLayoutX(adressField.getLayoutX() + 20);
		lock.setLayoutY(adressField.getLayoutY() + ((adressField.getHeight() - 13) / 2));

		optionsPane.setLayoutX(0);
		optionsPane.setLayoutY(h / 12);
		optionsPane.setPrefSize(w / 6, h - (h / 12) - 22);

		loggedInLabel.setLayoutX(6);
		loggedInLabel.setLayoutY(6);

		searchField.setLayoutX(5);
		searchField.setLayoutY(110);
		searchField.setPrefSize((w / 6) - 10, 30);

		createButton.setLayoutX(0);
		createButton.setLayoutY(50);
		createButton.setPrefSize(w / 6, 50);

		showAllButton.setLayoutX(0);
		showAllButton.setLayoutY(150);
		showAllButton.setPrefSize(w / 6, 50);

		showSubmittedButton.setLayoutX(0);
		showSubmittedButton.setLayoutY(200);
		showSubmittedButton.setPrefSize(w / 6, 50);

		showPublishedButton.setLayoutX(0);
		showPublishedButton.setLayoutY(250);
		showPublishedButton.setPrefSize(w / 6, 50);

		adminToolButton.setLayoutX(0);
		adminToolButton.setLayoutY(350);
		adminToolButton.setPrefSize(w / 6, 50);

		loginButton.setLayoutX(0);
		loginButton.setLayoutY(450);
		loginButton.setPrefSize(w / 6, 50);

		rightScroll.setLayoutX(w / 6);
		rightScroll.setLayoutY(h / 12);
		rightScroll.setPrefSize(w - (w / 6), h - (h / 12) - 22);
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

	/**
	 * Creates the TopBar with standard size according to screen size.
	 *
	 * @param topPane The Pane which is to hold the TopBar
	 */
	public static void addTopBar(Pane topPane)
	{
		addViews(topPane, images + "/background.png", 0, 0, w, h / 12);
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h / 12);

		TextField addressField = new TextField(website);
		addressField.setId("addressField");
		stage.setScene(editScene);
		stage.setResizable(true);
		stage.setResizable(false);
		topPane.getChildren().add(addressField);
		addressField.setFocusTraversable(false);
		addressField.setEditable(false);
		addressField.setLayoutX(w / 12);
		addressField.setLayoutY(((h / 12) - addressField.getHeight()) / 2);
		addressField.setPrefWidth(w - (w / 6));

		System.out.println(addressField.getHeight());
		System.out.println(h / 12);


		ImageView buttons = new ImageView(new Image("application/library/images/buttons.png"));
		topPane.getChildren().add(buttons);
		buttons.setLayoutX((addressField.getLayoutX() - 78) / 2);
		buttons.setLayoutY(addressField.getLayoutY() + ((addressField.getHeight() - 14) / 2));

		ImageView lock = new ImageView(new Image("application/library/images/lock.png"));
		topPane.getChildren().add(lock);
		lock.setLayoutX(addressField.getLayoutX() + 20);
		lock.setLayoutY(addressField.getLayoutY() + ((addressField.getHeight() - 13) / 2));
	}
}