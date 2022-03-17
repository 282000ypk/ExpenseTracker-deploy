package com.expense.expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.expense.db.DBCon;
import com.expense.user.User;
import com.expense.expense.ExpenseDAO;

public class ExpenseDAO {
	
	private static Connection con = DBCon.getConnection();
	
	public static ArrayList<Expense> getAllExpense(User user) 
	{
		ExpenseDAO.con = DBCon.getConnection();
		ArrayList<Expense> allexpense = new ArrayList<Expense>();
		try
		{
			Statement st = ExpenseDAO.con.createStatement();
			Expense expense = null;
			ResultSet rs = st.executeQuery("select id, amount, date, time, description, category, transaction_type from et_expense where user_id = '"+ user.getId() +"' order by date desc, time desc");
			while(rs.next())
			{
				int id = Integer.parseInt(rs.getString(1));
				double amount = Double.parseDouble(rs.getString(2));
				String date = rs.getString(3);
				String time = rs.getString(4);
				String description = rs.getString(5);
				String category = rs.getString(6);
				String transaction_type = rs.getString(7);
				expense = new Expense(id, amount, date, time, description, category, transaction_type);
				
				allexpense.add(expense);
			}
			return allexpense;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return allexpense;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	
	public static HashMap<String, Double> getDashboardData(User user)
	{
		ExpenseDAO.con = DBCon.getConnection();
		
		String debit_for_month_query = "select sum(amount) from et_expense where user_id = '" + user.getId() + "' and transaction_type = 'debit' and date between date_trunc('month', current_date) and date_trunc('month', current_date) + interval '1 month'";
		String credit_for_month_query = "select sum(amount) from et_expense where user_id = '" + user.getId() + "' and transaction_type = 'credit' and date between date_trunc('month', current_date) and date_trunc('month', current_date) + interval '1 month'";
		String debit_for_week_query = "select sum(amount) from et_expense where user_id = '" + user.getId() + "' and transaction_type = 'debit' and date between date_trunc('week', current_date) and date_trunc('week', current_date) + interval '1 week'";
		String credit_for_week_query = "select sum(amount) from et_expense where user_id = '" + user.getId() + "' and transaction_type = 'credit' and date between date_trunc('week', current_date) and date_trunc('week', current_date) + interval '1 week'";
		String debit_for_today_query = "select sum(amount) from et_expense where user_id = '" + user.getId() + "' and transaction_type = 'debit' and date = current_date";
		String credit_for_today_query = "select sum(amount) from et_expense where user_id = '" + user.getId() + "' and transaction_type = 'credit' and date = current_date";
		
		HashMap<String, Double> dashboard_data = new HashMap<String, Double>();
		try
		{
			Statement st = ExpenseDAO.con.createStatement();
			ResultSet rs;
			
			rs = st.executeQuery(debit_for_month_query);
			dashboard_data.put("debit_for_month", (rs.next()) ? rs.getDouble(1): 0);
			
			rs = st.executeQuery(credit_for_month_query);
			dashboard_data.put("credit_for_month", (rs.next()) ? rs.getDouble(1): 0);
			
			rs = st.executeQuery(debit_for_week_query);
			dashboard_data.put("debit_for_week", (rs.next()) ? rs.getDouble(1): 0);
			
			rs = st.executeQuery(credit_for_week_query);
			dashboard_data.put("credit_for_week", (rs.next()) ? rs.getDouble(1): 0);
			
			rs = st.executeQuery(debit_for_today_query);
			dashboard_data.put("debit_for_today", (rs.next()) ? rs.getDouble(1): 0);
			
			rs = st.executeQuery(credit_for_today_query);
			dashboard_data.put("credit_for_today", (rs.next()) ? rs.getDouble(1): 0);
			
			return dashboard_data;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return dashboard_data;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static boolean addExpense(User user, double amount, String description, String category, String date, String time, String transaction_type) 
	{
		ExpenseDAO.con = DBCon.getConnection();
		try
		{
			String Query = "insert into et_expense values(default, ?,'" + date + "','" + time + "',?,?,?,?)";
			PreparedStatement st = ExpenseDAO.con.prepareStatement(Query);
			st.setDouble(1, amount);
			st.setString(2, description);
			st.setString(3, category);
			st.setString(4, user.getId());
			st.setString(5, transaction_type);
			
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
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static ArrayList<String> getCategories(User user)
	{
		ArrayList<String> categories = new ArrayList<String>();
		
		try
		{
			ExpenseDAO.con = DBCon.getConnection();
			Statement st = ExpenseDAO.con.createStatement();
			ResultSet rs = st.executeQuery("select distinct category from et_expense where user_id = '" + user.getId() + "' union select * from default_categories");
			while(rs.next())
			{
				categories.add(rs.getString(1));
			}
			return categories;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return categories;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static ArrayList<Expense> getExpenseByDuration(User user, String duration)
	{
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense;
		try
		{
			ExpenseDAO.con = DBCon.getConnection();
			Statement st = ExpenseDAO.con.createStatement();
			String query;
			if(duration.equals("day"))
			{
				query = "select id, amount, date, time, description, category, transaction_type from et_expense where date = current_date and user_id = '" + user.getId() + "' order by date desc, time desc";
			}
			else if(duration.equals("week"))
			{
				query = "select id, amount, date, time, description, category, transaction_type from et_expense where date between date_trunc('week', current_date) and date_trunc('week', current_date) + interval '1 week' and user_id = '" + user.getId() + "' order by date desc, time desc;";
			}
			else if(duration.equals("month"))
			{
				query = "select id, amount, date, time, description, category, transaction_type from et_expense where date between date_trunc('month', current_date) and date_trunc('month', current_date) + interval '1 month' and user_id = '" + user.getId() + "' order by date desc, time desc;";
			}
			else if(duration.equals("last_three_month"))
			{
				query = "select id, amount, date, time, description, category, transaction_type from et_expense where date between date_trunc('month', current_date) - interval '3 month' and date_trunc('month', current_date) + interval '1 month' and user_id = '" + user.getId() + "' order by date desc, time desc;";
			}
			else if(duration.equals("last_six_month"))
			{
				query = "select id, amount, date, time, description, category, transaction_type from et_expense where date between date_trunc('month', current_date) - interval '6 month' and date_trunc('month', current_date) + interval '1 month' and user_id = '" + user.getId() + "' order by date desc, time desc;";
			}
			else
			{
				query = "select id, amount, date, time, description, category, transaction_type from et_expense where user_id = '"+ user.getId() +"' order by date desc, time desc";
			}
			ResultSet rs = st.executeQuery(query);
			while(rs.next())
			{
				int id = Integer.parseInt(rs.getString(1));
				double amount = Double.parseDouble(rs.getString(2));
				String date = rs.getString(3);
				String time = rs.getString(4);
				String description = rs.getString(5);
				String category = rs.getString(6);
				String transaction_type = rs.getString(7);
				expense = new Expense(id, amount, date, time, description, category, transaction_type);
				
				expenses.add(expense);
				
			}
			return  expenses;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return  expenses;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static Expense getById(User user, int id)
	{
		Expense expense = null;
		try
		{
			ExpenseDAO.con = DBCon.getConnection();
			Statement st = ExpenseDAO.con.createStatement();
			String query = "select id, amount, date, time, description, category, transaction_type from et_expense where id = " + id;
			ResultSet rs = st.executeQuery(query);
			if(rs.next())
			{
				int eid = Integer.parseInt(rs.getString(1));
				double amount = Double.parseDouble(rs.getString(2));
				String date = rs.getString(3);
				String time = rs.getString(4);
				String description = rs.getString(5);
				String category = rs.getString(6);
				String transaction_type = rs.getString(7);
				expense = new Expense(eid, amount, date, time, description, category, transaction_type);
			}
			return  expense;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return  expense;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
	
	public static boolean deleteById(int id)
	{
		try
		{
			ExpenseDAO.con = DBCon.getConnection();
			String query = "delete from et_expense where id = ?";
			PreparedStatement st = ExpenseDAO.con.prepareStatement(query);
			st.setInt(1, id);
			if(st.executeUpdate() == 1)
			{
				return true;
			}
			return  false;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return  false;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}

	public static boolean editExpense(User user, double amount, String description, String category, String date,String time, String transaction_type, int id) {
		try
		{
			ExpenseDAO.con = DBCon.getConnection();
			// update et_expense set amount = ? ,description = ?, category = ?, date = ?, time = ?, transaction_type = ?  where id = ? and user_id = ?
			String query = "update et_expense set amount = ? ,description = ?, category = ?, date = '"+ date +"', time = '"+ time +"', transaction_type = ?  where id = ? and user_id = ?";
			PreparedStatement st = ExpenseDAO.con.prepareStatement(query);
			st.setDouble(1, amount);
			st.setString(2, description);
			st.setString(3, category);
			st.setString(4, transaction_type);
			st.setInt(5, id);
			st.setString(6, user.getId());
			if(st.executeUpdate() == 1)
			{
				return true;
			}
			return  false;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return  false;
		}
		finally
		{
			DBCon.close();
			try
			{
				if(ExpenseDAO.con!=null)
				{
					ExpenseDAO.con.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}	
		}
	}
} 
