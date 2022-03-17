package com.expense.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.expense.expense.Expense;
import com.expense.user.User;

@WebServlet("/Dashboard/*")
public class DashboardController extends HttpServlet {

	public void Search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("title", "Search Transactions");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
		dispatcher.forward(request, response);
		return;
	}
	
	public void Overview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setAttribute("title", "Overview of Transactions");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/overview.jsp");
		dispatcher.forward(request, response);
		return;
	}
	
	public void Newtransaction(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException
	{
		request.setAttribute("title", "Add New Transaction");
		ArrayList<String> categories = Expense.getCategories(user);
		request.setAttribute("categories", categories);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/newtransaction.jsp");
		dispatcher.forward(request, response);
		return;
	}
	
	public void Dashboard(HttpServletRequest request, HttpServletResponse response, User user) throws ServletException, IOException
	{
		request.setAttribute("title", "DashBoard");
		HashMap<String, Double> dashboard_data = Expense.getDashboardData(user);
		request.setAttribute("dashboard_data", dashboard_data);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/dashboard.jsp");
		dispatcher.forward(request, response);
		return;
	}
	
	public void Sendback(HttpServletRequest request, HttpServletResponse response, String path, User user) throws ServletException, IOException
	{
		if(path.equals("/Search"))
		{
			Search(request, response);
		}
		else if(path.equals("/Overview"))
		{
			Overview(request, response);
		}
		else if(path.equals("/Newtransaction"))
		{
			Newtransaction(request, response, user);
		}
		else
		{
			Dashboard(request, response, user);
		}
	}
	
	public void Error(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
		dispatcher.forward(request, response);
		return;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession httpsession = request.getSession(false);
		try 
		{
			if(httpsession != null && httpsession.getAttribute("user") == null)
			{
				response.sendRedirect("../Login/");
				return;
			}
			else
			{
				User user = (User) httpsession.getAttribute("user");
				request.setAttribute("user", user);
				String path = (request.getPathInfo() != null) ? request.getPathInfo() : "";
				
				if(path.equals("/History"))
				{
					request.setAttribute("title", "Transaction History");
					String duration = (request.getParameter("duration") == null)? "day" : (request.getParameter("duration"));
					String key = (request.getParameter("key") == null)? "" : (request.getParameter("key"));
					ArrayList<Expense> allexpense = Expense.getExpenseByDuration(user, duration);
					Iterator<Expense> iterator = allexpense.iterator();
					while(iterator.hasNext() && !key.equals(""))
					{
						if(!iterator.next().toString().toLowerCase().contains(key.toLowerCase()))
						{
							iterator.remove();
						}
					}
					
					request.setAttribute("transactions", allexpense);
					duration = (duration.equals("day"))? "Today": duration;
					duration = (duration.equals("week"))? "This Week": duration;
					duration = (duration.equals("month"))? "This Month": duration;
					request.setAttribute("duartion", duration);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/history.jsp");
					dispatcher.forward(request, response);
					return;
				}
				if(path.equals("/Sendback"))
				{
					Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
					return;
				}
				if(path.equals("/Search"))
				{
					httpsession.setAttribute("previous_path", path);
					Search(request, response);
					return;
				}
				
				else if(path.equals("/Overview"))
				{
					httpsession.setAttribute("previous_path", path);
					Overview(request, response);
					return;
				}
				else if(path.equals("/Newtransaction"))
				{
					Newtransaction(request, response, user);
					return;
				}
				else if(path.equals("/Addtransaction"))
				{
					// retrive attributes
					
					double amount = (request.getParameter("et_amount") == null) ? 0 : (Double.parseDouble((String) request.getParameter("et_amount")));
					String description = (request.getParameter("et_description") == null) ? "" : (String) request.getParameter("et_description");
					String category = (request.getParameter("et_category") == null)? "" : (String) request.getParameter("et_category");
					String transaction_type = (request.getParameter("et_transactiontype") == null) ? "" : (String) request.getParameter("et_transactiontype");
					String date = (request.getParameter("et_date") == null) ? "current_date" : (String) request.getParameter("et_date");
					String time = (request.getParameter("et_time") == null) ? "current_time" : (String) request.getParameter("et_time");
					
					if(Expense.addExpense(user, amount, description, category, date, time, transaction_type))
					{
						request.setAttribute("alert", "success");
						request.setAttribute("message", "Transaction Added Successfully");
						Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
					}
					else
					{
						request.setAttribute("alert", "failure");
						request.setAttribute("message", "Oops! failed to add Transaction");
						Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
					}
					return;
				}
				
				// edit transaction
				else if(path.equals("/Edittransaction"))
				{
					// Retrieve attributes
					double amount = (request.getParameter("et_amount") == null) ? 0 : (Double.parseDouble((String) request.getParameter("et_amount")));
					String description = (request.getParameter("et_description") == null) ? "" : (String) request.getParameter("et_description");
					String category = (request.getParameter("et_category") == null)? "" : (String) request.getParameter("et_category");
					String transaction_type = (request.getParameter("et_transactiontype") == null) ? "" : (String) request.getParameter("et_transactiontype");
					String date = (request.getParameter("et_date") == null) ? "current_date" : (String) request.getParameter("et_date");
					String time = (request.getParameter("et_time") == null) ? "current_time" : (String) request.getParameter("et_time");
					int id = (request.getParameter("et_id") == null) ? -1 : Integer.parseInt(request.getParameter("et_id"));
					if(Expense.editExpense(user, amount, description, category, date, time, transaction_type, id) && id != -1)
					{
						request.setAttribute("alert", "success");
						request.setAttribute("message", "Transaction Edited Successfully");
						Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
					}
					else
					{
						request.setAttribute("alert", "failure");
						request.setAttribute("message", "Oops! failed to Edit Transaction");
						Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
					}
					return;
				}
				else if(path.equals("/edit"))
				{
					PrintWriter out = response.getWriter();
					try
					{
						request.setAttribute("title", "Edit Transaction");
						int id = Integer.parseInt(request.getParameter("id"));
						Expense expense = Expense.getById(user, id);
						request.setAttribute("transaction", expense);
						ArrayList<String> categories = Expense.getCategories(user);
						request.setAttribute("categories", categories);
						RequestDispatcher dispatcher = request.getRequestDispatcher("/edit.jsp");
						dispatcher.forward(request, response);
						return;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						Error(request, response);
						return;
					}
				}
				
				else if(path.equals("/delete"))
				{
					PrintWriter out = response.getWriter();
					try
					{
						int id = Integer.parseInt(request.getParameter("id"));
						if(Expense.deleteExpense(id))
						{
							request.setAttribute("alert", "success");
							request.setAttribute("message", "Transaction Deleted Successfully");
							Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
						}
						else
						{
							request.setAttribute("alert", "failure");
							request.setAttribute("message", "Oops! failed to Delete Transaction");
							Sendback(request, response, (String)httpsession.getAttribute("previous_path"), user);
						}
						return;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						Error(request, response);
						return;
					}
				}
				else if(path.equals("/profile") || path.equals("/saveprofilepic") || path.equals("/newpassword"))
				{
					RequestDispatcher dispatcher = request.getRequestDispatcher("/ProfileController/" + path);
					dispatcher.forward(request, response);
					return;
				}
				httpsession.setAttribute("previous_path", path);
				Dashboard(request, response, user);
				return;
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			response.sendRedirect("../Login/");
			return;
		}	
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

}
