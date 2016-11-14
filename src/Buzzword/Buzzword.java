package Buzzword;

import BuzzScene.BuzzScene;
import apptemplate.AppTemplate;
import Profile.*;
import components.AppComponentsBuilder;
import components.AppDataComponent;
import components.AppFileComponent;
import components.AppWorkspaceComponent;
import gui.Workspace;

public class Buzzword extends AppTemplate{
		
	public static void main(String[] args) {
        launch(args);
    }

	@Override
	public AppComponentsBuilder makeAppBuilderHook() {
		return new AppComponentsBuilder() {
            @Override
            public AppDataComponent buildDataComponent() throws Exception {
            	//TODO
                return new Profile();
            }
    
            @Override
            public AppFileComponent buildFileComponent() throws Exception {
            	//TODO
                return new ProfileManager();
            }
    
            @Override
            public AppWorkspaceComponent buildWorkspaceComponent() throws Exception {
                return new Workspace(Buzzword.this);
            }
        };
	}
	
	
}
