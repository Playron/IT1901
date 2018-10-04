package application;

import application.database.CurrentUser;
import application.database.DB;
import application.scene.Home;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main.java should run when you start the program.
 * 
 * @author Niklas Sølvberg
 */
public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage stage = primaryStage;
		stage.setMaximized(true);
		
		Home.showHome(stage, 1, 1);
	}
	
	public static void main(String[] args) {
		
		System.out.println("Connecting to database...");
		DB.connect();
		System.out.println("Database connection established.");
		
		
//		FIXME: The next line of code is used for testing functionality until we have a working log-in system
//		
//		CurrentUser.setCurrentUser("testeditor", 'E');
//		CurrentUser.setCurrentUser("testadmin", 'A');
		
		
		System.out.println("\nApplication running...\n");
		launch(args);
		
		System.out.println("Database connection closing...");
		DB.disconnect();
		if (DB.connected())
			throw new IllegalStateException("Database connection could not close.");
		else
			System.out.println("Database connection closed.");
		
	}
	
}
