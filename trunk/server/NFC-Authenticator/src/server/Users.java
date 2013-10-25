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
	
	public String[] getAccounts() {
		if(accounts.size() == 0) {
			System.out.println("### DEBUG: lista vuota");
			return null;
		} else {
			String[] list = new String[accounts.size()];
			Enumeration<String> enumKey = accounts.keys();
			int i = 0;
			while(enumKey.hasMoreElements()) {
			    String key = enumKey.nextElement();
			    list[i] = key;
			    i++;
			}
			return list;
			
			
		}
	}
	
	public boolean removeUser(String user) {
		if(accounts.containsKey(user)) {
			accounts.remove(user);
			System.out.println("User " + user + " deleted.");
			return true;
		} else {
			System.out.println("User " + user + " not found");
			return false;
		}
	}
	
	public boolean addUser(String user, String password) {
		if(accounts.containsKey(user)) {
			System.out.println("The specified user is already present");
			return false;
		} else {
			accounts.put(user, new UserInfo(password));
			System.out.println("User " + user + " has been added");
			return true;
		}
	}
	
	public ArrayList<String> getAuth (String user) {
		if(accounts.containsKey(user)) {
			return accounts.get(user).getAuth();
		} else {
			return null;
		}
	}

}
