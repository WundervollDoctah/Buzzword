package BuzzScene;

import java.util.ArrayList;

import BuzzScene.SceneManager.gameState;
import Buzzword.BuzzGrid;
import Buzzword.BuzzObject;
import Buzzword.BuzzScores;
import gui.Workspace;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//@author Jeremy Chu

public class Gameplay extends BuzzScene {

	private boolean paused;
	private boolean quitPaused;
	
	public Gameplay(){
		buzzObjects = new ArrayList<>();
		paused = false;
		quitPaused = false;
		generateGameplay();
	}
	
	public void generateGameplay(){
		BuzzObject gamemodeTitle = new BuzzObject("GamemodeTitle", new FlowPane(), 375, 125);
		Label gmt = new Label(Workspace.getSM().gamemode);
		gmt.setTextFill(Color.WHITESMOKE);
		gmt.setFont(Font.font(30));
		gamemodeTitle.addNode("Label", gmt);
		buzzObjects.add(gamemodeTitle);
		
		BuzzObject time = new BuzzObject("Time", new StackPane(), 675, 125);
		Text timeLabel = new Text("Time Remaining: 69 seconds");
		timeLabel.setFill(Color.WHITESMOKE);
		Rectangle rect1 = new Rectangle(160, 30);
		rect1.setFill(Color.GRAY);
		time.addNode("Rectangle", rect1);
		time.addNode("Label", timeLabel);
		buzzObjects.add(time);
		
		BuzzGrid grid = new BuzzGrid("GameGrid", 450, 310);
		grid.constructGameGrid(4, 4);
		buzzObjects.add(grid);
		
		BuzzObject level = new BuzzObject("Level", new FlowPane(), 420, 500);
		Label levelLabel = new Label("Level " + Workspace.getSM().difficulty);
		levelLabel.setFont(Font.font(30));
		levelLabel.setTextFill(Color.WHITESMOKE);
		level.addNode("Label", levelLabel);
		buzzObjects.add(level);
		
		BuzzObject playPause = new BuzzObject("PlayPause", new FlowPane(), 200, 500);
		Polygon triangle = new Polygon(0, 0, 0, 30, 20, 15);
		triangle.setFill(Color.WHITESMOKE);
		Rectangle square = new Rectangle(30,30);
		square.setFill(Color.WHITESMOKE);
		Button playPauseB = new Button("", square);
		playPauseB.setStyle("-fx-base: dimgrey");
		playPauseB.setOnAction(e -> {
			playPause();
			if(paused)
				playPauseB.setGraphic(triangle);
			else
				playPauseB.setGraphic(square);
		});
		playPause.addNode("Button", playPauseB);
		buzzObjects.add(playPause);
		
		BuzzObject letters = new BuzzObject("Letters", new StackPane(), 675, 180);
		Text letterLabel = new Text("S A N I");
		timeLabel.setFill(Color.WHITESMOKE);
		Rectangle rect2 = new Rectangle(100, 30);
		rect2.setFill(Color.GRAY);
		letters.addNode("Rectangle", rect2);
		letters.addNode("Label", letterLabel);
		buzzObjects.add(letters);
		
		BuzzScores buzzScores = new BuzzScores("Scores", 625, 200);
		buzzScores.constructBuzzScores();
		buzzObjects.add(buzzScores);
		
		BuzzObject targetRect = new BuzzObject("TargetRect", new FlowPane(), 630, 470);
		targetRect.addNode("Rect", new Rectangle(100, 50));
		buzzObjects.add(targetRect);
		
		BuzzObject targetScore = new BuzzObject("TargetScore", new FlowPane(), 640, 480);
		Text targetText = new Text("Target:\n " + (50 + (Workspace.getSM().difficulty - 1) * 10) + " Points");
		targetText.setFill(Color.WHITESMOKE);
		targetScore.addNode("Text", targetText);
		buzzObjects.add(targetScore);
	}
	
	@Override
	public void unload(){
		super.unload();
		paused = false;
	}
	
	@Override
	public void load(){
		SceneManager.currentGameState = gameState.gameplay;
		super.load();
		BuzzObject home = find("Button2");
		home.setY(150);
		((Button)home.getNode("Button")).setText("Home");
		((BuzzGrid)find("GameGrid")).generateGrid();
		find("GamemodeTitle").<Label>getNode("Label").setText(Workspace.getSM().gamemode);
		find("Level").<Label>getNode("Label").setText("Level " + Workspace.getSM().difficulty);
		find("TargetScore").<Text>getNode("Text").setText("Target:\n " + (50 + (Workspace.getSM().difficulty - 1) * 10) + " Points");
		Rectangle square = new Rectangle(30,30);
		square.setFill(Color.WHITESMOKE);
		find("PlayPause").<Button>getNode("Button").setGraphic(square);
		showGrid();
	}
	
	public void playPause(){
		Polygon triangle = new Polygon(0, 0, 0, 30, 20, 15);
		triangle.setFill(Color.WHITESMOKE);
		Rectangle square = new Rectangle(30,30);
		square.setFill(Color.WHITESMOKE);
		if(paused)
			showGrid();
		else
			hideGrid();
		paused = !paused;
	}
	
	public void quitPause(){
		hideGrid();
		quitPaused = paused;
		paused = true;
		
	}
	
	public void quitResume(){
		paused = !quitPaused;
		playPause();
	}
	
	private void hideGrid(){
		((BuzzGrid)find("GameGrid")).setVisible(false);
	}
	
	private void showGrid(){
		((BuzzGrid)find("GameGrid")).setVisible(true);
	}
}
