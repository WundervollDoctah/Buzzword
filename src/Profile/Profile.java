package Profile;

import components.AppDataComponent;

//@author Jeremy Chu

public class Profile implements AppDataComponent{
	
	String username;
	
	public Profile(){
		username = null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public String getUsername() {
		return username;
	}
}
