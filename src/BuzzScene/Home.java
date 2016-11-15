package BuzzScene;

import java.util.ArrayList;
import javafx.event.ActionEvent;

import Buzzword.BuzzGrid;
import Buzzword.BuzzObject;
import gui.Workspace;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

//@author Jeremy Chu

public class Home extends BuzzScene{
	
	ArrayList<BuzzObject> profileObjects;
	
	public BuzzObject home = new BuzzObject("Home", new FlowPane(), 100, 20);
	public BuzzObject profile = new BuzzObject("Profile", new FlowPane(), 200, 20);
	public BuzzObject levelSelect = new BuzzObject("LevelSelect", new FlowPane(), 300, 20);
	public BuzzObject gameplay = new BuzzObject("Gameplay", new FlowPane(), 400, 20);
	
	private boolean loginUp = false;
	private double btn2Pos = 150;
	
	public Home(){
		BuzzObject exit = new BuzzObject("Exit", new FlowPane(), 775, 0);
		Button exitB = new Button("X");
		exitB.setStyle("-fx-base: dimgrey");
		exit.addNode("Button", exitB);
		addGlobal(exit);
		buzzObjects = new ArrayList<>();
		profileObjects = new ArrayList<>();
		generateSplashScreen();
		generateLoginScreen();
		//generateHomeScreen();
		Button homeB = new Button("Home");
		homeB.setOnAction(e -> {
			if(find("GameModeSelect") == null)
				generateHomeScreen();
			//if(Workspace.getSM().getHome().equals(Workspace.getSM().getScene()))
				Workspace.getSM().loadScene(Workspace.getSM().getHome());
		});
		home.addNode("Button", homeB);
		Button levelB = new Button("Level");
		levelB.setOnAction(e -> {
				Workspace.getSM().loadScene(Workspace.getSM().getLevelSelect());
		});
		levelSelect.addNode("Button", levelB);
		Button profileB = new Button("Profile");
		profileB.setOnAction(e -> {
			if(!loginUp && Workspace.getSM().getHome().equals(Workspace.getSM().getScene()))
				loadLogin();
		});
		profile.addNode("Button", profileB);
		Button gameplayB = new Button("Gameplay");
		gameplayB.setOnAction(e -> {
			Workspace.getSM().loadScene(Workspace.getSM().getGameplay());
		});
		gameplay.addNode("Button", gameplayB);
		addGlobal(home);
		addGlobal(profile);
		addGlobal(levelSelect);
		addGlobal(gameplay);
	}
	
	private void generateSplashScreen(){
		unloadLogin();
		BuzzObject title = new BuzzObject("Title", new FlowPane(), 325, 50);
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
		addGlobal(profileButton);
		BuzzObject loginButton = new BuzzObject("Button2", new FlowPane(), 100, 150);
		Button login = new Button("Login");
		login.setMinWidth(120);
		login.setStyle("-fx-base: dimgray");
		login.setAlignment(Pos.CENTER_LEFT);
		loginButton.addNode("Button", login);
		addGlobal(loginButton);
		BuzzGrid grid = new BuzzGrid("SplashGrid", 450, 300);
		grid.constructHomeGrid(4,4);
		buzzObjects.add(grid);
	}
	
	private void generateHomeScreen(){
		unloadLogin();
		btn2Pos = 200;
		BuzzObject b1 = find("Button1");
		Button button1 = (Button)(b1.getNode("Button"));
		button1.setText("ShujuLong");
		BuzzObject gamemodeSelect = new BuzzObject("GameModeSelect", new FlowPane(), 100, 150);
		ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList(
				"Select Mode", "English Dictionary", "Places", "Science", "Sanic Memes")
				);
		cb.setValue("Select Mode");
		cb.setMinWidth(120);
		cb.setStyle("-fx-base: dimgray");
		gamemodeSelect.addNode("ChoiceBox", cb);
		buzzObjects.add(gamemodeSelect);
		//gamemodeSelect.loadNodes();
		BuzzObject b2 = find("Button2");
		b2.setY(btn2Pos);
		Button button2 = (Button)(b2.getNode("Button"));
		button2.setText("Start Playing");
	}
	
	private void generateLoginScreen(){
		BuzzObject rectangle = new BuzzObject("LoginSquare", new FlowPane(), 300, 200);
		rectangle.addNode("Rectangle", new Rectangle(300, 100));
		profileObjects.add(rectangle);
		BuzzObject username = new BuzzObject("UserName", new FlowPane(), 350, 200);
		username.addNode("Label", new Label("Username: "));
		username.addNode("TextField", new TextField());
		profileObjects.add(username);
		BuzzObject password = new BuzzObject("Password", new FlowPane(), 350, 250);
		password.addNode("Label", new Label("Password: "));
		password.addNode("TextField", new PasswordField());
		profileObjects.add(password);
	}

	@Override
	public void load() {
		unloadLogin();
		for(BuzzObject bz : buzzObjects){
			bz.loadNodes();
		}
		BuzzObject b2 = find("Button2");
		b2.setY(btn2Pos);
		Button button2 = (Button)(b2.getNode("Button"));
		if(b2.getY() == 200)
			button2.setText("Start Playing");
	}
	
	public void loadLogin(){
		for(BuzzObject bz : profileObjects){
			bz.loadNodes();
		}
		loginUp = true;
	}
	
	public void unloadLogin(){
		for(BuzzObject bz : profileObjects){
			bz.unloadNodes();
		}
		loginUp = false;
	}

	@Override
	public void unload() {
		unloadLogin();
		for(BuzzObject bz : buzzObjects){
			bz.unloadNodes();
		}
	}

}
