package application.database;

import java.sql.ResultSet;

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
		else if (categoryExists(categoryName))
		{
			throw new IllegalArgumentException("Category already exists. Stop trying to create it!");
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
	
	/**
	 * @return ResultSet including all the rows in the table 'categories'
	 */
	public static ResultSet getCategories()
	{
		String query;
		query = "SELECT * FROM categories";
		return DB.select(query);
	}
	
	
	public static boolean categoryExists(String cat)
	{
		String query;
		query = "SELECT * FROM categories WHERE categoryName='" + cat + "'";
		try
		{
			return DB.select(query).first();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
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
			DB.disconnect();
			e.printStackTrace();
		}
		try
		{
			DB.connect();
			ResultSet rs = getCategories();
			rs.first();
			while (!rs.isAfterLast())
			{
				System.out.println(rs.getString(1) + " " + rs.getString(2));
				rs.next();
			}
			System.out.println("Expected true: " + categoryExists("School"));
			System.out.println("Expected false: " + categoryExists("ba"));
			DB.disconnect();
		} catch (Exception e)
		{
			DB.disconnect();
			e.printStackTrace();
		}
		
	}

}
