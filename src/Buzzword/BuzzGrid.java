package Buzzword;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import BuzzScene.Home;
import Profile.ProfileManager;
import gui.Workspace;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

//@author Jeremy Chu

public class BuzzGrid extends BuzzObject {
	
	private static final int TOTAL_NUMBER_OF_STORED_WORDS    = 330622;
	private GridPane grid;
	private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private ArrayList<String> dictionary;
	private ArrayList<String> foundWords;
	private int gridWidth;
	private int gridHeight;
	private Canvas canvas;

	public BuzzGrid(String name) {
		this(name, 0, 0);
	}
	
	public BuzzGrid(String name, double x, double y) {
		super(name, new StackPane(), x, y);
		grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		pane.getChildren().add(grid);
		dictionary = new ArrayList<>();
		createDictionary();
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
		grid.getChildren().clear();
		gridWidth = width;
		gridHeight = height;
		int buttonSize = 60; 
		int gapSize = 20;
		Canvas canvas = new Canvas(width * buttonSize + (width - 1) * gapSize, height * buttonSize + (width - 1) * gapSize);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for(int i = 1; i < width; i++)
			for(int j = 1; j <= height; j++)
				gc.strokeLine(i*40 + i*20, j*40+(j-1)*40, i*40 + i*40 + 20, j*40+(j-1)*40);
		for(int i = 1; i <= width; i++)
			for(int j = 1; j < height; j++)
				gc.strokeLine(i*40 + (i-1)*40, j*60+(j-1)*20, i*40 + (i-1)*40, j*60+(j-1)*20+40);
		pane.getChildren().add(canvas);
		canvas.toBack();
		Button[] buttons = new Button[]{new Button("G"), new Button("O"), new Button("T"), new Button("T"),
				new Button("A"), new Button("G"), new Button("O"), new Button("S"),
				new Button("A"), new Button("N"), new Button("I"), new Button("C"),
				new Button("F"), new Button("A"), new Button("S"), new Button("T")};
		for(int i = 1; i <= height; i++){
			for(int j = 1; j <= width; j++){
				Button circle = buttons[((i-1) * 4) + j - 1];
				circle.setShape(new Circle(30));
				circle.setStyle("-fx-base: gray;"
						+ "-fx-background-radius: 20px;"
						+ "-fx-font-size: 25px;");
				circle.setAlignment(Pos.CENTER);
				grid.add(circle, j, i);
				
				circle.setMinSize(buttonSize, buttonSize);
				circle.setPrefSize(buttonSize, buttonSize);
				circle.setMaxSize(buttonSize, buttonSize);
			}
		}
		
	}
	
	public void generateGrid(){
		int targetScore = 50 + (Workspace.getSM().getDifficulty() - 1) * 10;
		int maxScore = 0;
		while(maxScore < targetScore){
			foundWords = new ArrayList<>();
			System.out.println("looped");
			for(Node button : grid.getChildren()){
				if(button instanceof Button)
					((Button)button).setText(Character.toString(alphabet[(int)(Math.random()*26)]));
			}
			for(int i = 1; i <= gridHeight; i++){
				for(int j = 1; j <= gridWidth; j++){
					maxScore += startWordCheck(i,j,new ArrayList<Node>(), "");
				}
			}
		}
	}
	
	private int startWordCheck(int i, int j, ArrayList<Node> markedNodes, String word){
		boolean foundNode = false;
		for(Node button : grid.getChildren()){
	        if(GridPane.getRowIndex(button) == i && GridPane.getColumnIndex(button) == j && !markedNodes.contains(button)) {
	            markedNodes.add(button);
	            word += ((Button)button).getText();
	            foundNode = true;
	            break;
	        }
		}
		Pattern p = Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(word);
		if(!foundNode || (word.length() > 1 && !m.find()) || word.length() > 8)
			return 0;
		//System.out.println(word);
		String rString = "";
		for(int k = word.length() - 1; k >= 0; k--){
			rString += word.charAt(k);
		}
		if(!checkPossibleWord(word.toLowerCase())){
			//System.out.println(word + " is bad");
			return 0;
		}
		else if(!foundWords.contains(word) && word.length() > 2 && checkIsWord(word.toLowerCase())){
			System.out.println(word + " is good");
			foundWords.add(word);
			return word.length() * 5;
		}
		int score = 0;
		ArrayList<Node> arrayCopy = new ArrayList<Node>();
		arrayCopy.addAll(markedNodes);
		score += startWordCheck(i+1, j, arrayCopy, word);
		score += startWordCheck(i, j+1, arrayCopy, word);
		score += startWordCheck(i-1, j, arrayCopy, word);
		score += startWordCheck(i, j-1, arrayCopy, word);
		return score;
	}
	
	private boolean checkPossibleWord(String s){
		//System.out.println(s);
		URL wordsResource = getClass().getClassLoader().getResource("words/sowpods.txt");
        assert wordsResource != null;
        //for(int i = 0; i < TOTAL_NUMBER_OF_STORED_WORDS; i++){
	        try (Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
	            //String retrievedWord = lines.skip(i).findFirst().get();
	            if(lines.anyMatch(string -> string.contains(s)))
	            	return true;
	        } catch (IOException | URISyntaxException e) {
	            e.printStackTrace();
	            System.exit(1);
	        }  
        //}
        return false;
	}
	
	private boolean checkIsWord(String s){
		if(dictionary.contains(s))
			return true;
		else
			return false;
	}
	
	private void createDictionary(){
		URL wordsResource = getClass().getClassLoader().getResource("words/sowpods.txt");
		assert wordsResource != null;
		try(Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
			lines.forEach(dictionary::add);
		} 
		catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setVisible(boolean value){
		pane.setVisible(value);
	}
}
