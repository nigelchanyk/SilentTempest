package ca.nigelchan.operationbanana.managers;

import java.util.Stack;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.activity.BaseGameActivity;

import ca.nigelchan.operationbanana.scenes.BaseScene;
import ca.nigelchan.operationbanana.scenes.LoadingScene;

public class SceneManager {

	private BaseGameActivity activity;
	private Camera camera;
	private Engine engine;
	private LoadingScene loadingScene;
	private Stack<BaseScene> sceneStack = new Stack<BaseScene>();
	
	public SceneManager(BaseGameActivity activity, Camera camera, Engine engine) {
		this.activity = activity;
		this.camera = camera;
		this.engine = engine;
		loadingScene = new LoadingScene(this);
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
		sceneStack.peek().prepareScene();
		engine.setScene(sceneStack.peek());
	}
	
	public void pushScene(final BaseScene scene) {
		if (!sceneStack.empty()) {
			if (!sceneStack.peek().isInactiveResourceAllowed())
				sceneStack.peek().unloadResources();
		}

		camera.setHUD(null);
		sceneStack.push(scene);
		if (scene.isResourceLoaded())
			return;
		if (scene.isLoadAsynchronous()) {
			pushScene(loadingScene);
			engine.registerUpdateHandler(new TimerHandler(0.3f, new ITimerCallback() {
				
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					engine.unregisterUpdateHandler(pTimerHandler);
					scene.prepareScene();
					popScene();
					engine.setScene(scene);
				}
			}));
		}
		else {
			scene.prepareScene();
			engine.setScene(scene);
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
