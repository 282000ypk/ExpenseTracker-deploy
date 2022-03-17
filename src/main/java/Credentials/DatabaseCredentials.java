package Credentials;

public class DatabaseCredentials {
	private String host;
	private String database;
	private String user;
	private String password;
	
	public DatabaseCredentials(String host_type) {
		this.host = "localhost";
		this.database = "et";
		this.user = "postgres";
		this.password = "postgres";
		
	}
	
	public DatabaseCredentials()
	{
		this.host = System.getenv("database_host");
		this.database = System.getenv("database_name");
		this.user = System.getenv("database_user");
		this.password = System.getenv("database_password");
	}
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
