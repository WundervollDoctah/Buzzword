package Buzzword;

import javafx.scene.layout.GridPane;

public class BuzzGrid extends BuzzObject {

	public BuzzGrid(String name) {
		this(name, 0, 0);
	}
	
	public BuzzGrid(String name, double x, double y) {
		super(name, new GridPane(), x, y);
	}

}
