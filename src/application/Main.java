package application;

import application.database.DB;
import application.scene.EditorTool;
import application.scene.Home;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Main.java should run when you start the program.
 *
 * @author Niklas Sølvberg
 */
public class Main extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Stage stage = primaryStage;
		stage.setMaximized(true);
		//stage.show();
		//Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		//Home.showHome(stage, 1, 1);
		EditorTool.showEditTool(stage, stage.getWidth(), stage.getHeight());
		
		System.out.println("Out of Main.start");
	}
	
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Connecting to database...");
			DB.connect();
			System.out.println("Database connection established.");
			
			System.out.println("\nApplication running...\n");
			launch(args);
			
			System.out.println("Database connection closing...");
			disconnectDB();
			/*DB.disconnect();
			if (DB.connected())
			{
				throw new IllegalStateException("Database connection could not close.");
			}
			else
			{
				System.out.println("Database connection closed.");
			}*/
		} catch (Exception e)
		{
			if (DB.connected())
			{
				DB.disconnect();
				System.out.println("Database connection closed.");
			}
			else
			{
				System.out.println("No connection.");
			}
		}
		
		
	}

	public static void disconnectDB()
	{
		DB.disconnect();
		if (DB.connected())
		{
			throw new IllegalStateException("Database connection could not close.");
		} else
		{
			System.out.println("Database connection closed.");
		}
	}

}
