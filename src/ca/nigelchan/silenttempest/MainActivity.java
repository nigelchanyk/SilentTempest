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
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.data.EventsData;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.data.actors.ActorConfiguration;
import ca.nigelchan.silenttempest.importer.EventImporter;
import ca.nigelchan.silenttempest.importer.WorldImporter;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.scenes.GameScene;
import ca.nigelchan.silenttempest.scenes.SplashScene;

public class MainActivity extends BaseGameActivity {
	
	private Camera camera;
	private CommonResource commonResource;
	private SceneManager manager;
	private SplashScene splash;

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
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		splash = new SplashScene(this, camera);
		mEngine.setScene(splash);
		mEngine.registerUpdateHandler(new FPSLogger());
		pOnCreateSceneCallback.onCreateSceneFinished(splash);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	@Override
	public void onPopulateScene(
		Scene pScene,
		OnPopulateSceneCallback pOnPopulateSceneCallback
	) throws Exception {
		final AsyncTask<Void, Integer, Boolean> asyncLoader = new AsyncTask<Void, Integer, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				long start = System.nanoTime();
				commonResource = new CommonResource(MainActivity.this);
				commonResource.load();
				manager = new SceneManager(MainActivity.this, camera, mEngine, commonResource);
				long duration = System.nanoTime() - start;
				if (duration >= 2000000000)
					buildFirstScene();
				else {
					// Splash screen should last for at least 2 seconds
					float remainingTime = (2000000000 - duration) / 1000000000f;
					mEngine.registerUpdateHandler(new TimerHandler(remainingTime, new ITimerCallback() {
						
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							mEngine.unregisterUpdateHandler(pTimerHandler);
							buildFirstScene();
						}
					}));
				}
				return true;
			}
			
		};
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				asyncLoader.execute();
			}
		});
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	private void buildFirstScene() {
		splash.dispose();
		//manager.pushScene(new MainMenuScene(manager, commonResource));
		ActorConfiguration actorConfiguration = new ActorConfiguration();
		try {
			WorldData worldData = WorldImporter.load("levels/sample.stl", MainActivity.this, actorConfiguration);
			EventsData eventsData = EventImporter.load("levels/sample.stl", MainActivity.this);
			manager.pushScene(new GameScene(manager, commonResource, worldData, eventsData));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
	}
	
}
