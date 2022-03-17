package Credentials;

public class MailCredentials {
	private String email;
	private String password;
	
	
	
	public MailCredentials() {
		this.email = System.getenv("ET_EMAIL");
		this.password = System.getenv("ET_PASSWORD");
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
