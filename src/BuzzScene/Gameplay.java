package BuzzScene;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import BuzzScene.SceneManager.gameState;
import Buzzword.BuzzGrid;
import Buzzword.BuzzObject;
import Buzzword.BuzzScores;
import gui.Workspace;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

//@author Jeremy Chu

public class Gameplay extends BuzzScene {

	private boolean paused;
	private boolean quitPaused;
	private boolean victory;
	private ArrayList<BuzzObject> endGameObjects;
	
	public Gameplay(){
		
		buzzObjects = new ArrayList<>();
		endGameObjects = new ArrayList<>();
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
		Rectangle rect1 = new Rectangle(180, 30);
		rect1.setFill(Color.GRAY);
		time.addNode("Rectangle", rect1);
		time.addNode("Label", timeLabel);
		buzzObjects.add(time);
		
		BuzzGrid grid = new BuzzGrid("GameGrid", 300, 160);
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
		letterLabel.setFill(Color.WHITESMOKE);
		Rectangle rect2 = new Rectangle(130, 30);
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
		Text targetText = new Text("Target:\n " + Workspace.getSM().getTargetScore() + " Points");
		targetText.setFill(Color.WHITESMOKE);
		targetScore.addNode("Text", targetText);
		buzzObjects.add(targetScore);
		
		BuzzObject endGamePanel = new BuzzObject("EndGamePanel", new FlowPane(), 0, 0);
		Rectangle endGameRect = new Rectangle(Workspace.getGui().getAppPane().getWidth(), Workspace.getGui().getAppPane().getHeight());
		endGameRect.setOpacity(0.5);
		endGamePanel.addNode("Panel", endGameRect);
		endGameObjects.add(endGamePanel);
		
		BuzzObject endGameBackBox = new BuzzObject("BackBox", new FlowPane(), 300, 50);
		endGameBackBox.addNode("Rect", new Rectangle(300, 100));
		endGameObjects.add(endGameBackBox);
		
		VBox endGameTextBox = new VBox();
		endGameTextBox.setAlignment(Pos.CENTER);
		BuzzObject endGameText = new BuzzObject("EndGameText", endGameTextBox, 450, 100);
		Text titleText = new Text("Game Over");
		titleText.setFont(Font.font(48));
		titleText.setFill(Color.WHITESMOKE);
		endGameText.addNode("GameOver", titleText);
		Text victoryText = new Text("You Win!");
		victoryText.setFont(Font.font(24));
		victoryText.setFill(Color.WHITESMOKE);
		victoryText.setTextAlignment(TextAlignment.CENTER);
		endGameText.addNode("Victory", victoryText);
		endGameObjects.add(endGameText);
		
		VBox endGameBox = new VBox();
		endGameBox.setAlignment(Pos.CENTER);
		BuzzObject endGameWordList = new BuzzObject("WordList", endGameBox, 150, 350);
		Text listText = new Text("Words in the Grid:");
		listText.setFont(Font.font(20));
		listText.setFill(Color.WHITESMOKE);
		endGameWordList.addNode("Text", listText);
		ListView<String> wordList = new ListView<>();
		wordList.setItems(((BuzzGrid)find("GameGrid")).getFoundWords());
		wordList.setMaxHeight(300);
		wordList.setMinHeight(300);
		wordList.setMinWidth(100);
		wordList.setCellFactory(ComboBoxListCell.forListView(((BuzzGrid)find("GameGrid")).getFoundWords()));
		wordList.getSelectionModel().selectedItemProperty().addListener(e -> {
			((BuzzGrid)find("GameGrid")).displayPaths(wordList.getSelectionModel().getSelectedItem().toLowerCase());
		});
		endGameWordList.addNode("List", wordList);
		endGameObjects.add(endGameWordList);
		
		VBox endGameButtonsBox = new VBox();
		endGameButtonsBox.setAlignment(Pos.CENTER);
		endGameButtonsBox.minWidth(120);
		endGameButtonsBox.setSpacing(10);
		BuzzObject endGameButtons = new BuzzObject("EndGameButtons", endGameButtonsBox, 450, 500);
		Button saveAndExit = new Button("Return to Home");
		saveAndExit.setMinWidth(120);
		saveAndExit.setStyle("-fx-color : gray");
		saveAndExit.setOnAction(e ->{
			Workspace.getSM().loadScene(Workspace.getSM().getHome());
		});
		endGameButtons.addNode("Exit", saveAndExit);
		Button restart = new Button("Replay");
		restart.setMinWidth(120);
		restart.setStyle("-fx-color : gray");
		restart.setOnAction(e -> {
			unload();
			load();
		});
		endGameButtons.addNode("Replay", restart);
		endGameObjects.add(endGameButtons);
	}
	
	@Override
	public void unload(){
		super.unload();
		unloadEndGame();
		paused = false;
	}
	
	@Override
	public void load(){
		unloadEndGame();
		victory = false;
		((BuzzScores)find("Scores")).getWords().clear();
		SceneManager.currentGameState = gameState.gameplay;
		super.load();
		BuzzObject home = find("Button2");
		home.setY(150);
		((Button)home.getNode("Button")).setText("Home");
		((BuzzGrid)find("GameGrid")).generateGrid();
		find("GamemodeTitle").<Label>getNode("Label").setText(Workspace.getSM().gamemode);
		find("Level").<Label>getNode("Label").setText("Level " + Workspace.getSM().difficulty);
		find("TargetScore").<Text>getNode("Text").setText("Target:\n " + Workspace.getSM().getTargetScore() + " Points");
		Rectangle square = new Rectangle(30,30);
		square.setFill(Color.WHITESMOKE);
		find("PlayPause").<Button>getNode("Button").setGraphic(square);
		showGrid();
		AnimationTimer timer = new AnimationTimer(){

			long time = TimeUnit.SECONDS.toNanos(3);
			long lastTime = System.nanoTime();
			Text TimeDisp;
			
			public void start(){
				TimeDisp = find("Time").<Text>getNode("Label");
				super.start();
			}
			
			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
				if(SceneManager.currentGameState != gameState.gameplay)
					stop();
				if(!paused){
					time -= now - lastTime;
				}
				long tempTime = TimeUnit.NANOSECONDS.toSeconds(time);
				if(tempTime <= 0){
					TimeDisp.setText("Time Remaining: 0 seconds");
					SceneManager.currentGameState = gameState.gameEnd;
					if(((BuzzScores)find("Scores")).getScore() > Workspace.getSM().getTargetScore());
						victory = true;
				}
				else{
					TimeDisp.setText("Time Remaining: " + tempTime + " seconds");
				}
				lastTime = now;
			}
			
			public void stop(){
				super.stop();
				if(SceneManager.currentGameState == gameState.gameEnd){
					if(victory){
						find("EndGameText").<Text>getNode("Victory").setText("You Win!");
					}
					else{
						find("EndGameText").<Text>getNode("Victory").setText("Better Luck Next Time");
					}
					loadEndGame();
				}
			}
			
		};
		timer.start();
		AnimationTimer currentWordView = new AnimationTimer(){

			Text currentWord = find("Letters").<Text>getNode("Label");
			
			@Override
			public void handle(long arg0) {
				// TODO Auto-generated method stub
				currentWord.setText(((BuzzGrid)find("GameGrid")).getCurrentWord());
				if(Workspace.getSM().getGameState() == gameState.gameEnd)
					stop();
			}
			
		};
		
		currentWordView.start();
	}
	
	private void loadEndGame(){
		for(BuzzObject bz : endGameObjects){
			bz.loadNodes();
		}
	}
	
	private void unloadEndGame(){
		for(BuzzObject bz : endGameObjects){
			bz.unloadNodes();
		}
	}
	
	public BuzzObject find(String name){
		for(BuzzObject bz : buzzObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		for(BuzzObject bz : endGameObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		for(BuzzObject bz : globalBuzzObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		return null;
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
	
	public void addWordScore(String word, int score){
		BuzzScores.WordScore ws = new BuzzScores.WordScore(word, score);
		if(word.length() > 0 && !((BuzzScores)find("Scores")).getWords().contains(ws))
			((BuzzScores)find("Scores")).getWords().add(ws);
	}
}
