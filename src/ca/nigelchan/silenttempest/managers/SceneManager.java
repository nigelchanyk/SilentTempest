package ca.nigelchan.silenttempest.managers;

import java.util.Stack;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.ui.activity.BaseGameActivity;

import ca.nigelchan.silenttempest.asynctasks.AsyncSceneLoader;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.scenes.BaseScene;
import ca.nigelchan.silenttempest.scenes.ForegroundScene;
import ca.nigelchan.silenttempest.scenes.LoadingScene;

public class SceneManager {

	private BaseGameActivity activity;
	private Camera camera;
	private Engine engine;
	private LoadingScene loadingScene;
	private Stack<BaseScene> sceneStack = new Stack<BaseScene>();
	
	public SceneManager(BaseGameActivity activity, Camera camera, Engine engine, CommonResource resource) {
		this.activity = activity;
		this.camera = camera;
		this.engine = engine;
		loadingScene = new LoadingScene(this, resource);
		loadingScene.prepareScene();
	}
	
	public void onBackKeyDown() {
		if (sceneStack.empty())
			return;
		sceneStack.peek().onBackKeyPressed();
	}
	
	public void popScene() {
		BaseScene scene = sceneStack.pop();
		switch (scene.getDefualtPopOperation()) {
		case UNLOAD:
			scene.unloadResources();
			break;
		case DISPOSE:
			scene.disposeScene();
			break;
		default:
		}
		
		if (sceneStack.empty())
			return;
		prepareScene(sceneStack.peek(), loadingScene);
		engine.setScene(sceneStack.peek());
	}

	public void pushScene(BaseScene scene) {
		pushScene(scene, loadingScene);
	}
	
	public void pushScene(BaseScene scene, ForegroundScene loadingScene) {
		if (!sceneStack.empty()) {
			if (!sceneStack.peek().isInactiveResourceAllowed())
				sceneStack.peek().unloadResources();
		}

		camera.setHUD(null);
		sceneStack.push(scene);
		if (scene.isResourceLoaded())
			return;
		prepareScene(scene, loadingScene);
		engine.setScene(sceneStack.peek());
	}
	
	private void prepareScene(final BaseScene scene, final ForegroundScene loadingScene) {
		if (scene.isResourceLoaded())
			return;
		if (scene.isLoadAsynchronous()) {
			pushScene(loadingScene);
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					new AsyncSceneLoader(scene, loadingScene).execute();
				}
			});
		}
		else {
			scene.prepareScene();
		}
	}

	// Getters
	public BaseGameActivity getActivity() {
		return activity;
	}

	public Camera getCamera() {
		return camera;
	}

	public Engine getEngine() {
		return engine;
	}
}
