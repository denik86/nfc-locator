package server;


import java.sql.*;

public class UsersDB {
	
	private String db;
	private String tableUsers;
	private String tableAuths;
	private String driverJDBC = "org.sqlite.JDBC";
	private Statement t;
	
	public UsersDB(String dbUsers, String tableUsers, String tableAuths) throws ClassNotFoundException
	{
		Class.forName(driverJDBC);
		Connection conn = null;

		db = dbUsers;
		this.tableUsers = tableUsers;
		this.tableAuths = tableAuths;
	
		try
		{
			conn = DriverManager.getConnection("jdbc:sqlite:" + db + ".db");
			System.out.println("Opened database " +db + " successfully");
			
			t = conn.createStatement();
			
			t.executeUpdate("create table if not exists "+tableUsers+" (username char(100), password char (50));");
			System.out.println("Opened table "+ tableUsers + " successfully");
			
			t.executeUpdate("create table if not exists "+tableAuths+" (resource char(100), address char (50));");
			System.out.println("Opened table "+ tableAuths + " successfully");
			
			t.executeUpdate("create table if not exists associations (username char(100), auth char(100));");
		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public String addUser(String username, String password)
	{
		try {
			if(!checkUser(username))
				t.executeUpdate("insert into "+tableUsers+" values('"+username+"', '"+password+"')");
			else
				System.out.println("NOTICE : User "+username+" is already present into database");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to insert the new user");
			e.printStackTrace();
		}
		return username;
	}
	
	public String removeUser(String username)
	{
		try {
			t.executeUpdate("delete from "+tableUsers+" where username = '"+username+"'");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to remove the user "+username);
			e.printStackTrace();
		}
		return username;
	}
	
	public boolean checkUser(String username)
	{
		try {
			return t.executeQuery("select username from "+tableUsers+" where username = '"+username+"';").next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkUser(String username, String password)
	{
		ResultSet rs;
		
		try {
			rs = t.executeQuery("select * from "+tableUsers+" where username = '"+username+"';");
			if(rs.next() && rs.getString("password").equals(password))
				return true;
			} catch (SQLException e)
			{
				System.out.println("ERR :: failed to check a user");
				e.printStackTrace();
			}
		return false;
	}
	
	public String[] getUsersName()
	{
		ResultSet rs;
		String[] names = null;
		try 
		{
			names = new String[countUsers()];
			rs = t.executeQuery( "select * from "+tableUsers+";" );
	
			int i = 0;
		    while ( rs.next() ) {
		    	names[i] = rs.getString("username");
		    	i++;
		    } 
		} 
		catch (SQLException e) 
		{
			System.out.println("---------------------");
			System.out.println("ERROR IN SQL COMMAND: ");
			e.printStackTrace();
			System.out.println("---------------------");
			
		}
		return names;
		    
	}
	
	public void printUsers() throws SQLException
	{
		String[] users = getUsersName();
		for(int i = 0; i < users.length; i++)
	    	System.out.println( (i+1) + ". " + users[i]);  
	}

	public String addAuth(String auth, String address)
	{
		try {			
			if(!checkAuth(auth))
				t.executeUpdate("insert into "+tableAuths+" values('"+auth+"', '"+address+"')");
			else
				System.out.println("NOTICE : Authorization "+auth+" is already present into database");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to add the authorization " + auth);
			e.printStackTrace();
		}
		return auth;
		
	}
	
	public String removeAuth(String auth)
	{
		try {
			if(checkAuth(auth))
				t.executeUpdate("delete from "+tableAuths+" where resource = '"+auth+"'");
			else
				System.out.println("NOTICE : Authorization "+auth+" is already present into database");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to remove the authorization "+auth);
			e.printStackTrace();
		}
		return auth;
	}
	
	public boolean checkAuth(String auth)
	{
		try {
			return t.executeQuery("select resource from "+tableAuths+" where resource = '"+auth+"';").next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addAuthToUser(String username, String auth)
	{
		try {
			t.executeUpdate("insert into associations values('"+username+"', '"+auth+"')");
		} catch(SQLException e) {
			System.out.println("ERR :: failed to insert new association ("+username+" ,"+auth+")");
			e.printStackTrace();
		}
	}
	
	public String[] getAuths()
	{
		ResultSet rs;
		String[] auth = null;
		try 
		{
			auth = new String[countAuths()];
			rs = t.executeQuery( "select * from "+tableAuths+";" );
	
			int i = 0;
		    while ( rs.next() ) {
		    	auth[i] = rs.getString("resource");
		    	i++;
		    } 
		} 
		catch (SQLException e) 
		{
			System.out.println("---------------------");
			System.out.println("ERROR IN SQL COMMAND: ");
			e.printStackTrace();
			System.out.println("---------------------");
			
		}
		return auth;
		    
	}
	
	public void printAuth() throws SQLException
	{
		String[] auth = getAuths();
		for(int i = 0; i < auth.length; i++)
	    	System.out.println( (i+1) + ". " + auth[i]);  
	}
	
	
	public int countUsers()
	{
		int nrows = 0;
		ResultSet rs;
		try {
			rs = t.executeQuery("SELECT COUNT(*) FROM "+tableUsers);
			 nrows = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nrows;
	   
	    
	}
	
	public int countAuths()
	{
		int nrows = 0;
		ResultSet rs;
		try {
			rs = t.executeQuery("SELECT COUNT(*) FROM "+tableAuths);
			 nrows = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nrows;
	   
	    
	}
	
	
}
