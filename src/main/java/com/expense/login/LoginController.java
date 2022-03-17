package com.expense.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.expense.user.User;

@WebServlet("/Login/*")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("user") != null)
		{
			response.sendRedirect("../Dashboard/");
			return;
		}
		
		RequestDispatcher dispatcher;
		String path = ( request.getPathInfo() != null ) ? request.getPathInfo(): "";
		if(path.equals("/Authenticate"))
		{
			String user_name = request.getParameter("user_name");
			String password= request.getParameter("password");
			
			User user = User.getbyemail(user_name);
			
			if(user != null)
			{
				if(user.authenticate(password))
				{
					HttpSession httpsession = request.getSession();
					httpsession.setMaxInactiveInterval(3600);
					httpsession.setAttribute("user", user);
					if(user.getAction().equals("reset"))
					{
						dispatcher = request.getRequestDispatcher("/resetpassword.jsp");
						dispatcher.forward(request, response);
						return;
					}
					httpsession.setAttribute("user", user);
					response.sendRedirect("../Dashboard/");
					return;
				}
				else
				{
					request.setAttribute("message", "Invalid credentials");
					dispatcher = request.getRequestDispatcher("/login.jsp");
					dispatcher.forward(request, response);
					return;
				}
			}
			
			else
			{
				request.setAttribute("message", "User Not Found");
				dispatcher = request.getRequestDispatcher("/login.jsp");
				dispatcher.forward(request, response);
				return;
			}
		}
		
		else if(path.equals("/GoogleLogin"))
		{
			String email = request.getParameter("email");
			User user = User.getbyemail(email);
			if(user == null)
			{
				String name = request.getParameter("name");
				String profile_pic_url = request.getParameter("profile_pic_url");
				if(User.create(email, name, email, profile_pic_url, "google"))
				{
					user = User.getbyemail(email);
					HttpSession httpsession = request.getSession();
					httpsession.setMaxInactiveInterval(3600);
					httpsession.setAttribute("user", user);
				}
				else
				{
					request.setAttribute("message", "Google Login Failed");
					dispatcher = request.getRequestDispatcher("/error.jsp");
					dispatcher.forward(request, response);
					return;
				}
			}
			else
			{
				HttpSession httpsession = request.getSession();
				httpsession.setMaxInactiveInterval(3600);
				httpsession.setAttribute("user", user);
			}
			
			response.sendRedirect("../Dashboard/");
			return;
		}
		
		dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
