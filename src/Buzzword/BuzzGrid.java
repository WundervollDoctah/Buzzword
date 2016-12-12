package Buzzword;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import BuzzScene.Home;
import BuzzScene.SceneManager;
import BuzzScene.SceneManager.gameState;
import Profile.ProfileManager;
import gui.Workspace;
import javafx.animation.AnimationTimer;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import ui.AppMessageDialogSingleton;

//@author Jeremy Chu

public class BuzzGrid extends BuzzObject {
	
	private static final int TOTAL_NUMBER_OF_STORED_WORDS    = 330622;
	private GridPane grid;
	private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private Hashtable<String, TreeSet<String>> dictionaries;
	private ObservableList<String> foundWords;
	private int gridWidth;
	private int gridHeight;
	private Canvas canvas;
	private Button lastButton;
	private ArrayList<Button> selectedButtons;
	private Line lineDraw;
	private ArrayList<Line> pathLines;
	private String currentWord;
	private HashSet<Button> litButtons;
	private ArrayList<ArrayList<Button>> tetherPaths;
	private AnimationTimer showPaths;

	public BuzzGrid(String name) {
		this(name, 0, 0);
	}
	
	public BuzzGrid(String name, double x, double y) {
		super(name, new Pane(), x, y);
		grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		pane.getChildren().add(grid);
		dictionaries = new Hashtable<>();
		pathLines = new ArrayList<>();
		litButtons = new HashSet<>();
		tetherPaths = new ArrayList<>();
		foundWords = FXCollections.observableList(new ArrayList<>());
		selectedButtons = new ArrayList<>();
	}
	
	public void constructHomeGrid(int width, int height){
		gridWidth = width;
		gridHeight = height;
		Button[] buttons = new Button[]{new Button("B"), new Button("U"), new Button(" "), new Button(" "),
				new Button("Z"), new Button("Z"), new Button(" "), new Button(" "),
				new Button(" "), new Button(" "), new Button("W"), new Button("O"),
				new Button(" "), new Button(" "), new Button("R"), new Button("D")};
		for(int i = 1; i <= height; i++){
			for(int j = 1; j <= width; j++){
				Button circle = buttons[((i-1) * 4) + j - 1];
				circle.setShape(new Circle(30));
				circle.setStyle("-fx-base: gray;"
						+ "-fx-background-radius: 20px;"
						+ "-fx-font-size: 25px;");
				grid.add(circle, j, i);
				int buttonSize = 60; 
				circle.setMinSize(buttonSize, buttonSize);
				circle.setPrefSize(buttonSize, buttonSize);
				circle.setMaxSize(buttonSize, buttonSize);
			}
		}
	}
	
	public void constructLevelSelectGrid(int width, int height){
		gridWidth = width;
		gridHeight = height;
		Button[] buttons = new Button[]{new Button("1"), new Button("2"), new Button("3"), new Button("4"),
				new Button("5"), new Button("6"), new Button("7"), new Button("8")};
		for(int i = 1; i <= height; i++){
			for(int j = 1; j <= width; j++){
				Button circle = buttons[((i-1) * 4) + j - 1];
				circle.setShape(new Circle(30));
				circle.setStyle("-fx-base: gray;"
						+ "-fx-background-radius: 20px;"
						+ "-fx-font-size: 25px;");
				circle.setAlignment(Pos.CENTER);
				if(i > 1)
					circle.setDisable(true);
				grid.add(circle, j, i);
				int buttonSize = 60; 
				circle.setMinSize(buttonSize, buttonSize);
				circle.setPrefSize(buttonSize, buttonSize);
				circle.setMaxSize(buttonSize, buttonSize);
				int difficulty = (i-1)*4 + j;
				circle.setOnAction(e -> {	
					Workspace.getSM().setDifficulty(difficulty);
					Workspace.getSM().loadScene(Workspace.getSM().getGameplay());
				});
			}
		}
	}
	
	public void reloadLevels(){
		for(Node button : grid.getChildren()){
			int maxLevel = Integer.parseInt(((Button)button).getText());
			if(maxLevel <= ProfileManager.getProfileManager().getLoadedProfile().getGamemodeProgress(Workspace.getSM().getGamemode())){
				((Button)button).setDisable(false);
			}
			else
				((Button)button).setDisable(true);
		}
	}
	
	public void constructGameGrid(int width, int height){
		createDictionaries();
		grid.getChildren().clear();
		double circleRadius = 30;
		gridWidth = width;
		gridHeight = height;
		int buttonSize = 60; 
		int gapSize = 20;
		Canvas canvas = new Canvas(width * buttonSize + (width - 1) * gapSize, height * buttonSize + (width - 1) * gapSize);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for(int i = 0; i < width; i++)
			for(int j = 0; j <= height; j++)
				gc.strokeLine(i*40 + i*20, j*40+(j-1)*40 - 10, i*40 + i*40 + 20, j*40+(j-1)*40 - 10);
		for(int i = 0; i <= width; i++)
			for(int j = 0; j < height; j++)
				gc.strokeLine(i*40 + (i-1)*40 - 10, j*60+(j-1)*20, i*40 + (i-1)*40 - 10, j*60+(j-1)*20+40);
		pane.getChildren().add(canvas);
		canvas.toBack();
		Button[] buttons = new Button[]{new Button("G"), new Button("O"), new Button("T"), new Button("T"),
				new Button("A"), new Button("G"), new Button("O"), new Button("S"),
				new Button("A"), new Button("N"), new Button("I"), new Button("C"),
				new Button("F"), new Button("A"), new Button("S"), new Button("T")};
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				Button circle = buttons[i * 4 + j];
				circle.setShape(new Circle(circleRadius));
				circle.setStyle("-fx-base: gray;"
						+ "-fx-background-radius: 20px;"
						+ "-fx-font-size: 25px;");
				circle.setAlignment(Pos.CENTER);
				grid.add(circle, j, i);
				
				circle.setMinSize(buttonSize, buttonSize);
				circle.setPrefSize(buttonSize, buttonSize);
				circle.setMaxSize(buttonSize, buttonSize);
				
				SceneManager sm = Workspace.getSM();
				
				circle.setOnDragDetected(e -> {
					if(sm.getGameState() == gameState.gameplay){
						currentWord = "";
						currentWord += circle.getText();
						selectedButtons = new ArrayList<>();
						Workspace.getGui().getPrimaryScene().startFullDrag();
						circle.setEffect(new DropShadow(circleRadius + 2, Color.LIGHTGREEN));
						lastButton = circle;
						selectedButtons.add(circle);
						lineDraw = new Line();
						lineDraw.setStroke(Color.RED);
						lineDraw.setStrokeWidth(4);
						lineDraw.setStrokeLineCap(StrokeLineCap.ROUND);
						lineDraw.setStartX(circle.getTranslateX() + circle.getLayoutX() + 30);
						lineDraw.setStartY(circle.getTranslateY() + circle.getLayoutY() + 30);
						lineDraw.setEndX(circle.getTranslateX() + circle.getLayoutX() + 30);
						lineDraw.setEndY(circle.getTranslateY() + circle.getLayoutY() + 30);
						lineDraw.setMouseTransparent(true);
						pane.getChildren().add(lineDraw);
					}
				});
				Workspace.getGui().getPrimaryScene().setOnKeyPressed(e ->{
					if(sm.getGameState() == gameState.gameplay){
						if(e.getCode() == KeyCode.ENTER){
							if(checkIsWord(currentWord.toLowerCase()))
								Workspace.getSM().getGameplay().addWordScore(currentWord, getScore(currentWord.length()));
							currentWord = "";
							for(Button b : buttons)
								b.setEffect(null);
							pane.getChildren().removeAll(pathLines);
							pathLines.clear();
						}
					}
				});
				
				Workspace.getGui().getPrimaryScene().setOnKeyTyped( e -> {
					if(sm.getGameState() == gameState.gameplay){
						tetherPaths = new ArrayList<>();
						if(currentWord.length() < 1){
							boolean isLetter = false;
							for(Button b : buttons){
								if(e.getCharacter().toUpperCase().equals(b.getText())){
									b.setEffect(new DropShadow(circleRadius + 2, Color.LIGHTGREEN));
									isLetter = true;
								}
							}
							if(isLetter)
								currentWord += e.getCharacter().toUpperCase();
						}
						else{
							litButtons = new HashSet<>();
							boolean isPath = checkPathExists(currentWord.toLowerCase() + e.getCharacter().toLowerCase());
							//boolean canAdd = isPath ? checkLetterAdjacency(e.getCharacter().toLowerCase()) : false;
							if(isPath){
								pane.getChildren().removeAll(pathLines);
								pathLines.clear();
								for(Button b : buttons)
									b.setEffect(null);
								for(Button b : litButtons)
									b.setEffect(new DropShadow(30 + 2, Color.LIGHTGREEN));
								for(ArrayList<Button> al : tetherPaths)
									for(int k = 0; k < al.size() - 1; k++)
										addTether(al.get(k), al.get(k+1));
								currentWord += isPath ? e.getCharacter().toUpperCase() : "";
							}
						}
					}
							
				});
				
				Workspace.getGui().getPrimaryScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, (mouseEvent) ->{
					if(sm.getGameState() == gameState.gameplay){
						if(lineDraw != null){
							lineDraw.setEndX(mouseEvent.getX() - x);
							lineDraw.setEndY(mouseEvent.getY() - y);
						}
					}
				});
				
				circle.setOnMouseDragEntered(e -> {
					if(sm.getGameState() == gameState.gameplay){
						if(checkAdjacentButtons(circle) && !selectedButtons.contains(circle)){
							currentWord += circle.getText();
							circle.setEffect(new DropShadow(circleRadius + 2, Color.LIGHTGREEN));
							lastButton = circle;
							selectedButtons.add(circle);
							lineDraw.setEndX(circle.getTranslateX() + circle.getLayoutX() + 30);
							lineDraw.setEndY(circle.getTranslateX() + circle.getLayoutY() + 30);
							pathLines.add(lineDraw);
							lineDraw = new Line(circle.getTranslateX() + circle.getLayoutX() + 30, circle.getTranslateY() + circle.getLayoutY() + 30, circle.getTranslateX() + circle.getLayoutX() + 30, circle.getTranslateY() + circle.getLayoutY() + 30);
							lineDraw.setMouseTransparent(true);
							lineDraw.setStroke(Color.RED);
							lineDraw.setStrokeWidth(4);
							lineDraw.setStrokeLineCap(StrokeLineCap.ROUND);
							pane.getChildren().add(lineDraw);
						}
					}
				});
				
				Workspace.getGui().getPrimaryScene().addEventFilter(MouseDragEvent.MOUSE_DRAG_RELEASED, e -> {
					if(checkIsWord(currentWord.toLowerCase()))
						Workspace.getSM().getGameplay().addWordScore(currentWord, getScore(currentWord.length()));
					currentWord = "";
					for(Button b : buttons)
						b.setEffect(null);
					selectedButtons.clear();
					pane.getChildren().removeAll(pathLines);
					pathLines.clear();
					pane.getChildren().remove(lineDraw);
					lineDraw = null;
					e.consume();
				});
			}
		}
		
	}
	
	public void generateGrid(){
		currentWord = "";
		for(Line ln : pathLines){
			pane.getChildren().remove(ln);
		}
		pathLines.clear();
		int targetScore =Workspace.getSM().getTargetScore();
		int maxScore = 0;
		long time = System.nanoTime();
		while(maxScore < targetScore){
			maxScore = 0;
			foundWords.clear();
			System.out.println("looped");
			for(Node button : grid.getChildren()){
				if(button instanceof Button){
					button.setEffect(null);
					((Button)button).setText(Character.toString(alphabet[(int)(Math.random()*26)]));
				}
			}
			for(int i = 0; i < gridHeight; i++){
				for(int j = 0; j < gridWidth; j++){
					maxScore += startWordCheck(i,j,new ArrayList<Node>(), "");
				}
			}
		}
		time = System.nanoTime() - time;
		double milliseconds = time / 1000000.0;
		System.out.println(milliseconds);
		Collections.sort(foundWords);
		try(FileWriter writer = new FileWriter("words_text.txt")){
			for(String str : foundWords)
				writer.write(str.toLowerCase() + "\n");
			writer.write("generation time: " + milliseconds);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		System.out.println(maxScore);
	}
	
	private int startWordCheck(int i, int j, ArrayList<Node> markedNodes, String word){
		int score = 0;
		boolean foundNode = false;
		ArrayList<Node> arrayCopy = new ArrayList<Node>();
		arrayCopy.addAll(markedNodes);
		for(Node button : grid.getChildren()){
	        if(GridPane.getRowIndex(button) == i && GridPane.getColumnIndex(button) == j && !markedNodes.contains(button)) {
	            arrayCopy.add(button);
	            word += ((Button)button).getText();
	            foundNode = true;
	            break;
	        }
		}
		if(!foundNode || word.length() > 16)
			return 0;
		if(!checkPossibleWord(word.toLowerCase())){
			return 0;
		}
		else if(!foundWords.contains(word) && word.length() > 2 && checkIsWord(word.toLowerCase())){
			System.out.println(word + " is good");
			foundWords.add(word);
			score += getScore(word.length());
		}
		
		score += startWordCheck(i+1, j, arrayCopy, word);
		score += startWordCheck(i, j+1, arrayCopy, word);
		score += startWordCheck(i-1, j, arrayCopy, word);
		score += startWordCheck(i, j-1, arrayCopy, word);
		score += startWordCheck(i+1, j+1, arrayCopy, word);
		score += startWordCheck(i-1, j+1, arrayCopy, word);
		score += startWordCheck(i+1, j-1, arrayCopy, word);
		score += startWordCheck(i-1, j-1, arrayCopy, word);
		return score;
	}
	
	private boolean checkPossibleWord(String s){
		String possibleString = dictionaries.get(Workspace.getSM().getGamemode()).ceiling(s);
		if(possibleString != null && possibleString.contains(s))
			return true;
		else
			return false;
	}
	
	private boolean checkIsWord(String s){
		if(s.length() > 2 && dictionaries.get(Workspace.getSM().getGamemode()).contains(s))
			return true;
		else
			return false;
	}
	
	private void createDictionaries(){
		for(String str : Home.GAMEMODES){
			if(str.equals("Select Mode"))
				continue;
			System.out.println(str);
			TreeSet<String> dictionary = new TreeSet<>();
			URL wordsResource = getClass().getClassLoader().getResource("words/" + str + ".txt");
			assert wordsResource != null;
			try(Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
				lines.forEach(dictionary::add);
			} 
			catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
				dialog.show("Missing Gamemode", "No dictionary for " + str);
				System.exit(0);
			}
			dictionaries.put(str, dictionary);
		}
	}
	
	public void setVisible(boolean value){
		pane.setVisible(value);
	}
	
	public static int getScore(int length){
		if(length < 5)
			return 1;
		else if(length < 8){
			switch(length){
			case 5:
				return 2;
			case 6:
				return 3;
			case 7:
				return 5;
			}
		}
		return 11;
	}
	
	private boolean checkPathExists(String word){
		int pathExists = 0;
		for(int i = 0; i < gridHeight; i++){
			for(int j = 0; j < gridWidth; j++){
				pathExists += startPathCheck(i,j,new ArrayList<Button>(), word);
			}
		}
		return pathExists > 0;
	}
	
	private int startPathCheck(int i, int j, ArrayList<Button> markedNodes, String word){
		int score = 0;
		boolean foundNode = false;
		ArrayList<Button> arrayCopy = new ArrayList<>();
		arrayCopy.addAll(markedNodes);
		for(Node button : grid.getChildren()){
	        if(GridPane.getRowIndex(button) == i && GridPane.getColumnIndex(button) == j && !markedNodes.contains(button)) {
	            arrayCopy.add((Button)button);
	            foundNode = true;
	            break;
	        }
		}
		if(!foundNode){
			return 0;
		}
		String constructedWord = "";
		for(Button b : arrayCopy){
			constructedWord += b.getText();
		}
		if(!word.contains(constructedWord.toLowerCase())){
			return 0;
		}
		else if(word.equals(constructedWord.toLowerCase())){
			System.out.println(word + " has a path");
			for(Button b : arrayCopy){
				//b.setEffect(new DropShadow(30 + 2, Color.LIGHTGREEN));
				litButtons.add(b);
			}
			tetherPaths.add(arrayCopy);
			return 1;
		}
		
		score += startPathCheck(i+1, j, arrayCopy, word);
		score += startPathCheck(i, j+1, arrayCopy, word);
		score += startPathCheck(i-1, j, arrayCopy, word);
		score += startPathCheck(i, j-1, arrayCopy, word);
		score += startPathCheck(i+1, j+1, arrayCopy, word);
		score += startPathCheck(i-1, j+1, arrayCopy, word);
		score += startPathCheck(i+1, j-1, arrayCopy, word);
		score += startPathCheck(i-1, j-1, arrayCopy, word);
		return score;
	}
	
	private void addTether(Button b, Button c){
		Line line = new Line();
		line.setStroke(Color.RED);
		line.setStrokeWidth(4);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStartX(b.getTranslateX() + b.getLayoutX() + 30);
		line.setStartY(b.getTranslateY() + b.getLayoutY() + 30);
		line.setEndX(c.getTranslateX() + c.getLayoutX() + 30);
		line.setEndY(c.getTranslateY() + c.getLayoutY() + 30);
		line.setMouseTransparent(true);
		pathLines.add(line);
		pane.getChildren().add(line);
	}
	
	private boolean checkAdjacentButtons(Button b){
		return checkAdjacentButtons(b, lastButton);
	}
	
	private boolean checkAdjacentButtons(Button b, Button c){
		int lastButtonRow = GridPane.getRowIndex(c);
		int lastButtonCol = GridPane.getColumnIndex(c);
		int buttonRow = GridPane.getRowIndex(b);
		int buttonCol = GridPane.getColumnIndex(b);
		if((buttonRow >= lastButtonRow - 1 && buttonRow <= lastButtonRow + 1) &&
				(buttonCol >= lastButtonCol - 1 && buttonCol <= lastButtonCol + 1))
			return true;
		else
			return false;
	}
	
	/*public boolean checkLetterAdjacency(String letter){
		Hashtable<Button, Boolean> hasAdjacent = new Hashtable<>();
		if(currentWord.length() < 1)
			return true;
		boolean hasAdjacency = false;
		for(Node b : grid.getChildren()){
			
			if(((Button)b).getText().toLowerCase().charAt(0) == currentWord.charAt(currentWord.length() - 1)){
				hasAdjacent.put((Button)b, false);
				for(Node c : grid.getChildren()){
					if(((Button)c).getText().toLowerCase().equals(letter) && checkAdjacentButtons((Button)b, (Button)c)){
						c.setEffect(new DropShadow(30 + 2, Color.LIGHTGREEN));
						hasAdjacency = true;
						hasAdjacent.replace((Button)b, true);
					}
				}
			}
		}
		if(hasAdjacency){
			for(Button k : hasAdjacent.keySet())
				if(hasAdjacent.get(k) == false)
					k.setEffect(null);
		}
		return hasAdjacency;
	}*/

	public String getCurrentWord() {
		return currentWord;
	}
	
	public ObservableList<String> getFoundWords(){
		return foundWords;
	}
	
	public void clearAll(){
		currentWord = "";
		for(Node b : grid.getChildren())
			b.setEffect(null);
		pane.getChildren().removeAll(pathLines);
		pathLines.clear();
		pane.getChildren().remove(lineDraw);
		lineDraw = null;
		//tetherPaths.clear();
		lastButton = null;
		selectedButtons.clear();
	}
	
	public void displayPaths(String word){
		if(showPaths != null)
			showPaths.stop();
		tetherPaths.clear();
		clearAll();
		checkPathExists(word);
		for(Node b : tetherPaths.get(0)){
			b.setEffect(new DropShadow(30 + 2, Color.LIGHTGREEN));
		}
		for(int k = 0; k < tetherPaths.get(0).size() - 1; k++){
			addTether(tetherPaths.get(0).get(k), tetherPaths.get(0).get(k+1));
		}
		if(tetherPaths.size() > 1){
			showPaths = new AnimationTimer(){
				long timer = TimeUnit.MILLISECONDS.toNanos(500);
				long lastSwitch = System.nanoTime();
				int currentPath = 0;
				
				@Override
				public void start(){
					super.start();
				}
	
				@Override
				public void handle(long arg0) {
					// TODO Auto-generated method stub
					if(arg0 - lastSwitch >= timer){
						lastSwitch = arg0;
						currentPath++;
						if(currentPath >= tetherPaths.size()){
							currentPath = 0;
						}
						clearAll();
						for(Button b : tetherPaths.get(currentPath)){
							b.setEffect(new DropShadow(30 + 2, Color.LIGHTGREEN));
						}
						for(int k = 0; k < tetherPaths.get(currentPath).size() - 1; k++){
							addTether(tetherPaths.get(currentPath).get(k), tetherPaths.get(currentPath).get(k+1));
						}
					}
				}
				
				@Override
				public void stop(){
					clearAll();
					super.stop();
				}
				
			};
			showPaths.start();
		}
	}
	
	
}
