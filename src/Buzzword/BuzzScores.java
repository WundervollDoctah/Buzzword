package Buzzword;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class BuzzScores extends BuzzObject {
	
	public StackPane wordsPane;
	public StackPane scoresPane;
	public FlowPane totalScore;
	
	public BuzzScores(String name){
		this(name, 0, 0);
	}
	
	public BuzzScores(String name, double x, double y){
		super(name, new FlowPane(), x, y);
	}
	
	public void constructBuzzScores(){
		VBox vBox = new VBox();
		HBox hBox = new HBox();
		Rectangle rect1 = new Rectangle(50, 200);
		rect1.setStroke(Color.WHITE);
		Rectangle rect2 = new Rectangle(50, 200);
		rect2.setStroke(Color.WHITE);
		StackPane wordsStack = new StackPane();
		wordsStack.getChildren().add(rect1);
		StackPane scoresStack = new StackPane();
		scoresStack.getChildren().add(rect2);
		VBox words = new VBox();
		VBox scores = new VBox();
		words.getChildren().add(new Text(" SANIC"));
		words.getChildren().add(new Text(" FAST"));
		words.getChildren().add(new Text(" GO"));
		words.getChildren().add(new Text(" FANG"));
		scores.getChildren().add(new Text(" 20"));
		scores.getChildren().add(new Text(" 20"));
		scores.getChildren().add(new Text(" 9"));
		scores.getChildren().add(new Text(" 20"));
		wordsStack.getChildren().add(words);
		scoresStack.getChildren().add(scores);
		for(Node text : scores.getChildren())
			((Text)text).setFill(Color.WHITESMOKE);
		for(Node text : words.getChildren())
			((Text)text).setFill(Color.WHITESMOKE);
		hBox.getChildren().addAll(wordsStack, scoresStack);
		StackPane total= new StackPane();
		Text totalText = new Text("Total: 69");
		totalText.setFill(Color.WHITESMOKE);
		total.getChildren().addAll(new Rectangle(100, 50), totalText);
		vBox.getChildren().addAll(hBox, total);
		pane.getChildren().add(vBox);
	}
}
