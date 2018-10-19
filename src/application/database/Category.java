package application.database;

public class Category
{

	//TODO create implementation for #28

	/**
	 * @param categoryName The String that corresponds to the categoryName we want to create.
	 * @throws Exception Currently a standard Exception that indicates method name and class
	 * @author Torleif Hensvold
	 */
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
			query = "INSERT INTO categories (categoryName) VALUE (\"" + categoryName + "\");";
			//System.out.println(query);
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

	public static void main(String[] args)
	{
		try
		{
			DB.connect();
			//System.out.println(DB.select("SELECT * FROM categories WHERE categoryName='test'").first());
			if (!DB.select("SELECT * FROM categories WHERE categoryName='test'").first())
			{
				createCategory("test");
				System.out.println("Created category 'test'.");
			}
			else
			{
				DB.delete("DELETE FROM categories WHERE categoryName = 'test'");
				System.out.println("Deleted category 'test'.");
			}
			DB.disconnect();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
