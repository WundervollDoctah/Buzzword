package Profile;

import java.util.HashMap;

import BuzzScene.Home;
import components.AppDataComponent;

//@author Jeremy Chu

public class Profile implements AppDataComponent{
	
	String username;
	HashMap<String, Integer> gamemodeLevels;
	
	public Profile(){
		username = null;
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
	
	public int getGamemodeProgress(String s){
		return gamemodeLevels.get(s);
	}
}
