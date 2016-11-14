package BuzzScene;

import BuzzScene.BuzzScene;
import ui.AppGUI;

public class SceneManager {
	
	private AppGUI gui;
	private BuzzScene scene = null;
	public enum gameState {home, categorySelect, levelSelect, profileManagement, gameplay, gameEnd}
	private static gameState currentGameState = gameState.home;
	private Home home;
	
	public SceneManager(AppGUI gui){
		this.gui = gui;
	}
	
	public void createScenes(){
		home = new Home();
	}
	
	public void loadScene(BuzzScene newScene){
		if(scene != null){
			scene.unload();
		}
		scene = newScene;
		scene.load();
	}
	
	public BuzzScene getScene() {
		return scene;
	}
	public gameState getGameState() {
		return currentGameState;
	}
	public Home getHome() {
		return home;
	}
	public AppGUI getGUI() {
		return gui;
	}
}
