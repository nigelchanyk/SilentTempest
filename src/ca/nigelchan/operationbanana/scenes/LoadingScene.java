package ca.nigelchan.operationbanana.scenes;

import ca.nigelchan.operationbanana.managers.SceneManager;
import ca.nigelchan.operationbanana.resources.LoadingResource;

public class LoadingScene extends BaseScene {
	
	private LoadingResource resource;

	public LoadingScene(SceneManager manager) {
		super(manager);
		resource = new LoadingResource(activity);
		setResource(resource);
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
