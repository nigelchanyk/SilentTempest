package ca.nigelchan.silenttempest.scenes;

import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.resources.MainMenuResource;

public class MainMenuScene extends BaseScene {
	
	private MainMenuResource resource;

	public MainMenuScene(SceneManager manager) {
		super(manager);
		resource = new MainMenuResource(activity);
		setResource(resource);
		loadAsynchronous = true;
		inactiveResourceAllowed = false;
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createScene() {
		// TODO Auto-generated method stub
		
	}
}
