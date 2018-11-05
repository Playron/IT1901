package application.scene;

import java.util.ArrayList;
import java.util.Optional;

import application.database.Category;
import application.database.Content;
import application.database.CurrentUser;
import application.database.DB;
import application.database.Login;
import application.logic.Post;
import application.logic.Posts;
import application.logic.Usertype;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Home {

	private static Stage stage;

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
	 * Many changes have been bade to the Home.java class, to a point where updates is more difficult in some situations.
	 * Adding this in addition to a Runnable-object in the showHome-method.
	 */
	static String lastChange = null;
	
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

	public static void updateSearch(String newSearch)
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
	 * @author Niklas Sølvberg
	 */
	public static void addContentToPane(Pane contentPane, TextField addressField, String subSite)
	{
		contentPane.setPrefHeight(40 + (200 * Posts.getLabels(search).size()));
		addressField.setText(website + subSite + searchFull);
		specifyPostsToLabel(contentPane);
	}

	// TODO Cleanup of methods.

	/**
	 * This method makes sure only the correct content is shown.
	 *
	 * @param contentPane The Pane which should show the content.
	 */
	private static void specifyPostsToLabel(Pane contentPane)
	{
		int i = 0;
		switch (showContent)
		{
			case 0:
				for (Label label : Posts.getLabels(search))
				{
					addLabels(contentPane, label, 80, 40, 200, i);
					i++;
				}
				break;
			case 1:
				for (Post post : Posts.getPublishedPosts(search))
				{
					Label label = Posts.getPostLabel(post);
					addLabels(contentPane, label, 80, 40, 200, i);
					System.out.println("publishing " + i);
					if (CurrentUser.hasExecutiveEditorRights())
					{
						handleUnpublishingPublishedLabels(label, post);
						System.out.println("Executivity " + i);
					}
					i++;
				}
				break;
			case 2:
				ArrayList<Post> postList = Posts.getSubmittedPosts(search);
				ArrayList<Label> labelList = Posts.getSubmittedLabels(search);
				for (int j = 0; j < labelList.size(); j++)
				{
					addLabelsForEditors(contentPane, labelList.get(j), 80, 40, 200, i, postList.get(j));
					System.out.println(i);
					i++;
				}
				break;
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
	public static void addLabels(Pane contentPane, Label label, int x, int y, int dy, int i)
	{
		contentPane.getChildren().add(label);
		label.setLayoutX(x);
		label.setLayoutY(y + (dy * i));
	}
	
	/**
	 * @param contentPane is the container we want to add the label to
	 * @param label is the label we want to add
	 * @param x is the x-value
	 * @param y is the initial y-value
	 * @param dy is the change in y-value between labels
	 * @param i is the index of the label/post
	 * @param post is the post corresponding to the label.
	 * 
	 * @author Torleif Hensvold
	 * @author Niklas Sølvberg
	 */
	public static void addLabelsForEditors(Pane contentPane, Label label, int x, int y, int dy, int i, Post post)
	{
		contentPane.getChildren().add(label);
		label.setLayoutX(x);
		label.setLayoutY(y + (dy * i));
		if (post.getAssigned() == null || post.getAssigned().equals(CurrentUser.getUsername())) {
			label.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent me)
				{
					Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
					dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.setTitle("Make new post");
					dialog.setHeaderText(null);
					ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
					ButtonType publishButtonType = new ButtonType("Publish", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().setAll(submitButtonType, publishButtonType, ButtonType.CANCEL);
					
					Pane dialogPane = new Pane();
					dialogPane.setPrefSize(300, 350);
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
					CheckBox completeBox = new CheckBox("Mark post as complete");
					completeBox.setLayoutX(20);
					completeBox.setLayoutY(305);
					completeBox.setSelected(false);
					
					dialogPane.getChildren().setAll(headerLabel, headerField, contentLabel, contentArea, completeBox);
					dialog.getDialogPane().setContent(dialogPane);
					dialog.setResultConverter(dialogButton ->
					{
						ArrayList<String> list = new ArrayList<String>();
						if (dialogButton == publishButtonType)
						{
							list.add(headerField.getText());
							list.add(contentArea.getText());
							list.add("published");
							return list;
						}
						else if (dialogButton == submitButtonType) {
							list.add(headerField.getText());
							list.add(contentArea.getText());
							list.add("submitted");
							return list;
						}
						return null;
					});
					Optional<ArrayList<String>> result = dialog.showAndWait();
					result.ifPresent(text ->
					{
						Content.updateContent(post.getID(), text.get(0), text.get(1), text.get(2), CurrentUser.getUsername(), completeBox.isSelected());
					});
					lastChange = "submitted";
				}
			});
		}
		if (Content.isComplete(post.getID()) == true) {
			Label completeLabel = new Label("This post was marked as 'complete' by:\n\t" + post.getEditor());
			contentPane.getChildren().add(completeLabel);
			completeLabel.setLayoutX(x + 650);
			completeLabel.setLayoutY(y + (dy * i) + 140);
		}
		if (post.isAssigned()) {
			Label assignedLabel = new Label("Assigned to:\n\t" + post.getAssigned());
			contentPane.getChildren().add(assignedLabel);
			assignedLabel.setLayoutX(x + 650);
			assignedLabel.setLayoutY(y + (dy * i) + 80);
		}
		else {
			Button assignButton = new Button("Assign yourself!");
			contentPane.getChildren().add(assignButton);
			assignButton.setLayoutX(x + 650);
			assignButton.setLayoutY(y + (dy * i) + 80);
			assignButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
					alert.setTitle("Assign post");
					alert.setHeaderText(null);
					alert.setContentText("Are you sure you want to assign\nthis post to yourself?");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						Content.updateAssignedToMyself(post.getID());
					}
					lastChange = "submitted";
				}
			});
		}
	}

	/**
	 * @param contentPane  The Pane in which we want to add content
	 * @param addressField The address field of the scene.
	 * @author Torleif Hensvold
	 * @author Niklas Sølvberg
	 */
	public static void populateContent(Pane contentPane, TextField addressField)
	{
		if (showContent == 0)
		{
			addContentToPane(contentPane, addressField, "/all_content");
		} else if (showContent == 1)
		{
			addContentToPane(contentPane, addressField, "/published_content");
		} else if (showContent == 2)
		{
			addContentToPane(contentPane, addressField, "submitted_content");
		}
	}
	
	/**
	 * @param node is the Node that is being placed
	 * @param x is the x-value of the node's new placement
	 * @param y is the y-value of the node's new placement
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void place(Node node, Double x, Double y) {
		if (x != null)
			node.setLayoutX(x);
		if (y != null)
			node.setLayoutY(y);
	}
	
	/**
	 * @param region is the Region that is being placed
	 * @param x is the x-value of the region's new placement
	 * @param y is the y-value of the region's new placement
	 * @param w is the width of the region's new state
	 * @param h is the height of the region's new state
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void place(Region region, Double x, Double y, Double w, Double h) {
		if (x != null)
			region.setLayoutX(x);
		if (y != null)
			region.setLayoutY(y);
		if (w != null)
			region.setPrefWidth(w);
		if (h != null)
			region.setPrefHeight(h);
	}
	
	/**
	 * @param node is the node we want to determine the visibility of
	 * @param b is the value that determines the visibility
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void visible(Node node, boolean b) {
		node.setVisible(b);
		node.setDisable(!b);
	}
	
	/**
	 * @return an EventHandler that will fire when a user presses a button at the homescreen that says "Request new accesslevel"
	 */
	public static EventHandler<ActionEvent> accessLevelRequestEventHandler() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ChoiceDialog<String> dialog = new ChoiceDialog<String>(CurrentUser.getAccessLevelString(), Usertype.usertypes());
				dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
				dialog.setTitle("Accesslevel request");
				dialog.setHeaderText("Request new accesslevel");
				dialog.setContentText("Accesslevel: ");
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					if (!result.get().equals(CurrentUser.getAccessLevelString())) {
						Content.requestAccessLevel(Usertype.asChar(result.get()));
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
						alert.setTitle("Request");
						alert.setHeaderText("Request sent");
						alert.setContentText("A request to change your usertype from " + Usertype.accessLevelInSentence(CurrentUser.getAccessLevel())
						+ " to " + Usertype.accessLevelInSentence(Usertype.asChar(result.get())) + " has been sent!");
						alert.showAndWait();
					}
				}
			}
		};
		return eventHandler;
	}
	
	/**
	 * @return the EventHandler used for the button where you subscribe to users
	 * 
	 * @author Niklas Sølvberg
	 */
	public static EventHandler<ActionEvent> subscribeToUserEventHandler() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				TextInputDialog dialog = new TextInputDialog("subscribe to");
				dialog.setTitle("Subscribe");
				dialog.setHeaderText(null);
				dialog.setContentText("Enter user: ");
				Optional<String> result = dialog.showAndWait();
				result.ifPresent(username -> {
					if (!Content.isSubscribedTo(username) && Content.userIsSubscribable(username)) {
						Content.addSubscription(username);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Subscription");
						alert.setHeaderText(null);
						alert.setContentText("The user '" + username + "' has been added to your subscriptions!");
						alert.showAndWait();
					}
					else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Subscription");
						alert.setHeaderText(null);
						alert.setContentText("An error occured while trying to subscribe to '" + username + "'.");
						alert.showAndWait();
					}
				});
			}
		};
		return eventHandler;
	}

	/**
	 * @return the EventHandler used for the button where you subscribe to categories
	 * 
	 * @author Niklas Sølvberg
	 */
	public static EventHandler<ActionEvent> subscribeToCategoryEventHandler() {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				ArrayList<String> categories = Content.getCategories();
				ChoiceDialog<String> dialog = new ChoiceDialog<String>("", categories);
				dialog.setTitle("Subscribe");
				dialog.setHeaderText(null);
				dialog.setContentText("Choose category: ");
				Optional<String> result = dialog.showAndWait();
				result.ifPresent(category -> {
					if (!category.equals("") && !Content.isSubscribedTo(Content.getCategoryID(category))) {
						Content.addCategorySubscription(Content.getCategoryID(category));
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Subscription");
						alert.setHeaderText(null);
						alert.setContentText("The category '" + category + "' has been added to your subscriptions!");
						alert.showAndWait();
					}
					else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Subscription");
						alert.setHeaderText(null);
						alert.setContentText("An error occured while trying to subscribe to '" + category + "'.");
						alert.showAndWait();
					}
				});
			}
		};
		return eventHandler;
	}
	
	/**
	 * @param text is the address field at the homepage
	 * @param pane is the pane that the labels are being put into
	 * @return the EventHandler that are being used for showing the posts that are subscribed to
	 * 
	 * @author Niklas Sølvberg
	 */
	public static EventHandler<ActionEvent> showSubscribedEventHandler(TextField text, Pane pane) {
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				showContent = 1;
				text.setText(website + "/subscribed_content" + searchFull);
				pane.getChildren().clear();
				int i = 0;
				ArrayList<Label> labels = Posts.getSubscribedLabels(search);
				pane.setPrefHeight(40 + (200 * labels.size()));
				for (Label label : labels)
				{
					pane.getChildren().add(label);
					label.setLayoutX(80);
					label.setLayoutY(40 + (200 * i));
					i++;
				}
			}
		};
		return eventHandler;
	}

	/**
	 * Contains all the buttons and panes you can see on the homescreen,
	 * and this is where you would see your feed (content).
	 *
	 * @param staged is the primaryStage passed along from Main
	 * @param width is the width of the maximised stage
	 * @param height is the height of the maximised stage
	 *
	 * @author Niklas Sølvberg
	 * @author Torleif Hensvold
	 */
	public static void showHome(Stage staged, double width, double height)
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
		populateContent(contentPane, adressField);
		
		// Button not visible. Still here as the onAction property still has uses
		Button refreshButton = new Button("O");
		topPane.getChildren().add(refreshButton);
		refreshButton.setVisible(false);
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				contentPane.getChildren().clear();
				populateContent(contentPane, adressField);
			}
		});

		
		
		Button createButton = new Button("Create content");
		optionsPane.getChildren().add(createButton);
		if (!CurrentUser.hasAuthorRights())
			visible((Node) createButton, false);
		createButtonOnAction(createButton, contentPane, adressField);
		
		Button showAllButton = new Button("View all content");
		optionsPane.getChildren().add(showAllButton);
		if (!CurrentUser.hasCopyEditorRights())
			visible((Node) showAllButton, false);
		showAllButtonOnAction(showAllButton, adressField, contentPane);

		Button showSubmittedButton = new Button("View submitted content" + searchFull);
		optionsPane.getChildren().add(showSubmittedButton);
		if (!CurrentUser.hasCopyEditorRights())
			visible((Node) showSubmittedButton, false);
		showSubmittedButtonOnAction(showSubmittedButton, adressField, contentPane, showAllButton);

		Button showPublishedButton = new Button("View published content");
		optionsPane.getChildren().add(showPublishedButton);
		showPublishedButtonOnAction(showPublishedButton, adressField, contentPane);
		
		Button showSubscribedButton = new Button("View subscribed content");
		optionsPane.getChildren().add(showSubscribedButton);
		if (!CurrentUser.isRegistered())
			visible((Node) showSubscribedButton, false);
		showSubscribedButton.setOnAction(showSubscribedEventHandler(adressField, contentPane));
		
		Button createCategoriesButton = new Button("Create categories");
		optionsPane.getChildren().add(createCategoriesButton);
		if (!CurrentUser.hasExecutiveEditorRights())
			visible((Node) createCategoriesButton, false);
		createCategoriesButtonOnAction(createCategoriesButton);
		
		Button requestAccessLevelButton = new Button("Request new accesslevel");
		optionsPane.getChildren().add(requestAccessLevelButton);
		if (!CurrentUser.isRegistered())
			visible((Node) requestAccessLevelButton, false);
		requestAccessLevelButton.setOnAction(accessLevelRequestEventHandler());
		
		Button subscribeToUserButton = new Button("Subscribe to user");
		optionsPane.getChildren().add(subscribeToUserButton);
		if (!CurrentUser.isRegistered())
			visible((Node) subscribeToUserButton, false);
		subscribeToUserButton.setOnAction(subscribeToUserEventHandler());
		
		Button subscribeToCategoryButton = new Button("Subscribe to category");
		optionsPane.getChildren().add(subscribeToCategoryButton);
		if (!CurrentUser.isRegistered())
			visible((Node) subscribeToCategoryButton, false);
		subscribeToCategoryButton.setOnAction(subscribeToCategoryEventHandler());
		
		
		
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
		loginButton(loginButton, loggedInLabel);
		
		
		
		place((Region) topPane, 0.0, 0.0, w, h/12);
		place((Node) background, 0.0, 0.0);
		place((Region) refreshButton, 2*(w/42), ((h/12)-adressField.getHeight())/2, w/42, adressField.getHeight());
		place((Region) adressField, w/12, ((h / 12) - adressField.getHeight()) / 2, w - (w / 6), null);
		place((Node) buttons, (adressField.getLayoutX() - 78) / 2, adressField.getLayoutY() + ((adressField.getHeight() - 14) / 2));
		place((Node) lock, adressField.getLayoutX() + 20, adressField.getLayoutY() + ((adressField.getHeight() - 13) / 2));
		place((Region) optionsPane, 0.0, h/12, w/6, h-(h/12)-22);
		place((Region) loggedInLabel, 6.0, 6.0, null, null);
		place((Region) searchField, 5.0, 110.0, (w/6)-10, 30.0);
		place((Region) createButton, 0.0, 50.0, w/6, 40.0);
		place((Region) showAllButton, 0.0, 150.0, w/6, 40.0);
		place((Region) showSubmittedButton, 0.0, 190.0, w/6, 40.0);
		place((Region) showPublishedButton, 0.0, 230.0, w/6, 40.0);
		place((Region) showSubscribedButton, 0.0, 270.0, w/6, 40.0);
		place((Region) adminToolButton, 0.0, 340.0, w/6, 40.0);
		place((Region) createCategoriesButton, 0.0, 380.0, w/6, 40.0);
		place((Region) requestAccessLevelButton, 0.0, 450.0, w/6, 40.0);
		place((Region) subscribeToUserButton, 0.0, 520.0, w/6, 25.0);
		place((Region) subscribeToCategoryButton, 0.0, 545.0, w/6, 25.0);
		place((Region) loginButton, 0.0, 600.0, w/6, 50.0);
		place((Region) rightScroll, w/6, h/12, w - (w / 6), h - (h / 12) - 22);
		
		System.out.println("AnimationTimer initialized.");
		new AnimationTimer() {
			long prev = 0L;
			@Override
			public void handle(long now) {
				if (now - prev >= 16_000_000) {
					if (lastChange != null) {
						switch (lastChange) {
							case "submitted": showSubmittedButton.fire(); break;
							case "published": showPublishedButton.fire(); break;
							case "all": showAllButton.fire(); break;
						}
						lastChange = null;
					}
				}
			}
		}.start();
		System.out.println("AnimationTimer stopped.");
	}

	// TODO Add documentation to loginButton!

	/**
	 * @param loginButton
	 * @param loggedInLabel
	 */
	public static void loginButton(Button loginButton, Label loggedInLabel)
	{
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
					Home.showHome(stage, w, h);
				}
			}
		});
	}

	public static void showPublishedButtonOnAction(Button showPublishedButton, TextField addressField, Pane contentPane)
	{
		showPublishedButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 1;
				//addressField.setText(website + "/published_content" + searchFull);
				contentPane.getChildren().clear();
				populateContent(contentPane, addressField);
			}
		});
	}

	public static void createCategoriesButtonOnAction(Button createCategoriesButton)
	{
		createCategoriesButton.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent ae) {
						// Creating dialog
						Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
						dialog.setTitle("Ceate category");
						
						dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
						
						// creates the button for creating category
						ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
						dialog.getDialogPane().getButtonTypes().setAll(createButtonType, ButtonType.CANCEL);
						
						// sets size of pane
						Pane dialogPane = new Pane();
						dialogPane.setPrefSize(300, 300);
						
						//Text field where user can write new categories to commit
						TextField categoryName = new TextField();
						categoryName.setPromptText("Category");
						categoryName.setLayoutX(20);
						categoryName.setLayoutY(250);
						categoryName.setPrefSize(260, 25);
						
						
						//Field that shows the already existing categories 
						TextArea existingCategories = new TextArea();
						existingCategories.setLayoutX(20);
						existingCategories.setLayoutY(20);
						existingCategories.setPrefSize(260, 160);
						existingCategories.setWrapText(true);
						// setting the field to not editable
						existingCategories.setEditable(false);
						// adds the categories to the TextArea
						for (String string : Content.getCategories())
							existingCategories.appendText(string + "\n");
						
						// adds the different fields to the dialogpane
						dialogPane.getChildren().setAll(categoryName,existingCategories);
						
						//creates a node for the create button
						Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
						//sets disable until something is written in text field for new category
						createButton.setDisable(true);
						categoryName.textProperty().addListener((observable, oldValue, newValue) ->
						{
							createButton.setDisable(newValue.trim().isEmpty());
						});
						
						//setting the pane to the dialog
						dialog.getDialogPane().setContent(dialogPane);
						
						// Setting what happens when the create button is pushed
						dialog.setResultConverter(dialogButton ->
						{
							if (dialogButton == createButtonType)
							{
								ArrayList<String> list = new ArrayList<String>();
								list.add(categoryName.getText());
								return list;
							}
							return null;
						});
						
						
						//runs the action stated above and adds the return values into the array result
						Optional<ArrayList<String>> result = dialog.showAndWait();
						
						// if there is anything in the result stated above it runs what is stated below
						result.ifPresent(text ->
						{
							try {
								Category.createCategory(text.get(0));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
						//System.out.println(categoryName.getText());
					}
			
				});
	}
	
	public static void showSubmittedButtonOnAction(Button showSubmittedButton, TextField addressField, Pane contentPane, Button showAllButton)
	{
		showSubmittedButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 2;
				addressField.setText(website + "/submitted_content" + searchFull);
				contentPane.getChildren().clear();
				contentPane.setPrefHeight(40 + (200 * Posts.getSubmittedLabels(search).size()));
				populateContent(contentPane, addressField);
			}
		});
	}

	public static void showAllButtonOnAction(Button showAllButton, TextField addressField, Pane contentPane)
	{
		showAllButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				showContent = 0;
				//addressField.setText(website + "/all_content" + searchFull);
				contentPane.getChildren().clear();
				populateContent(contentPane, addressField);
			}
		});
	}

	public static void createButtonOnAction(Button createButton, Pane contentPane, TextField addressField)
	{
		createButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ae)
			{
				if (CurrentUser.hasAuthorRights())
				{
					Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
					dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.setTitle("Make new post");
					dialog.setHeaderText(null);
					ButtonType publishButtonType = new ButtonType("Publish", ButtonData.OK_DONE);
					ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().setAll(publishButtonType, submitButtonType, ButtonType.CANCEL);
					
					Pane dialogPane = new Pane();
					dialogPane.setPrefSize(300, 395);
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
					Label categoryLabel = new Label("Add categories:");
					categoryLabel.setLayoutX(20);
					categoryLabel.setLayoutY(295);
					Label feedbackLabel = new Label("Category successfully added!");
					feedbackLabel.setLayoutX(20);
					feedbackLabel.setLayoutY(355);
					feedbackLabel.setTextFill(Color.web("#0000ff"));
					feedbackLabel.setVisible(false);
					feedbackLabel.setFont(Font.font(10));
					ComboBox<String> categoryBox = new ComboBox<String>();
					categoryBox.setLayoutX(20);
					categoryBox.setLayoutY(325);
					categoryBox.setPrefSize(200, 25);
					categoryBox.setItems(FXCollections.observableArrayList(Content.getCategories()));
					categoryBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent me) {
							feedbackLabel.setVisible(false);
						}
					});
					Button addCategoryButton = new Button("Add");
					addCategoryButton.setLayoutX(220);
					addCategoryButton.setLayoutY(325);
					addCategoryButton.setPrefSize(60, 25);
					ObservableList<String> categories = FXCollections.observableArrayList();
					addCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent ae) {
							if (Content.getCategories().contains(categoryBox.getValue()) && !categories.contains(categoryBox.getValue()))
								categories.add(categoryBox.getValue());
						}
					});
					
					dialogPane.getChildren().setAll(headerLabel, headerField, contentLabel, contentArea, categoryBox, addCategoryButton, feedbackLabel);
					Node submitButton = dialog.getDialogPane().lookupButton(submitButtonType);
					submitButton.setDisable(true);
					categories.addListener((ListChangeListener<String>) change -> {
						feedbackLabel.setVisible(true);
					});
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
					dialog.setResultConverter(dialogButton ->
					{
						if (dialogButton == submitButtonType)
						{
							ArrayList<String> list = new ArrayList<String>();
							list.add(headerField.getText());
							list.add(contentArea.getText());
							list.add("submitted");
							return list;
						} else if (dialogButton == publishButtonType)
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
						Content.addContent(text.get(0), text.get(1), text.get(2), null);
						
						// This wont work if more than one post is created at the exact same time.
						// Which postID we retrieve will depend on the fact that it, in an ideal
						// situation, will be the last entry in the post-table.
						Content.addPostCategories(categories);
					});
					contentPane.getChildren().clear();
					populateContent(contentPane, addressField);
				}
			}
		});
	}
	
	private static void handleUnpublishingPublishedLabels(Label label, Post post)
	{
		label.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent me)
			{
				Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
				dialog.getDialogPane().getStylesheets().add("application/library/stylesheets/basic.css");
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.setTitle("Unpublish?");
				dialog.setHeaderText(null);
				ButtonType unpublishButtonType = new ButtonType("Unpublish", ButtonData.OK_DONE);
				dialog.getDialogPane().getButtonTypes().setAll(unpublishButtonType, ButtonType.CANCEL);
				
				Pane dialogPane = new Pane();
				dialogPane.setPrefSize(300, 350);
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
				dialog.getDialogPane().setContent(dialogPane);
				dialog.setResultConverter(dialogButton ->
				{
					ArrayList<String> list = new ArrayList<String>();
					if (dialogButton == unpublishButtonType)
					{
						list.add(headerField.getText());
						list.add(contentArea.getText());
						list.add("submitted");
						return list;
					}
					return null;
				});
				Optional<ArrayList<String>> result = dialog.showAndWait();
				result.ifPresent(text ->
				{
					Content.updateContent(post.getID(), text.get(0), text.get(1), text.get(2), CurrentUser.getUsername(), false);
				});
				lastChange = "submitted";
			}
		});
	}
}