package com.expense.db;

import java.sql.Connection;
import java.sql.DriverManager;

import Credentials.DatabaseCredentials;

public class DBCon {
	
	private static DatabaseCredentials db_cred = new DatabaseCredentials();
	private static String url = "jdbc:postgresql://"+ db_cred.getHost() + "/" + db_cred.getDatabase();
	private static String user_name = db_cred.getUser();
	private static String password = db_cred.getPassword();
	private static Connection con = null;
	
	
	
	public static Connection getConnection()
	{
		try
		{
			
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(DBCon.url, DBCon.user_name, DBCon.password);
			return con;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return con;
		}
	}
	
	public static boolean close()
	{
		try
		{
			if(DBCon.con!=null)
			{
				DBCon.con.close();
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}		
	}
}





