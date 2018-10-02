package application.scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Home {

	public static void showHome(Stage stage) {
		Pane root = new Pane();
		
		Pane topPane = new Pane();
		root.getChildren().add(topPane);
		
		TextField adressField = new TextField("www.contentmanagementsystem.com");
		topPane.getChildren().add(adressField);
		adressField.setFocusTraversable(false);
		
		Pane leftPane = new Pane();
		ScrollPane leftScroll = new ScrollPane(leftPane);
		leftScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		leftScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		root.getChildren().add(leftScroll);
		
		Pane rightPane = new Pane();
		ScrollPane rightScroll = new ScrollPane(rightPane);
		rightScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		root.getChildren().add(rightScroll);
		
		Button backButton = new Button("<=");
		topPane.getChildren().add(backButton);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				
			}
		});
		
		Button forwardButton = new Button("=>");
		topPane.getChildren().add(forwardButton);
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				
			}
		});
		
		Button refreshButton = new Button("O");
		topPane.getChildren().add(refreshButton);
		refreshButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				
			}
		});
		
		
		
		Scene scene = new Scene(root, 200, 200);
		scene.getStylesheets().add("application/library/stylesheets/basic.css");
		stage.setScene(scene);
		stage.show();
		
		refreshButton.requestFocus();
		
		
		
		double w = stage.getWidth();
		double h = stage.getHeight();
		
		topPane.setLayoutX(0);
		topPane.setLayoutY(0);
		topPane.setPrefSize(w, h/12);
		
		backButton.setLayoutX(0);
		backButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		backButton.setPrefSize(w/42, adressField.getHeight());
		
		forwardButton.setLayoutX(w/42);
		forwardButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		forwardButton.setPrefSize(w/42, adressField.getHeight());
		
		refreshButton.setLayoutX(2*(w/42));
		refreshButton.setLayoutY(((h/12)-adressField.getHeight())/2);
		refreshButton.setPrefSize(w/42, adressField.getHeight());
		
		adressField.setLayoutX(w/14);
		adressField.setLayoutY(((h/12)-adressField.getHeight())/2);
		adressField.setPrefWidth(w-(w/7));
		
		leftScroll.setLayoutX(0);
		leftScroll.setLayoutY(h/12);
		leftScroll.setPrefSize(w/6, h-(h/12));
		
		rightScroll.setLayoutX(w/6);
		rightScroll.setLayoutY(h/12);
		rightScroll.setPrefSize(w-(w/6), h-(h/12));
	}
	
}
