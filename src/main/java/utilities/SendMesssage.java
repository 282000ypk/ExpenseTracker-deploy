package utilities;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class SendMesssage
 */
@WebServlet("/SendMessage")
public class SendMesssage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class Status
	{
		public int status;
		Status(){
			this.status = 0;
		}
		
		Status(int num){
			this.status = num;
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		name = (name == null)? "": name;
		String message = request.getParameter("message");
		message = (message == null)? "" : message;
		Gson gson = new Gson();
		
		if(SendEmail.sendmessage(message, name))
			response.getWriter().append(gson.toJson(new Status(0)));
		else
			response.getWriter().append(gson.toJson(new Status(1)));
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
