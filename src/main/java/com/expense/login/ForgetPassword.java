package com.expense.login;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.expense.expense.Expense;
import com.expense.user.User;
import com.expense.user.userDAO;

import Credentials.MailCredentials;
import utilities.RandomString;
import utilities.SendEmail;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


@WebServlet("/ForgetPassword/*")
public class ForgetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		path = (path == null)? "": path; 
		RequestDispatcher dispatcher = null;
		dispatcher = request.getRequestDispatcher("/forgetpassword.jsp");
		if(path.equals("/sendotp"))
		{
			String email = request.getParameter("et_email");
			User user = User.getbyemail(email);
			if(user == null) 
			{
				request.setAttribute("message", "This Email address is not registered with ExpenseTracker");
			}
			else
			{
				String one_time_password = RandomString.getRandomString(8);
				if(one_time_password != null && userDAO.updateLogin(user, one_time_password))
				{
					if(SendEmail.sendonetimepassword(one_time_password, user) && userDAO.setAction(user, "reset"))
					{
						HttpSession httpsession = request.getSession();
						if(httpsession != null)
							httpsession.invalidate();
						response.sendRedirect("../Login/");
						return;
					}
				}
			}
		}
		else if(path.equals("/newpassword"))
		{
			
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
					String password = request.getParameter("et_password");
					User user = (User) httpsession.getAttribute("user");
					if(userDAO.updateLogin(user, password)) 
					{
						userDAO.setAction(user, "");
						response.sendRedirect("../Dashboard/");
						return;
					}
					else
					{
						response.sendRedirect("/static_content/error.html");
					}
				}
			}
			catch(Exception e)
			{
				response.sendRedirect("../Login/");
				return;
			}
		}
		dispatcher.forward(request, response);
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
