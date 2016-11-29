package BuzzScene;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;

import Buzzword.BuzzGrid;
import Buzzword.BuzzObject;
import Profile.ProfileException;
import Profile.ProfileManager;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ui.AppMessageDialogSingleton;
import ui.YesNoCancelDialogSingleton;

//@author Jeremy Chu

public class Home extends BuzzScene{
	
	ArrayList<BuzzObject> profileObjects;
	public static final String[] GAMEMODES = new String[]{"Select Mode", "English Dictionary", "Places", "Science", "Sanic Memes"};
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
		exitB.setOnAction(e -> {
			if(Workspace.getSM().getScene() == Workspace.getSM().getGameplay()){
				Workspace.getSM().getGameplay().quitPause();
				YesNoCancelDialogSingleton yn = YesNoCancelDialogSingleton.getSingleton();
				yn.show("Exit Game", "You have a game in progress, are you sure you want to quit?");
				String result = yn.getSelection();
				if(result.equals(YesNoCancelDialogSingleton.YES))
					System.exit(0);
				else
					Workspace.getSM().getGameplay().quitResume();
			}
			else
				System.exit(0);
		});
		exit.addNode("Button", exitB);
		addGlobal(exit);
		buzzObjects = new ArrayList<>();
		profileObjects = new ArrayList<>();
		generateSplashScreen();
		generateLoginScreen();
		//generateHomeScreen();
		/*Button homeB = new Button("Home");
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
		addGlobal(gameplay);*/
	}
	
	private void revertToSplash(){
		btn2Pos = 150;
		BuzzObject createButton = find("Button1");
		Button button1 = createButton.<Button>getNode("Button");
		button1.setText("Create New Profile");
		button1.setGraphic(null);
		button1.setOnAction(e -> {
			loadLogin();
			find("CreateProfile").<Button>getNode("Button").setVisible(true);
			find("LoginButton").<Button>getNode("Button").setVisible(false);
		});
		if(find("GameModeSelect") != null)
			find("GameModeSelect").unloadNodes();
		buzzObjects.remove(find("GameModeSelect"));
		BuzzObject loginButton = find("Button2");
		loginButton.setY(btn2Pos);
		Button login = loginButton.<Button>getNode("Button");
		login.setText("Login");
		login.setDisable(false);
		login.setOnAction(e -> {
			loadLogin();
			find("CreateProfile").<Button>getNode("Button").setVisible(false);
			find("LoginButton").<Button>getNode("Button").setVisible(true);
		});
	}
	
	private void revertToHome(){
		unloadLogin();
		btn2Pos = 200;
		BuzzObject b1 = find("Button1");
		Button button1 = (Button)(b1.getNode("Button"));
		//button1.setText("ShujuLong");
		button1.setOnAction(e -> {
			Workspace.getSM().getGameplay().quitPause();
			YesNoCancelDialogSingleton.getSingleton().show("Logout", "Do you want to log out?");
			String result = YesNoCancelDialogSingleton.getSingleton().getSelection();
			if(result.equals(YesNoCancelDialogSingleton.YES)){
				if(Workspace.getSM().getGameplay() == Workspace.getSM().getScene()){
					YesNoCancelDialogSingleton.getSingleton().show("Exit Game", "This will exit the game. Are you sure?");
					result = YesNoCancelDialogSingleton.getSingleton().getSelection();
					if(result.equals(YesNoCancelDialogSingleton.YES)){
						Workspace.getSM().loadScene(Workspace.getSM().getHome());
						revertToSplash();
					}
				}
				Workspace.getSM().loadScene(Workspace.getSM().getHome());
				revertToSplash();
			}
		});
		buzzObjects.add(find("GamemodeSelect"));
		checkChoice(find("GamemodeSelect").<ChoiceBox<String>>getNode("ChoiceBox"));
		//gamemodeSelect.loadNodes();
		BuzzObject b2 = find("Button2");
		b2.setY(btn2Pos);
		Button button2 = b2.<Button>getNode("Button");
		button2.setText("Start Playing");	
		button2.setOnAction(e -> {
			Workspace.getSM().loadScene(Workspace.getSM().getLevelSelect());
		});
		button2.setDisable(true);
	}
	
	private void generateSplashScreen(){
		//unloadLogin();
		BuzzObject title = new BuzzObject("Title", new FlowPane(), 325, 50);
		Label t = new Label("!! BUZZWORD !!");
		t.setTextFill(Color.WHITE);
		t.setFont(Font.font(40));
		title.addNode("Text", t);
		addGlobal(title);
		BuzzObject profileButton = new BuzzObject("Button1", new FlowPane(), 100, 100);
		Button nP = new Button("Create New Profile");
		nP.setOnAction(e -> {
			loadLogin();
			find("CreateProfile").<Button>getNode("Button").setVisible(true);
			find("LoginButton").<Button>getNode("Button").setVisible(false);
		});
		nP.setMinWidth(120);
		nP.setStyle("-fx-base: dimgray");
		nP.setAlignment(Pos.CENTER_LEFT);
		profileButton.addNode("Button", nP);
		addGlobal(profileButton);
		BuzzObject loginButton = new BuzzObject("Button2", new FlowPane(), 100, 150);
		Button login = new Button("Login");
		login.setOnAction(e -> {
			loadLogin();
			find("CreateProfile").<Button>getNode("Button").setVisible(false);
			find("LoginButton").<Button>getNode("Button").setVisible(true);
		});
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
		button1.setOnAction(e -> {
			Workspace.getSM().getGameplay().quitPause();
			YesNoCancelDialogSingleton.getSingleton().show("Logout", "Do you want to log out?");
			String result = YesNoCancelDialogSingleton.getSingleton().getSelection();
			if(result.equals(YesNoCancelDialogSingleton.YES)){
				if(Workspace.getSM().getGameplay() == Workspace.getSM().getScene()){
					YesNoCancelDialogSingleton.getSingleton().show("Exit Game", "This will exit the game. Are you sure?");
					result = YesNoCancelDialogSingleton.getSingleton().getSelection();
					if(result.equals(YesNoCancelDialogSingleton.YES)){
						Workspace.getSM().loadScene(Workspace.getSM().getHome());
						revertToSplash();
					}
					else
						Workspace.getSM().getGameplay().quitResume();
				}
				else{
					Workspace.getSM().loadScene(Workspace.getSM().getHome());
					revertToSplash();
				}
			}
			else
				Workspace.getSM().getGameplay().quitResume();
		});
		BuzzObject gamemodeSelect = new BuzzObject("GameModeSelect", new FlowPane(), 100, 150);
		ChoiceBox<String> cb = new ChoiceBox<>(FXCollections.observableArrayList(
			GAMEMODES)
		);		
		cb.setValue("Select Mode");
		cb.getSelectionModel().selectedItemProperty().addListener(e -> {
			checkChoice(cb);
		});
		cb.setMinWidth(120);
		cb.setStyle("-fx-base: dimgray");
		gamemodeSelect.addNode("ChoiceBox", cb);
		buzzObjects.add(gamemodeSelect);
		checkChoice(cb);
		//gamemodeSelect.loadNodes();
		BuzzObject b2 = find("Button2");
		b2.setY(btn2Pos);
		Button button2 = b2.<Button>getNode("Button");
		button2.setText("Start Playing");	
		button2.setOnAction(e -> {
			Workspace.getSM().loadScene(Workspace.getSM().getLevelSelect());
		});
		button2.setDisable(true);
	}
	
	private void generateLoginScreen(){
		ProfileManager.getProfileManager();
		BuzzObject rectangle = new BuzzObject("LoginSquare", new FlowPane(), 300, 200);
		rectangle.addNode("Rectangle", new Rectangle(300, 100));
		profileObjects.add(rectangle);
		BuzzObject username = new BuzzObject("Username", new FlowPane(), 350, 200);
		Label usernameText = new Label("Username: ");
		usernameText.setTextFill(Color.WHITE);
		username.addNode("Label", usernameText);
		username.addNode("TextField", new TextField());
		profileObjects.add(username);
		BuzzObject password = new BuzzObject("Password", new FlowPane(), 350, 250);
		Label passwordText = new Label("Password: ");
		passwordText.setTextFill(Color.WHITE);
		password.addNode("Label", passwordText);
		password.addNode("TextField", new PasswordField());
		profileObjects.add(password);
		BuzzObject createProfile = new BuzzObject("CreateProfile", new FlowPane(), 350, 300);
		Button createProfileButton = new Button("Create Profile");
		createProfileButton.setOnAction(e -> { createProfile(); });
		createProfile.addNode("Button", createProfileButton);
		profileObjects.add(createProfile);
		BuzzObject login = new BuzzObject("LoginButton", new FlowPane(), 450, 300);
		Button loginButton = new Button("Login");
		loginButton.setOnAction(e -> { login(); });
		login.addNode("Button", loginButton);
		profileObjects.add(login);
		BuzzObject closeLogin = new BuzzObject("CloseLogin", new FlowPane(), 575, 200);
		Button clb = new Button("X");
		clb.setStyle("-fx-base: dimgray");
		clb.setOnAction(e -> {
			unloadLogin();
		});
		closeLogin.addNode("Button", clb);
		profileObjects.add(closeLogin);
	}
	
	private void createProfile(){
		ProfileManager profileManager = ProfileManager.getProfileManager();
		String username = find("Username").<TextField>getNode("TextField").getText();
		String password = find("Password").<PasswordField>getNode("TextField").getText();
		try{
			if(profileManager.createData(username, password)){
				unloadLogin();
				find("Button1").<Button>getNode("Button").setDisable(false);
				find("Button2").<Button>getNode("Button").setDisable(false);
				generateHomeScreen();
				Workspace.getSM().loadScene(Workspace.getSM().getHome());
				Button profileButton = find("Button1").<Button>getNode("Button");
				profileButton.setText(ProfileManager.getProfileManager().getLoadedProfile().getUsername());
				/*profileButton.setOnAction(e -> {
					
				});*/
			}
			else
				AppMessageDialogSingleton.getSingleton().show("Profile Exists", "Profile already exists");
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	private void login(){
		ProfileManager profileManager = ProfileManager.getProfileManager();
		String username = find("Username").<TextField>getNode("TextField").getText();
		String password = find("Password").<PasswordField>getNode("TextField").getText();
		try{
			if(profileManager.loadProfile(username, password)){
				unloadLogin();
				find("Button1").<Button>getNode("Button").setDisable(false);
				find("Button2").<Button>getNode("Button").setDisable(false);
				generateHomeScreen();
				Workspace.getSM().loadScene(Workspace.getSM().getHome());
				Button profileButton = find("Button1").<Button>getNode("Button");
				profileButton.setText(ProfileManager.getProfileManager().getLoadedProfile().getUsername());
				Polygon arrow = new Polygon(new double[]{
						0,5,
						0,15,
						10,15,
						10,20,
						20,10,
						10,0,
						10,5});
				arrow.setFill(Color.WHITESMOKE);
				profileButton.setGraphic(arrow);
				/*profileButton.setOnAction(e -> {
					
				});*/
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		catch(ProfileException ex){
			AppMessageDialogSingleton.getSingleton().show("Login Error", ex.getMessage());
		}
	}
	
	private void checkChoice(ChoiceBox<String> cb){
		System.out.println(cb.getSelectionModel().getSelectedItem());
		Workspace.getSM().gamemode = cb.getSelectionModel().getSelectedItem();
		if(Workspace.getSM().gamemode.equals("Select Mode"))
			find("Button2").<Button>getNode("Button").setDisable(true);
		else
			find("Button2").<Button>getNode("Button").setDisable(false);
	}

	@Override
	public void load() {
		SceneManager.currentGameState = SceneManager.gameState.home;
		unloadLogin();
		for(BuzzObject bz : buzzObjects){
			bz.loadNodes();
		}
		BuzzObject b2 = find("Button2");
		b2.setY(btn2Pos);
		Button button2 = (Button)(b2.getNode("Button"));
		if(b2.getY() == 200){
			button2.setText("Start Playing");
			button2.setOnAction(e -> {
				Workspace.getSM().loadScene(Workspace.getSM().getLevelSelect());
			});
		}
	}
	
	public void loadLogin(){
		find("Button1").<Button>getNode("Button").setDisable(true);
		find("Button2").<Button>getNode("Button").setDisable(true);
		BuzzObject gms = find("GameModeSelect");
		if(gms != null)
			gms.<ChoiceBox<String>>getNode("ChoiceBox").setDisable(true);
		for(BuzzObject bz : profileObjects){
			bz.loadNodes();
		}
		find("Username").<TextField>getNode("TextField").clear();
		find("Password").<TextField>getNode("TextField").clear();
		loginUp = true;
	}
	
	public void unloadLogin(){
		find("Button1").<Button>getNode("Button").setDisable(false);
		for(BuzzObject bz : profileObjects){
			bz.unloadNodes();
		}
		if(find("GameModeSelect") == null)
			find("Button2").<Button>getNode("Button").setDisable(false);
		loginUp = false;
	}

	@Override
	public void unload() {
		unloadLogin();
		for(BuzzObject bz : buzzObjects){
			bz.unloadNodes();
		}
	}
	
	@Override
	public BuzzObject find(String name){
		for(BuzzObject bz : buzzObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		for(BuzzObject bz : profileObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		for(BuzzObject bz : globalBuzzObjects){
			if(bz.getName().equals(name))
				return bz;
		}
		return null;
	}

}
