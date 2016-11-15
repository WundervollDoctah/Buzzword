package gui;

import static settings.AppPropertyType.*;

import BuzzScene.SceneManager;
import components.AppWorkspaceComponent;
import apptemplate.AppTemplate;
import ui.AppGUI;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import propertymanager.PropertyManager;

public class Workspace extends AppWorkspaceComponent{

	private AppTemplate app;
	private static AppGUI gui;
	private static SceneManager sm;
	
	public Workspace(AppTemplate initApp){
		app = initApp;
		gui = initApp.getGUI();
		sm = new SceneManager(gui);
		sm.createScenes();
		layoutGUI();
	}
	
	public void layoutGUI(){
		sm.loadScene(sm.getHome());
		//sm.loadScene(sm.getLevelSelect());
		//sm.loadScene(sm.getGameplay());
	}
	
	@Override
	public void initStyle() {
		PropertyManager propertyManager = PropertyManager.getManager();
		gui.getAppPane().setStyle("-fx-background-color: dimgrey");
		Rectangle sideRectangle = new Rectangle(0, 0, 150, Integer.parseInt(propertyManager.getPropertyValue(APP_WINDOW_HEIGHT)));
		sideRectangle.setFill(Color.GRAY);
		gui.getAppPane().getChildren().add(sideRectangle);
		sideRectangle.toBack();
	}

	@Override
	public void reloadWorkspace() {
		// TODO Auto-generated method stub
		
	}
	
	public static SceneManager getSM(){
		return sm;
	}

}
