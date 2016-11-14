package BuzzScene;

import java.util.ArrayList;
import Buzzword.BuzzObject;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Home extends BuzzScene{
	
	
	public Home(){
		buzzObjects = new ArrayList<>();
		generateSplashScreen();
		generateHomeScreen();
	}
	
	private void generateSplashScreen(){
		BuzzObject title = new BuzzObject("Title", new FlowPane(), 350, 50);
		Label t = new Label("!! BUZZWORD !!");
		t.setTextFill(Color.WHITE);
		t.setFont(Font.font(40));
		title.addNode("Text", t);
		addGlobal(title);
		BuzzObject profileButton = new BuzzObject("Button1", new FlowPane(), 100, 100);
		Button nP = new Button("Create New Profile");
		nP.setMinWidth(120);
		nP.setStyle("-fx-base: dimgray");
		nP.setAlignment(Pos.CENTER_LEFT);
		profileButton.addNode("Button", nP);
		buzzObjects.add(profileButton);
		BuzzObject loginButton = new BuzzObject("Button2", new FlowPane(), 100, 150);
		Button login = new Button("Login");
		login.setMinWidth(120);
		login.setStyle("-fx-base: dimgray");
		login.setAlignment(Pos.CENTER_LEFT);
		loginButton.addNode("Button", login);
		buzzObjects.add(loginButton);
	}
	
	private void generateHomeScreen(){
		BuzzObject b1 = find("Button1");
		Button button1 = (Button)(b1.getNode("Button"));
		button1.setText("ShujuLong");
		BuzzObject gamemodeSelect = new BuzzObject("GameModeSelect", new FlowPane(), 100, 150);
		ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList(
				"Select Mode", "English Dictionary", "Places", "Science", "Famous People")
				);
		cb.setValue("Select Mode");
		cb.setMinWidth(120);
		cb.setStyle("-fx-base: dimgray");
		gamemodeSelect.addNode("ChoiceBox", cb);
		buzzObjects.add(gamemodeSelect);
		//gamemodeSelect.loadNodes();
		BuzzObject b2 = find("Button2");
		b2.setY(200);
		Button button2 = (Button)(b2.getNode("Button"));
		button2.setText("Start Playing");
	}

	@Override
	public void load() {
		for(BuzzObject bz : buzzObjects){
			bz.loadNodes();
		}
		
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub
		
	}

}
