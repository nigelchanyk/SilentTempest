package ca.nigelchan.silenttempest;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.data.actors.EnemyData;
import ca.nigelchan.silenttempest.data.actors.PlayerData;
import ca.nigelchan.silenttempest.data.actors.sequences.MoveData;
import ca.nigelchan.silenttempest.data.layers.ActorLayerData;
import ca.nigelchan.silenttempest.data.layers.FieldLayerData;
import ca.nigelchan.silenttempest.data.layers.TileTemplate;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.scenes.BaseScene;
import ca.nigelchan.silenttempest.scenes.GameScene;
import ca.nigelchan.silenttempest.scenes.SplashScene;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Direction;

public class MainActivity extends BaseGameActivity {
	
	private Camera camera;
	private SceneManager manager;

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			manager.onBackKeyDown();
		return false;
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
				WorldData worldData = new WorldData(5, 5);
				FieldLayerData fyd = new FieldLayerData(5, 5);
				TileTemplate ground = new TileTemplate(0, TileTemplate.Attribute.NORMAL);
				TileTemplate thingy = new TileTemplate(1, TileTemplate.Attribute.OBSTACLE);
				TileTemplate hidingSpot = new TileTemplate(5, TileTemplate.Attribute.HIDING_SPOT);
				for (int i = 0; i < 5; ++i) {
					for (int j = 0; j < 5; ++j)
						fyd.setTile(i, j, ground);
				}
				fyd.setTile(2, 2, thingy);
				fyd.setTile(0, 1, hidingSpot);
				ActorLayerData ald = new ActorLayerData(5, 5, new PlayerData(Coordinate.ZERO, 0, 3f));
				EnemyData enemy = new EnemyData(new Coordinate(1, 1), 0, 1.5f, 1);
				enemy.addSequenceItem(new MoveData(Direction.EAST));
				enemy.addSequenceItem(new MoveData(Direction.EAST));
				enemy.addSequenceItem(new MoveData(Direction.SOUTH));
				enemy.addSequenceItem(new MoveData(Direction.SOUTH));
				enemy.addSequenceItem(new MoveData(Direction.WEST));
				enemy.addSequenceItem(new MoveData(Direction.WEST));
				enemy.addSequenceItem(new MoveData(Direction.NORTH));
				enemy.addSequenceItem(new MoveData(Direction.NORTH));
				ald.addEnemy(enemy);

				worldData.addLayer(fyd);
				worldData.addLayer(ald);
				manager.pushScene(new GameScene(manager, worldData));
			}

		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
}
