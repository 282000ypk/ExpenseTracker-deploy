package com.expense.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.expense.db.DBCon;

public class userDAO {
	
	private static Connection con;
	
	public userDAO()
	{
		userDAO.con = DBCon.getConnection();
	}
	
	public static String getSalt(User user) throws SQLException
	{
		userDAO.con = DBCon.getConnection();
		String salt = null;
		try
		{
			Statement st = userDAO.con.createStatement();
			ResultSet rs = st.executeQuery("select salt from et_login where email = '" + user.getEmail() + "'");
			rs.next();
			salt = rs.getString(1);
			return salt;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return salt;
		}
		
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static String getPassword(User user) throws SQLException
	{
		userDAO.con = DBCon.getConnection();
		String password = null;
		
		try
		{
			Statement st = userDAO.con.createStatement();
			ResultSet rs = st.executeQuery("select password from et_login where email = '" + user.getEmail() + "'");
			rs.next();
			password = rs.getString(1);
			return password;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return password;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static User getUserByEmail(String email)
	{
		userDAO.con = DBCon.getConnection();
		User user = null;
		try
		{
			Statement st = userDAO.con.createStatement();
			ResultSet rs = st.executeQuery("select * from et_user where email = '" + email + "'");
			if(rs.next())
			{
				user = new User();
				user.setId(rs.getString(1));
				user.setEmail(rs.getString(2));
				user.setName(rs.getString(3));
				user.setProfile_pic_url(rs.getString(4));
				user.setAction(rs.getString(5));
			}
			return user;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return user;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	public static boolean createLogin(User user, String password)
	{
		userDAO.con = DBCon.getConnection();
		String chrs;
		String salt = null;
		try
		{
			// code to generate salt
			chrs = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		    SecureRandom secureRandom = SecureRandom.getInstanceStrong();
		    // 9 is the length of the string you want
		    salt = secureRandom.ints(9, 0, chrs.length())
		    		.mapToObj(i -> chrs.charAt(i))
		    		.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
		    		.toString();
		    
		    //code to hash password with salt
		    password = password + salt;
			MessageDigest message = MessageDigest.getInstance("SHA-256");
			String hashed_password = "";
			
			for(byte i:message.digest(password.getBytes(StandardCharsets.UTF_8)))
			{
				hashed_password += String.format("%02X", i);
			}
			hashed_password = hashed_password.toLowerCase();
			
			// store password and slat to DB
			String query = "insert into et_login values(?,?,?,?)";
			PreparedStatement st = userDAO.con.prepareStatement(query);
			st.setString(1, user.getId());
			st.setString(2, user.getEmail());
			st.setString(3, hashed_password);
			st.setString(4, salt);
			
			if(0 != st.executeUpdate())
			{
				return true;
			}
			// if anything goes wrong retur false
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
			return false;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.print(e);
			return false;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	public static boolean create(User user) {
		
		try
		{
			userDAO.con = DBCon.getConnection();
			PreparedStatement st = userDAO.con.prepareStatement("insert into et_user values(?,?,?,?,?)");
			st.setString(1, user.getId());
			st.setString(2, user.getEmail());
			st.setString(3, user.getName());
			st.setString(4, user.getProfile_pic_url());
			st.setString(5, user.getAction());
			
			if(0 != st.executeUpdate())
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}

	public static boolean setAction(User user, String action) {
		try
		{
			userDAO.con = DBCon.getConnection();
			PreparedStatement st = userDAO.con.prepareStatement("update et_user set action = ? where email = ?");
			st.setString(1, action);
			st.setString(2, user.getEmail());
			
			if(0 != st.executeUpdate())
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
		
	}

	public static boolean updateLogin(User user, String one_time_password) {
		try
		{
			userDAO.con = DBCon.getConnection();
			PreparedStatement st = userDAO.con.prepareStatement("delete from et_login where email = ?");
			st.setString(1, user.getEmail());
			st.executeUpdate();
			if(userDAO.createLogin(user, one_time_password))
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}

	public static void updateprofile(String fname, User user) 
	{
		try
		{
			userDAO.con = DBCon.getConnection();
			PreparedStatement st = userDAO.con.prepareStatement("update et_user set profile_pic_url = ? where id = ?");
			st.setString(1, fname);
			st.setString(2, user.getId());
			st.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			DBCon.close();
			try
			{
				if(userDAO.con!=null)
				{
					userDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
		
	}
}
