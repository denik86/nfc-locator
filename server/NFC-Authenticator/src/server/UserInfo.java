package server;

import java.util.ArrayList;

public class UserInfo {
	
	private String password;
	private ArrayList<String> authorizations;
	
	public UserInfo (String password) {
		authorizations = new ArrayList<String>();
		this.password = password;
	}
	
	public boolean addAuth (String resource) {
		if (authorizations.contains(resource))
			return false;
		else {
			authorizations.add(resource);
			return true;
		}
	}
	
	public boolean removeAuth (String resource) {
		if (authorizations.contains(resource)) {
			authorizations.remove(resource);
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<String> getAuth () {
		return (ArrayList<String>) authorizations.clone();
	}

}
