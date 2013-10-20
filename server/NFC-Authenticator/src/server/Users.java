package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Users {
	
	private Hashtable<String, UserInfo> accounts;
	
	/**
	 * File format:<br>
	 * username1 password1<br>
	 * username2 password2<br>
	 * ...
	 * @param usersFile file name
	 * @throws IOException
	 */
	public Users (String usersFile) throws IOException {
		accounts = new Hashtable<String, UserInfo>();
		FileInputStream fstream = new FileInputStream(usersFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while((strLine = br.readLine()) != null) {
			String[] split = strLine.split(" ");
			if(split.length < 2) {
				System.out.println("String " + strLine + " not recognised");
			} else {
				accounts.put(split[0], new UserInfo(split[1]));
				for(int i=2; i < split.length; i++) {
					accounts.get(split[0]).addAuth(split[i]);
					System.out.println("DEBUG -- added auth " + split[i]);
				}
			}
		}
	}
	
	public void getAccounts() {
		if(accounts.size() == 0) {
			System.out.println("Users list is empty");
		} else {
			System.out.println("User list: ");
			Enumeration<String> enumKey = accounts.keys();
			int counter = 1;
			while(enumKey.hasMoreElements()) {
			    String key = enumKey.nextElement();
			    ArrayList<String> auth = accounts.get(key).getAuth();
			    System.out.print(counter + " - " + key);
			    for(int i=0; i < auth.size(); i++) {
			    	System.out.print(" " + auth.get(i));
			    }
			    System.out.println(" ");
			    counter++;
			}
		}
	}
	
	public void removeUser(String user) {
		if(accounts.containsKey(user)) {
			accounts.remove(user);
			System.out.println("User " + user + " deleted.");
		} else {
			System.out.println("User " + user + " not found");
		}
	}
	
	public void addUser(String user, String password) {
		if(accounts.containsKey(user))
			System.out.println("The specified user is already present");
		else {
			accounts.put(user, new UserInfo(password));
			System.out.println("User " + user + " has been added");
		}
	}
	
	

}
