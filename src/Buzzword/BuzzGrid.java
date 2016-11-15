package Buzzword;

import javafx.geometry.Pos;
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
	
	private GridPane grid;

	public BuzzGrid(String name) {
		this(name, 0, 0);
	}
	
	public BuzzGrid(String name, double x, double y) {
		super(name, new StackPane(), x, y);
		grid = new GridPane();
		grid.setHgap(20);
		grid.setVgap(20);
		pane.getChildren().add(grid);
	}
	
	public void constructHomeGrid(int width, int height){
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
			}
		}
	}
	
	public void constructMemeGrid(int width, int height){
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
}
