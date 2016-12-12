package BuzzScene;

import java.util.ArrayList;

import Buzzword.BuzzGrid;
import Buzzword.BuzzObject;
import gui.Workspace;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ui.YesNoCancelDialogSingleton;

//@author Jeremy Chu

public class LevelSelect extends BuzzScene {

	public LevelSelect(){
		buzzObjects = new ArrayList<BuzzObject>();
		GenerateLevelSelect();
	}
	
	public void GenerateLevelSelect(){
		BuzzGrid grid = new BuzzGrid("LevelSelect", 300, 200);
		grid.constructLevelSelectGrid(4, 2);
		buzzObjects.add(grid);
		BuzzObject gamemodeTitle = new BuzzObject("GamemodeTitle", new FlowPane(), 465, 175);
		((FlowPane)gamemodeTitle.getPane()).setAlignment(Pos.CENTER);
		Label gmt = new Label("");
		gmt.setTextFill(Color.WHITESMOKE);
		gmt.setFont(Font.font(30));
		gmt.setAlignment(Pos.CENTER);
		gmt.setTextAlignment(TextAlignment.CENTER);
		gamemodeTitle.addNode("Label", gmt);
		buzzObjects.add(gamemodeTitle);
	}
	
	@Override
	public void load(){
		super.load();
		find("GamemodeTitle").<Label>getNode("Label").setText(Workspace.getSM().gamemode);
		find("Button1").<Button>getNode("Button").setDisable(true);
		BuzzObject home = find("Button2");
		((BuzzGrid)find("LevelSelect")).reloadLevels();
		home.setY(150);
		home.<Button>getNode("Button").setText("Home");
		home.<Button>getNode("Button").setOnAction(e -> {
			if(Workspace.getSM().getGameplay() == Workspace.getSM().getScene()){
				Workspace.getSM().getGameplay().quitPause();
				YesNoCancelDialogSingleton.getSingleton().show("Exit Game", "This will exit the game. Are you sure?");
				String result = YesNoCancelDialogSingleton.getSingleton().getSelection();
				if(result.equals(YesNoCancelDialogSingleton.YES)){
					Workspace.getSM().loadScene(Workspace.getSM().getHome());
				}
				else{
					Workspace.getSM().getGameplay().quitResume();
				}
			}
			else
				Workspace.getSM().loadScene(Workspace.getSM().getHome());
		});
	}
	
	/*@Override
	public void unload() {
		// TODO Auto-generated method stub

	}*/

}
