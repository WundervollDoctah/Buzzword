package Buzzword;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

//@author Jeremy Chu

public class BuzzScores extends BuzzObject {
	
	private TableView<WordScore> table;
	public FlowPane totalScore;
	private ObservableList<WordScore> words;
	private int score;
	
	public BuzzScores(String name){
		this(name, 0, 0);
	}
	
	public BuzzScores(String name, double x, double y){
		super(name, new FlowPane(), x, y);
	}
	
	public void constructBuzzScores(){
		words = FXCollections.observableList(new ArrayList<WordScore>());
		VBox vBox = new VBox();
		table = new TableView<>();
		table.setEditable(true);
		TableColumn<WordScore, String> wordColumn = new TableColumn<WordScore, String>("words");
		wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
		TableColumn<WordScore, Integer> scoreColumn = new TableColumn<WordScore, Integer>("scores");
		scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
		table.getColumns().setAll(wordColumn, scoreColumn);
		table.setMaxHeight(200);
		table.setMaxWidth(158);
		words.add(new WordScore("SANIC", 20));
		words.add(new WordScore("FAST", 20));
		words.add(new WordScore("GO", 9));
		words.add(new WordScore("FANG", 20));
		table.setItems(words);
		/*for(Node text : scores.getChildren())
			((Text)text).setFill(Color.WHITESMOKE);
		for(Node text : words.getChildren())
			((Text)text).setFill(Color.WHITESMOKE);*/
		
		StackPane total= new StackPane();
		Text totalText = new Text("Total Score: 0");
		words.addListener((ListChangeListener<WordScore>) (e ->{
			score = 0;
			for(WordScore ws : words)
				score += ws.getScore();
			totalText.setText("Total Score: " + score);
		}));;
		totalText.setFill(Color.WHITESMOKE);
		total.getChildren().addAll(new Rectangle(158, 50), totalText);
		vBox.getChildren().addAll(table, total);
		pane.getChildren().add(vBox);
	}
	
	public static class WordScore{
		private String word;
		private int score;
		
		public WordScore(String word, int score){
			this.word = word;
			this.score = score;
		}

		public String getWord() {
			return word;
		}

		public int getScore() {
			return score;
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof WordScore){
				return ((WordScore)o).word.equals(word);
			}
			return false;
		}
	}
	
	public ObservableList<WordScore> getWords(){
		return words;
	}

	public int getScore() {
		return score;
	}
}
