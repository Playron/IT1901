package application.scene;

import application.database.Content;
import application.database.DB;
import application.database.Login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScreen {

	/**
	 * This is the name of the website when logging in. This will show up in the adressbar.
	 */
	static String websiteLogin = "          https://www.contentmanagementsystem.com/login";
	
	/**
	 * This is the name of the website when registering. This will show up in the adressbar.
	 */
	static String websiteRegister = "          https://www.contentmanagementsystem.com/register";
	
	/**
	 * This is the height of which the content of this scene will be adjusted by.
	 */
	static float y = 300;
	
	/**
	 * This is the width of elements with fixed width.
	 */
	static float x = 220;
	
	/**
	 * Contains all the buttons and panes you can see in the login screen,
	 * and this is where a user would log in to the site or register a new user.
	 * 
	 * @param stage is the primaryStage passed along from Main
	 * @param w is the width of the maximised stage
	 * @param h is the height of the maximised stage
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void showLoginScreen(Stage stage, double w, double h) {
		
		Pane root = new Pane();
		
		Pane topPane = new Pane();
		root.getChildren().add(topPane);
		
		ImageView background = new ImageView(new Image("application/library/images/background.png"));
		topPane.getChildren().add(background);
		
		ImageView buttons = new ImageView(new Image("application/library/images/buttons.png"));
		topPane.getChildren().add(buttons);
		
		TextField adressField = new TextField(websiteLogin);
		topPane.getChildren().add(adressField);
		adressField.setFocusTraversable(false);
		adressField.setEditable(false);
		
		ImageView lock = new ImageView(new Image("application/library/images/lock.png"));
		topPane.getChildren().add(lock);
		
		
		
		Pane loginPane = new Pane();
		root.getChildren().add(loginPane);
		
		ImageView loginBackground = new ImageView(new Image("application/library/images/line.png"));
		loginPane.getChildren().add(loginBackground);
		
		ImageView loginLogo = new ImageView(new Image("application/library/images/logo.png"));
		loginPane.getChildren().add(loginLogo);
		
		TextField loginUsernameField = new TextField();
		loginPane.getChildren().add(loginUsernameField);
		loginUsernameField.setPromptText("Username");
		
		PasswordField loginPasswordField = new PasswordField();
		loginPane.getChildren().add(loginPasswordField);
		loginPasswordField.setPromptText("Password");
		
		Label invalidLoginLabel = new Label("Invalid login!");
		loginPane.getChildren().add(invalidLoginLabel);
		invalidLoginLabel.setTextFill(Color.web("#ff0000"));
		invalidLoginLabel.setVisible(false);
		
		Button loginButton = new Button("Login");
		loginPane.getChildren().add(loginButton);
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (Login.isValidLogin(loginUsernameField.getText(), loginPasswordField.getText())) {
					Login.login(loginUsernameField.getText());
					Home.showHome(stage, w, h);
				}
				else
					invalidLoginLabel.setVisible(true);
			}
		});
		
		Button loginCancelButton = new Button("Cancel");
		loginPane.getChildren().add(loginCancelButton);
		loginCancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				Home.showHome(stage, w, h);
			}
		});
		
		
		
		Pane registerPane = new Pane();
		root.getChildren().add(registerPane);
		
		ImageView registerBackground = new ImageView(new Image("application/library/images/line.png"));
		registerPane.getChildren().add(registerBackground);
		
		ImageView registerLogo = new ImageView(new Image("application/library/images/logo.png"));
		registerPane.getChildren().add(registerLogo);
		
		TextField registerUsernameField = new TextField();
		registerPane.getChildren().add(registerUsernameField);
		registerUsernameField.setPromptText("Username");
		
		PasswordField registerPasswordField = new PasswordField();
		registerPane.getChildren().add(registerPasswordField);
		registerPasswordField.setPromptText("Password");
		
		PasswordField registerConfirmField = new PasswordField();
		registerPane.getChildren().add(registerConfirmField);
		registerConfirmField.setPromptText("Confirm password");
		
		Label invalidRegisterLabel = new Label("Invalid registration!");
		registerPane.getChildren().add(invalidRegisterLabel);
		invalidRegisterLabel.setTextFill(Color.web("#ff0000"));
		invalidRegisterLabel.setVisible(false);
		
		Button registerButton = new Button("Register");
		registerPane.getChildren().add(registerButton);
		registerButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (registerPasswordField.getText().equals(registerConfirmField.getText()) && registerPasswordField.getText().length() > 5 && registerUsernameField.getText().length() > 5) {
					try {
						Content.createUser(registerUsernameField.getText(), registerPasswordField.getText());
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle(null);
						alert.setHeaderText(null);
						alert.setContentText("User registration was successful!");
						alert.showAndWait();
						root.getChildren().remove(registerPane);
						root.getChildren().add(loginPane);
					}
					catch (IllegalArgumentException e) {
						invalidRegisterLabel.setVisible(true);
					}
					// TODO Add clearer message for password and username, or clarify rules for password.
				}
				else
					invalidRegisterLabel.setVisible(true);
			}
		});
		
		Button registerCancelButton = new Button("Cancel");
		registerPane.getChildren().add(registerCancelButton);
		registerCancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				Home.showHome(stage, w, h);
			}
		});
		
		
		
		Hyperlink registerHyperlink = new Hyperlink("Don't have a user? Register here");
		loginPane.getChildren().add(registerHyperlink);
		registerHyperlink.setTextFill(Color.web("#0000ff"));
		registerHyperlink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				loginUsernameField.setText("");
				loginPasswordField.setText("");
				invalidLoginLabel.setVisible(false);
				registerHyperlink.setTextFill(Color.web("#0000ff"));
				root.getChildren().remove(loginPane);
				root.getChildren().add(registerPane);
				registerButton.requestFocus();
				adressField.setText(websiteRegister);
			}
		});
		registerHyperlink.setFocusTraversable(false);
		
		Hyperlink loginHyperlink = new Hyperlink("Already have a user? Log in here");
		registerPane.getChildren().add(loginHyperlink);
		loginHyperlink.setTextFill(Color.web("#0000ff"));
		loginHyperlink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				registerUsernameField.setText("");
				registerPasswordField.setText("");
				registerConfirmField.setText("");
				invalidRegisterLabel.setVisible(false);
				loginHyperlink.setTextFill(Color.web("#0000ff"));
				root.getChildren().remove(registerPane);
				root.getChildren().add(loginPane);
				loginButton.requestFocus();
				loginHyperlink.setLayoutX((w-loginHyperlink.getWidth())/2);
				loginHyperlink.setLayoutY(y+180);
				adressField.setText(websiteLogin);
			}
		});
		loginHyperlink.setFocusTraversable(false);
		
		
		
		
		
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
		
		
		
		loginPane.setLayoutX(0);
		loginPane.setLayoutY(h/12);
		loginPane.setPrefSize(w, h-(h/12));

		loginBackground.setLayoutX(0);
		loginBackground.setLayoutY(0);
		
		loginLogo.setLayoutX((w-411)/2);
		loginLogo.setLayoutY(y-200);
		
		loginUsernameField.setLayoutX((w-x)/2);
		loginUsernameField.setLayoutY(y);
		loginUsernameField.setPrefSize(x, 25);
		
		loginPasswordField.setLayoutX((w-x)/2);
		loginPasswordField.setLayoutY(y+50);
		loginPasswordField.setPrefSize(x, 25);
		
		loginButton.setLayoutX((w-x)/2);
		loginButton.setLayoutY(y+100);
		loginButton.setPrefSize(x, 25);
		
		loginCancelButton.setLayoutX((w-x)/2);
		loginCancelButton.setLayoutY(y+130);
		loginCancelButton.setPrefSize(x, 25);
		
		registerHyperlink.setLayoutX((w-registerHyperlink.getWidth())/2);
		registerHyperlink.setLayoutY(y+180);
		
		invalidLoginLabel.setLayoutX((w-invalidLoginLabel.getWidth())/2);
		invalidLoginLabel.setLayoutY(y+230);
		
		
		
		root.getChildren().remove(registerPane);
		
		registerPane.setLayoutX(0);
		registerPane.setLayoutY(h/12);
		registerPane.setPrefSize(w, h-(h/12));

		registerBackground.setLayoutX(0);
		registerBackground.setLayoutY(0);
		
		registerLogo.setLayoutX((w-411)/2);
		registerLogo.setLayoutY(y-200);
		
		registerUsernameField.setLayoutX((w-x)/2);
		registerUsernameField.setLayoutY(y-50);
		registerUsernameField.setPrefSize(x, 25);
		
		registerPasswordField.setLayoutX((w-x)/2);
		registerPasswordField.setLayoutY(y);
		registerPasswordField.setPrefSize(x, 25);
		
		registerConfirmField.setLayoutX((w-x)/2);
		registerConfirmField.setLayoutY(y+50);
		registerConfirmField.setPrefSize(x, 25);
		
		registerButton.setLayoutX((w-x)/2);
		registerButton.setLayoutY(y+100);
		registerButton.setPrefSize(x, 25);
		
		registerCancelButton.setLayoutX((w-x)/2);
		registerCancelButton.setLayoutY(y+130);
		registerCancelButton.setPrefSize(x, 25);
		
		loginHyperlink.setLayoutX((w-loginHyperlink.getWidth())/2);
		loginHyperlink.setLayoutY(y+180);
		
		invalidRegisterLabel.setLayoutX((w-invalidRegisterLabel.getWidth())/2);
		invalidRegisterLabel.setLayoutY(y+230);
		
		
		
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h/12);

		background.setLayoutX(0);
		background.setLayoutY(0);
		
		adressField.setLayoutX(w/12);
		adressField.setLayoutY(((h/12)-adressField.getHeight())/2);
		adressField.setPrefWidth(w-(w/6));

		buttons.setLayoutX((adressField.getLayoutX()-78) / 2);
		buttons.setLayoutY(adressField.getLayoutY()+((adressField.getHeight()-14)/2));
		
		lock.setLayoutX(adressField.getLayoutX()+20);
		lock.setLayoutY(adressField.getLayoutY()+((adressField.getHeight()-13)/2));
		
		loginButton.requestFocus();
		
	}
	
}
