package application.scene;

import application.Main;
import application.database.Content;
import application.database.DB;
import application.logic.User;
import application.logic.Users;
import application.logic.Usertype;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class EditorTool
{

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
	static String website = "          https://www.contentmanagementsystem.com/Edit_tool";
	
	/**
	 * This will represent the width of the maximised stage
	 */
	static double w;
	
	/**
	 * This will represent the height of the maximised stage
	 */
	static double h;
	
	
	private static void addViews(Pane parentPane, String imagePath)
	{
		ImageView iv = new ImageView(new Image(imagePath));
		parentPane.getChildren().add(iv);
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
		System.out.println("Started Edit tool");

		Pane root = new Pane();                             // Creates a root in which we hang everything else in the scene
		Scene editScene = new Scene(root, width, height);   // Creates a new Scene using the root and given dimensions
		stage.setScene(editScene);                          // Instructs the single Stage to prepare the Scene
		stage.show();                                       // Instructs the single Stage to be shown. Necessary!

		editScene.getStylesheets().add(stylesheets + "/basic.css");

		escapeKeyHandler(editScene);                        // Handles the escape key

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
}