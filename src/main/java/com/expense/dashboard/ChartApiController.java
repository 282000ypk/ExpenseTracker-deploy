package com.expense.dashboard;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.expense.user.User;
import com.google.gson.Gson;

@WebServlet("/ChartData/*")
public class ChartApiController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession httpsession = request.getSession(false);
		try 
		{
			if(httpsession != null)
			{
				if(httpsession.getAttribute("user") == null)
				{
					response.getWriter().append("null");
					return;
				}
			}
		
			User user = (User) httpsession.getAttribute("user");
			Gson gson = new Gson();
			
			String type = (request.getParameter("type") == null)? "credit": request.getParameter("type");
			String duration = (request.getParameter("duration") == null)? "all": request.getParameter("duration");
			
			String path = (request.getPathInfo() != null)? request.getPathInfo(): "";
			if(path.equals("/line")) 
			{
				LineChartData chartdata = new LineChartData(user, duration, type);
				String json = gson.toJson(chartdata);
				response.getWriter().append(json);
				return;
			}
			
			ChartData chartdata = new ChartData(user, duration, type);
			String json = gson.toJson(chartdata);
			response.getWriter().append(json);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
