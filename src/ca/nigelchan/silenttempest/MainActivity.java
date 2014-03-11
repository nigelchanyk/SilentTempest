package ca.nigelchan.silenttempest;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONException;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.data.actors.ActorConfiguration;
import ca.nigelchan.silenttempest.importer.WorldImporter;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.scenes.BaseScene;
import ca.nigelchan.silenttempest.scenes.GameScene;
import ca.nigelchan.silenttempest.scenes.MainMenuScene;
import ca.nigelchan.silenttempest.scenes.SplashScene;

public class MainActivity extends BaseGameActivity {
	
	private Camera camera;
	private SceneManager manager;

	@Override
	public void onBackPressed() {
		manager.onBackKeyDown();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		camera = new Camera(0, 0, metrics.widthPixels, metrics.heightPixels);
		EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		options.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		options.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		manager = new SceneManager(this, camera, mEngine);
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		SplashScene splashScene = new SplashScene(manager);
		splashScene.setDefualtPopOperation(BaseScene.PopOperation.DISPOSE);
		splashScene.prepareScene();
		manager.pushScene(splashScene);
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				manager.popScene();
				// TODO Change to MainMenuScene
                CommonResource resource = new CommonResource(MainActivity.this);
                resource.load();
                manager.pushScene(new MainMenuScene(manager, resource));
                /*
				ActorConfiguration actorConfiguration = new ActorConfiguration();
				try {
					WorldData worldData = WorldImporter.load("levels/sample.stl", MainActivity.this, actorConfiguration);
                    manager.pushScene(new GameScene(manager, resource, worldData));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finish();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finish();
				}*/
			}

		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
}
