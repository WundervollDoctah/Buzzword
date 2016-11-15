package BuzzScene;

import java.util.ArrayList;
import Buzzword.BuzzObject;

//@author Jeremy Chu

public abstract class BuzzScene {
	
	protected ArrayList<BuzzObject> buzzObjects;
	protected static ArrayList<BuzzObject> globalBuzzObjects = new ArrayList<>();
	
	public void load(){
		for(BuzzObject bz : buzzObjects){
			bz.loadNodes();
		}
	}
	
	public void unload() {
		for(BuzzObject bz : buzzObjects){
			bz.unloadNodes();
		}
	}
	
	public void addGlobal(BuzzObject bz){
		globalBuzzObjects.add(bz);
		bz.loadNodes();
	}
	
	public BuzzObject find(String name){
		for(BuzzObject bz : buzzObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		for(BuzzObject bz : globalBuzzObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		return null;
	}

}
