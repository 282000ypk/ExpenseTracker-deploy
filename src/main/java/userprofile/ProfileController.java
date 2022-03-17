package userprofile;

import java.io.File;
import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import utilities.FileRename;

import java.util.Enumeration; 


import com.expense.user.User;
import com.expense.user.userDAO;

@WebServlet("/ProfileController/*")
public class ProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		HttpSession httpsession = request.getSession(false);
		if(httpsession != null)
		{
			if(httpsession.getAttribute("user")!=null)
			{
				User user = (User) httpsession.getAttribute("user");
				request.setAttribute("user", user);
				String path = request.getPathInfo();
				
				if(path.equals("/saveprofilepic"))
				{
					String file_path = getServletContext( ).getRealPath("/") + "static_content" + File.separator + "images" + File.separator + "user_profiles" + File.separator;
					System.out.println(file_path);
					MultipartRequest mpr = new MultipartRequest(request,file_path,(5 * 1024 * 1024), new FileRename());
					
					// code to delete old file
					String old_file = user.getProfile_pic_url();
					old_file = old_file.substring(old_file.indexOf("/", 1));
					String old_file_path = getServletContext( ).getRealPath(old_file);
					
					System.out.println(old_file_path);
					
					File old_file_obj = new File(old_file_path);
					if(old_file_obj.exists())
					{
						if(old_file_obj.delete())
						{
							System.out.println("old file deleted");
						}
					}
					
					Enumeration list = mpr.getFileNames( );
					if(list.hasMoreElements())
					{
						request.setAttribute("message", "Profile Picture Uploaded successfully");
						request.setAttribute("alert", "success");
						String fname =  mpr.getFilesystemName((String)list.nextElement());
						fname = "../static_content/images/user_profiles/" + fname;
						user.setProfile_pic_url(fname);
						user.updateprofile(fname, user);
						httpsession.setAttribute("user", user);
						
						RequestDispatcher dispatcher = request.getRequestDispatcher("/profile.jsp");
						dispatcher.forward(request, response);
						return;
					}
					request.setAttribute("message", "Oops! failed to upload profile picture");
					request.setAttribute("alert", "failure");
					RequestDispatcher dispatcher = request.getRequestDispatcher("/profile.jsp");
					dispatcher.forward(request, response);
					return;
				}
				
				else if(path.equals("/newpassword"))
				{
					String old_password = request.getParameter("old_password");
					if(user.getAction().equals("google") && old_password == null)
					{	
						String new_password = request.getParameter("new_password");
						if(userDAO.createLogin(user, new_password))
						{
							user.setAction("");
							httpsession.setAttribute("user", user);
							userDAO.setAction(user, "");
							request.setAttribute("message", "Password saved successfully");
							request.setAttribute("alert", "success");
						}
						else
						{
							request.setAttribute("message", "Oops! failed save password");
							request.setAttribute("alert", "failure");
						}
						
					}
					else
					{
						if(user.authenticate(old_password))
						{
							String new_password = request.getParameter("new_password");
							if(userDAO.updateLogin(user, new_password))
							{
								request.setAttribute("message", "Password updated successfully");
								request.setAttribute("alert", "success");
							}
							else 
							{
								request.setAttribute("message", "Oops! failed to reset password");
								request.setAttribute("alert", "failure");
							}
						}
						else
						{
							request.setAttribute("message", "Incorrect Old Password");
							request.setAttribute("alert", "failure");
						}

					}
				}

				
				RequestDispatcher dispatcher = request.getRequestDispatcher("/profile.jsp");
				dispatcher.forward(request, response);
				return;
			}
			else
			{
				response.sendRedirect("../Login/");
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
