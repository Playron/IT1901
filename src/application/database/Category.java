package application.database;

import java.sql.SQLException;

public class Category
{

	//TODO create implementation for #28
	public static void createCategory(String categoryName)
	{
		String query;
		//if (categoryName.length()>=1 && categoryName.length()<=100)
		try
		{
			query = "INSERT INTO categories (categoryName) VALUE (" + categoryName + ");";
			DB.insert(query);
		} catch (SQLException se)
		{

		}
	}


	//TODO create implementation for #14
	public static void addCategoryToPost(String categoryName)
	{

	}

}
