package ca.nigelchan.silenttempest.scenes;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.controllers.ActorController;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.managers.EnemyManager;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.userinterface.game.JoystickDisplay;

public class GameScene extends BaseScene {
	
	private ActorController controller;
	private Joystick joystick;
	private JoystickDisplay joystickDisplay;
	private GameResource resource;
	private World world;
	private WorldData worldData;

	public GameScene(SceneManager manager, WorldData worldData) {
		super(manager);
		this.worldData = worldData;
		resource = new GameResource(activity);
		setResource(resource);
		// TODO Set to asynchronous loading
		setBackground(new Background(Color.WHITE));
		
		joystick = new Joystick(activity.getResources().getDisplayMetrics());
		setOnSceneTouchListener(joystick);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void createScene() {
		world = new World(worldData, resource);
		attachChild(world);
		controller = new ActorController(world.getPlayer());
		joystick.subscribe(controller);
		world.subscribe(new EnemyManager(world, resource));
		
		setHud(new HUD());
		joystickDisplay = new JoystickDisplay(resource);
		joystick.subscribe(joystickDisplay);
		getHud().attachChild(joystickDisplay);
	}

	// Setters
	public void setWorldData(WorldData worldData) {
		if (world != null) {
			world.detachSelf();
			world.dispose();
		}
		world = new World(worldData, resource);
		attachChild(world);
		controller.setActor(world.getPlayer());
		world.subscribe(new EnemyManager(world, resource));
	}
}