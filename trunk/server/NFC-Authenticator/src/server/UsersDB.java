package server;


import java.sql.*;

public class UsersDB {
	
	private String db;
	private String table;
	private String driverJDBC = "org.sqlite.JDBC";
	private Statement t;
	
	public UsersDB(String dbUsers, String tableUsers) throws ClassNotFoundException
	{
		Class.forName(driverJDBC);
		Connection conn = null;

		db = dbUsers;
		table = tableUsers;
		
		try
		{
			conn = DriverManager.getConnection("jdbc:sqlite:" + db + ".db");
			System.out.println("Opened database " +db + " successfully");
			
			t = conn.createStatement();
			t.executeUpdate("create table if not exists "+table+" (username char(100), password char (50), auth char(200));");
			System.out.println("Opened table "+ table + " successfully");
		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public String addUser(String username, String password)
	{
		try {
			t.executeUpdate("insert into "+table+" values('"+username+"', '"+password+"', '')");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to insert the new user");
			e.printStackTrace();
		}
		return username;
	}
	
	public String removeUser(String username)
	{
		try {
			t.executeUpdate("delete from "+table+" where username = '"+username+"'");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to remove the user "+username);
			e.printStackTrace();
		}
		return username;
	}
	
	public String addAuth(String auth, String username)
	{
		ResultSet rs;
		String authString;
		try {
			rs = t.executeQuery( "select auth from "+table+";" );
			authString = rs.getString("auth");
			authString = authString + "|" + auth;
			
			t.executeUpdate("update " +table+" set auth = '"+authString+"'" +" where username = '"+username+"';");
		} catch (SQLException e) {
			System.out.println("ERR :: failed to add the authorization at "+username);
			e.printStackTrace();
		}
		return username;
		
	}
	
	public String[] getUsersName()
	{
		ResultSet rs;
		String[] names = null;
		try 
		{
			names = new String[countRows()];
			rs = t.executeQuery( "select * from "+table+";" );
	
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
	
	public void printUsersName() throws SQLException
	{
		ResultSet rs = t.executeQuery( "select * from "+table+";" );
	    while ( rs.next() ) {
	    	System.out.println(rs.getString("username"));
	    }   
	}
	
	public int countRows()
	{
		int nrows = 0;
		ResultSet rs;
		try {
			rs = t.executeQuery("SELECT COUNT(*) FROM "+table);
			 nrows = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nrows;
	   
	    
	}
	
	
}
