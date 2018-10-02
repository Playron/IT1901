package application;

import application.database.DB;
import application.scene.Home;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		DB.connect("jdbc:mysql://mysql02.it.ntnu.no:3306/niklaso_tdt4140?useSSL=false", "niklaso_tdt4140", "gruppe69");
		Stage stage = primaryStage;
		stage.setFullScreen(true);
		stage.setFullScreenExitHint("");
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				DB.disconnect();
			}
		});
		Home.showHome(stage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
