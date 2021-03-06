package BuzzScene;

import BuzzScene.BuzzScene;
import ui.AppGUI;

//@author Jeremy Chu

public class SceneManager {
	
	private AppGUI gui;
	private BuzzScene scene = null;
	public enum gameState {splash, home, levelSelect, profileManagement, gameplay, gameEnd}
	static gameState currentGameState = gameState.splash;
	private Home home;
	private LevelSelect levelSelect;
	private Gameplay gameplay;
	String gamemode = "Select Mode";
	int difficulty = 1;
	
	public SceneManager(AppGUI gui){
		this.gui = gui;
	}
	
	public void createScenes(){
		home = new Home();
		levelSelect = new LevelSelect();
		gameplay = new Gameplay();
	}
	
	public LevelSelect getLevelSelect() {
		return levelSelect;
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

	public Gameplay getGameplay() {
		return gameplay;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
	
	public void setDifficulty(int value){
		difficulty = value;
	}
	
	public String getGamemode(){
		return gamemode;
	}
	
	public int getTargetScore(){
		return 25 + (difficulty - 1) * 5;
	}
}
