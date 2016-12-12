package Profile;

import java.util.HashMap;

import BuzzScene.Home;
import components.AppDataComponent;

//@author Jeremy Chu

public class Profile implements AppDataComponent{
	
	String username;
	String password;
	HashMap<String, Integer> gamemodeLevels;
	
	public Profile(){
		username = null;
		password = null;
		gamemodeLevels = new HashMap<>();
		for(String gamemode : Home.GAMEMODES){
			if(!gamemodeLevels.containsKey(gamemode))
				gamemodeLevels.put(gamemode, 1);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public int getGamemodeProgress(String s){
		return gamemodeLevels.get(s);
	}
	
	public void increaseGamemodeProgress(String s){
		gamemodeLevels.replace(s, gamemodeLevels.get(s) + 1);
	}
}
