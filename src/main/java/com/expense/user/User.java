package com.expense.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class User {
	
	private String id;
	private String name;
	private String email;
	private String profile_pic_url;
	private String action;
	
	public User()
	{
		this.id = null;
		this.name = null;
		this.email = null;
		this.profile_pic_url = null;
		this.action = null;
	}
	
	public static boolean create(String id, String name, String email, String profile_pic_url, String action)
	{
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setEmail(email);
		user.setProfile_pic_url(profile_pic_url);
		user.setAction(action);
		
		if(userDAO.create(user))
			return true;
		return false;
	}
	
	
	public String getAction() {
		return (action == null)? "" : action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public static boolean create(String id, String name, String email, String profile_pic_url, String password, String action)
	{
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setEmail(email);
		user.setProfile_pic_url(profile_pic_url);
		user.setAction(action);
		
		if(userDAO.create(user) && userDAO.createLogin(user, password))
			return true;
		return false;
	}
	
	public static User getbyemail(String email)
	{
		User user = null;
		try
		{
			user = userDAO.getUserByEmail(email);
			return user;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return user;
		}
	}
	
	public boolean authenticate(String password)
	{
		String salt = null;
		String user_password = null;
		try
		{
			salt = userDAO.getSalt(this);
			user_password = userDAO.getPassword(this);
			
			password = password + salt;
			MessageDigest message = MessageDigest.getInstance("SHA-256");
			String hashed_password = "";
			
			for(byte i:message.digest(password.getBytes(StandardCharsets.UTF_8)))
			{
				hashed_password += String.format("%02X", i);
			}
			
			hashed_password = hashed_password.toLowerCase();
			
			if(user_password.equals(hashed_password))
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfile_pic_url() {
		return profile_pic_url;
	}

	public void setProfile_pic_url(String profile_pic_url) {
		this.profile_pic_url = profile_pic_url;
	}

	public void updateprofile(String fname, User user) {
		
		userDAO.updateprofile(fname, user);
	}
}
