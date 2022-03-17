package utilities;

import javax.mail.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.expense.user.User;

import Credentials.MailCredentials;

public class SendEmail {
	public static boolean sendonetimepassword(String password, User user)
	{
		MailCredentials cred = new MailCredentials();
		String message = "Hello " + user.getName()
						+ "\n\n"
						+ "we have done a Password Reset for Your ExpenseTracket Account. and below is your one time password to access the application"
						+ "\n\n<h2>"
						+ password
						+ "</h2>\n\n"
						+ "If you still face any problems in accessing the Platform Please write us on litscripters@gmail.com";
		
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");   
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.user", cred.getEmail()); /*replace youremail@gmail.com with your email which you are going to user for sending emails*/
		props.setProperty("mail.password", cred.getPassword()); /*replace youremailpassword with your email password which you are going to user for sending emails*/
		
		//Session se = Session.getDefaultInstance(props);
		Session se = Session.getInstance(props,
		          new javax.mail.Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		            		return new PasswordAuthentication(cred.getEmail(), cred.getPassword());
		            }
		          });
		
		try 
		{
		    MimeMessage message1 = new MimeMessage(se);
		    message1.setFrom(new InternetAddress(cred.getEmail()));
		    message1.addRecipient(Message.RecipientType.TO,new InternetAddress(user.getEmail()));
		    message1.setSubject("One Time Password - ExpenseTracker");
		    message1.setContent(message, "text/html");
		   
		    Transport.send(message1);
		    return true;
		}
		catch(Exception e) 
		{
			System.out.print(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean sendmessage(String message, String name)
	{
		MailCredentials cred = new MailCredentials();
		String recepient = "ypkamble200045@gmail.com";
		
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");   
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.user", cred.getEmail()); /*replace youremail@gmail.com with your email which you are going to user for sending emails*/
		props.setProperty("mail.password", cred.getPassword()); /*replace youremailpassword with your email password which you are going to user for sending emails*/
		
		//Session se = Session.getDefaultInstance(props);
		Session se = Session.getInstance(props,
		          new javax.mail.Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		            		return new PasswordAuthentication(cred.getEmail(), cred.getPassword());
		            }
		          });
		
		try 
		{
		    MimeMessage message1 = new MimeMessage(se);
		    message1.setFrom(new InternetAddress(cred.getEmail()));
		    message1.addRecipient(Message.RecipientType.TO,new InternetAddress(recepient));
		    message1.setSubject(name + " - ExpenseTracker - Sent a Message");
		    message1.setContent(message, "text/html");
		   
		    Transport.send(message1);
		    return true;
		}
		catch(Exception e) 
		{
			System.out.print(e);
			e.printStackTrace();
			return false;
		}
	}
}
