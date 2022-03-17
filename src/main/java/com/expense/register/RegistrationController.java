package com.expense.register;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.expense.user.User;

@WebServlet("/Registration/*")
public class RegistrationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		RequestDispatcher dispatcher;
		String path = ( request.getPathInfo() != null ) ? request.getPathInfo(): "";
		if(path.equals("/Register"))
		{
			String id = request.getParameter("et_email");
			String email = id;
			String name = request.getParameter("et_name");
			String profile_pic_url = "/ExpenseTracker/static_content/images/user_profiles/default.png";
			String password = request.getParameter("et_password");
			if(User.getbyemail(email)!=null)
			{
				request.setAttribute("message", "This email address is already user please user another email address");
				request.setAttribute("alert", "failure");
				dispatcher = request.getRequestDispatcher("/register.jsp");
				dispatcher.forward(request, response);
				return;
			}
			else
			{
				if(User.create(id, name, email, profile_pic_url, password, ""))
				{
					request.setAttribute("message", "Registration Successfull PLease login with the credentials created");
					HttpSession httpsession = request.getSession();
					httpsession.invalidate();
					response.sendRedirect("../Login/");
					return;
				}
				else
				{
					request.setAttribute("alert", "failure");
					request.setAttribute("message", "Your account cound not be created due to some Technical Issues");
					dispatcher = request.getRequestDispatcher("/register.jsp");
					dispatcher.forward(request, response);
					return;
				}
			}
		}
		dispatcher = request.getRequestDispatcher("/register.jsp");
		dispatcher.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

}
