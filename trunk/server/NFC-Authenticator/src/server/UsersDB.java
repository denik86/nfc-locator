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
			
			t.executeUpdate(
					"create table if not exists "
					+tableUsers+
					" (username char(100) not null, " +
					"password char (50) not null, " +
					"primary key(username));");
			System.out.println("Opened table "+ tableUsers + " successfully");
			
			t.executeUpdate(
					"create table if not exists "
					+tableAuths+
					" (resource char(100) not null, " +
					"address char (50) not null, " +
					"primary key(resource));");
			System.out.println("Opened table "+ tableAuths + " successfully");
			
			t.executeUpdate("create table if not exists associations " +
							"(username char(100), auth char(100), " +
							"foreign key(username) references "+tableUsers+"(username) on delete cascade, " +
							"foreign key(auth) references "+tableAuths+"(resource) on delete cascade)");
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
			t.executeUpdate("pragma foreign_keys = on; delete from "+tableUsers+" where username = '"+username+"'");
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
	
	public String[] getUsers()
	{
		ResultSet rs;
		String[] names = null;
		try 
		{
			names = new String[t.executeQuery("SELECT COUNT(*) FROM "+tableUsers).getInt(1)];
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
		String[] users = getUsers();
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
				t.executeUpdate("pragma foreign_keys = on; delete from "+tableAuths+" where resource = '"+auth+"'");
			else
				System.out.println("NOTICE : Authorization "+auth+" is not present into database");
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
	
	public String authLocation(String auth) {
		
		ResultSet rs;
		
		try {
			rs = t.executeQuery("select * from "+tableAuths+" where resource = '"+auth+"';");
			if(rs.next())
				return rs.getString("address");
			} catch (SQLException e)
			{
				System.out.println("ERR :: failed to find the Auth");
				e.printStackTrace();
			}
		return null;
	}

	public boolean checkAuthUser(String username, String auth)
	{
		try {
			return t.executeQuery("select * from associations where auth = '"+auth+"' and username = '"+username+"' ;").next();
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
	
 	public String remAuthToUser(String username, String auth)
 	{
 		try {
			if(checkAuthUser(username,auth))
				t.executeUpdate("delete from associations where username = '"+username+"' and auth = '"+auth+"'");
			else
				System.out.println("NOTICE : Authorization "+auth+" is not associated to user "+username);
		} catch (SQLException e) {
			System.out.println("ERR :: failed to remove the association "+auth+" - " + username);
			e.printStackTrace();
		}
		return auth;
 	}
	
 	public String[] getAuths()
	{
		ResultSet rs;
		String[] auth = null;
		try 
		{
			auth = new String[t.executeQuery("SELECT COUNT(*) FROM "+tableAuths).getInt(1)];
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
	
 	public String[] getAuthsNOUser(String username)
 	{
 		System.out.println(username);
 		
 		ResultSet rs;
		String[] auth = null;
		try 
		{
			auth = new String[t.executeQuery("SELECT count(*) from (select resource from "+tableAuths+" as auth except select auth from associations where username = '"+username+"') s").getInt(1)];
			rs = t.executeQuery("SELECT resource from "+tableAuths+" as auth except select auth from associations where username = '"+username+"'");
			System.out.println("dimens non " +auth.length);
	
			int i = 0;
		    while ( rs.next() ) {
		    	auth[i] = rs.getString(1);
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
 	
	public String[] getAuthsUsers(String username)
	{
		ResultSet rs;
		String[] auth = null;
		try 
		{
			auth = new String[t.executeQuery("SELECT COUNT(*) FROM associations where username = '"+username+"'").getInt(1)];
			rs = t.executeQuery( "select auth from associations where username = '"+username+"';" );
	
			int i = 0;
		    while ( rs.next() ) {
		    	auth[i] = rs.getString("auth");
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
	
	public String[] getUsersAuth(String auth)
	{
		ResultSet rs;
		String[] users = null;
		try 
		{
			users = new String[t.executeQuery("SELECT COUNT(*) FROM associations where auth = '"+auth+"'").getInt(1)];
			rs = t.executeQuery( "select username from associations where auth = '"+auth+"';" );
	
			int i = 0;
		    while ( rs.next() ) {
		    	users[i] = rs.getString("username");
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
		return users;    
	}
	
	public void printAuth() throws SQLException
	{
		String[] auth = getAuths();
		for(int i = 0; i < auth.length; i++)
	    	System.out.println( (i+1) + ". " + auth[i]);  
	}
	
}
