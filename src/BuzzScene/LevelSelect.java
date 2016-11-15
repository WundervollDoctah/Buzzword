package BuzzScene;

import java.util.ArrayList;

import Buzzword.BuzzGrid;
import Buzzword.BuzzObject;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

//@author Jeremy Chu

public class LevelSelect extends BuzzScene {

	public LevelSelect(){
		buzzObjects = new ArrayList<BuzzObject>();
		GenerateLevelSelect();
	}
	
	public void GenerateLevelSelect(){
		BuzzGrid grid = new BuzzGrid("LevelSelect", 450, 300);
		grid.constructLevelSelectGrid(4, 2);
		buzzObjects.add(grid);
		BuzzObject gamemodeTitle = new BuzzObject("GamemodeTitle", new FlowPane(), 375, 150);
		Label gmt = new Label("Sanic Memes");
		gmt.setTextFill(Color.WHITESMOKE);
		gmt.setFont(Font.font(30));
		gamemodeTitle.addNode("Label", gmt);
		buzzObjects.add(gamemodeTitle);
	}
	
	@Override
	public void load(){
		super.load();
		BuzzObject home = find("Button2");
		home.setY(150);
		((Button)home.getNode("Button")).setText("Home");
	}
	
	/*@Override
	public void unload() {
		// TODO Auto-generated method stub

	}*/

}
