package application;

import application.database.CurrentUser;
import application.database.DB;
import application.scene.Home;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage stage = primaryStage;
		stage.setMaximized(true);
		
		Home.showHome(stage);
	}
	
	public static void main(String[] args) {
		DB.connect();
		
//		FIXME: The next line of code is used for testing functionality until we have a working log-in system
//		
		CurrentUser.setCurrentUser("testeditor", 'E');
		
		launch(args);
		DB.disconnect();
	}
	
}
