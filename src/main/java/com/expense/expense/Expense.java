package com.expense.expense;

import java.util.ArrayList;
import java.util.HashMap;

import com.expense.user.User;

public class Expense {
	private int id;
	private double amount;
	private String description;
	private String date;
	private String time;
	private String category;
	private String transaction_type;
	
	public Expense(int id, double amount, String date, String time,String description, String category, String transaction_type) {
		super();
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.time = time;
		this.category = category;
		this.transaction_type = transaction_type;
	}
	
	
	public String getTransaction_type() {
		return transaction_type;
	}


	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}


	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
	
	@Override
	public String toString() {
		return "Expense [id=" + id + ", amount=" + amount + ", description=" + description + ", date=" + date
				+ ", time=" + time + ", category=" + category + ", transaction_type=" + transaction_type + "]";
	}


	public static ArrayList<Expense> getAllExpense(User user)
	{
		return ExpenseDAO.getAllExpense(user);
	}
	
	
	
	public static HashMap<String, Double> getDashboardData(User user)
	{
		return ExpenseDAO.getDashboardData(user);
	}
	public static boolean addExpense(User user, double amount, String description, String category, String date, String time, String transaction_type)
	{
		return ExpenseDAO.addExpense(user, amount, description, category, date, time, transaction_type);
	}
	
	public static ArrayList<String> getCategories(User user)
	{
		return ExpenseDAO.getCategories(user);
	}
	public static ArrayList<Expense> getExpenseByDuration(User user, String duration)
	{
		return ExpenseDAO.getExpenseByDuration(user, duration);
	}
	
	public static Expense getById(User use, int id)
	{
		return ExpenseDAO.getById(use, id);
	}
	public static boolean deleteExpense(int id)
	{
		return ExpenseDAO.deleteById(id);
	}
	
	public static boolean editExpense(User user, double amount, String description, String category, String date, String time, String transaction_type, int id)
	{
		return ExpenseDAO.editExpense(user, amount, description, category, date, time, transaction_type, id);
	}
	
	public static ArrayList<Expense> findExpense(User user)
	{
		return new ArrayList<Expense>();
	}
	
}
