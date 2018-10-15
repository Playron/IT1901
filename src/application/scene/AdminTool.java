package application.scene;

import java.util.ArrayList;
import java.util.Optional;

import application.database.Content;
import application.database.DB;
import application.logic.User;
import application.logic.Users;
import application.logic.Usertype;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminTool
{

	/**
	 * This is the name of the website. This will show up in the adressbar.
	 */
	static String website = "          https://www.contentmanagementsystem.com/admin_tool";

	/**
	 * List that contains all different usertypes
	 */
	static ArrayList<Character> usertypes = Usertype.usertypes();

	/**
	 * Used for iterations
	 */
	static int i;

	/**
	 * Contains all the buttons and panes you can see in the admin tool,
	 * and this is where an admin would assign and manage the roles of users on the site.
	 *
	 * @param stage is the primaryStage passed along from Main
	 * @param w     is the width of the maximised stage
	 * @param h     is the height of the maximised stage
	 * @author Niklas Sølvberg
	 */
	public static void showAdminTool(Stage stage, double w, double h)
	{

		Pane root = new Pane();

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
		i = 0;
		for (Label label : Users.getLabelsNotCurrent())
		{
			usersPane.getChildren().add(label);
			label.setLayoutX(200);
			label.setLayoutY(160 + (60 * i));
			label.setFont(Font.font("Andale Mono", 16));
			label.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent me)
				{
					User user = Users.getUsersNotCurrent().get(usersPane.getChildren().indexOf(label));
					ChoiceDialog<Character> dialog = new ChoiceDialog<Character>(user.getUsertype(), usertypes);
					dialog.setTitle(null);
					dialog.setHeaderText("User: " + user.getUsername());
					dialog.setContentText("Set access level: ");
					Optional<Character> result = dialog.showAndWait();
					result.ifPresent(type ->
					{
						if (type != ' ')
						{
							Content.updateUser(user.getUsername(), type);
							AdminTool.showAdminTool(stage, w, h);
						}
					});
				}
			});
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

	}
	
}
