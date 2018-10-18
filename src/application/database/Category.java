package application.database;

public class Category
{

	//TODO create implementation for #28
	public static void createCategory(String categoryName) throws Exception
	{
		String query;
		if (categoryName.length() < 1)
		{
			throw new IllegalArgumentException("Category name must be at least 1 character long.");
		} else if (categoryName.length() > 100)
		{
			throw new IllegalArgumentException("Category name must be less than 100 characters long.");
		}
		try
		{
			query = "INSERT INTO categories (categoryName) VALUE (" + categoryName + ");";
			DB.insert(query);
		} catch (Exception e)
		{
			throw new Exception("CreateCategory in Category in database");
			// TODO Add better Exception message. Slightly dependant on DB.java methods.
		}
	}


	//TODO create implementation for #14
	public static void addCategoryToPost(String categoryName)
	{

	}

}
