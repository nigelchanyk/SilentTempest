package ca.nigelchan.silenttempest.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.controllers.ActorController;
import ca.nigelchan.silenttempest.data.EventsData;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.managers.EnemyManager;
import ca.nigelchan.silenttempest.managers.EventManager;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.managers.SubsceneManager;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;
import ca.nigelchan.silenttempest.scenes.subscenes.GameMenu;

public class GameScene extends BaseScene {
	
	private ActorController controller = new ActorController();
	private EventManager eventManager = null;
	private EventsData eventsData;
	private GameInterface gameInterface;
	private GameMenu gameMenu;
	private GameResource resource;
	private SubsceneManager subsceneManager;
	private World world;
	private WorldData worldData;

	public GameScene(
		SceneManager manager,
		CommonResource commonResource,
		WorldData worldData,
		EventsData eventsData
	) {
		super(manager);
		this.worldData = worldData;
		this.eventsData = eventsData;
		resource = new GameResource(activity);
		setResource(resource);
		// TODO Set to asynchronous loading
		setBackground(new Background(Color.WHITE));
		gameInterface = new GameInterface(resource, this.controller);
		gameMenu = new GameMenu(commonResource) {

			@Override
			public void onContinue() {
				onBackKeyPressed();
			}

			@Override
			public void onExit() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onRestart() {
				// TODO Auto-generated method stub
				
			}
			
		};
		Entity uiLayer = new Entity();
		subsceneManager = new SubsceneManager(uiLayer);
		subsceneManager.add(gameInterface, gameMenu);
		setOnSceneTouchListener(subsceneManager);
		attachUI(uiLayer);
	}

	@Override
	public void disposeScene() {
		subsceneManager.dispose();
		super.disposeScene();
	}

	@Override
	public void onBackKeyPressed() {
		if (gameInterface.isActive()) {
			subsceneManager.activate(gameMenu);
			world.lock();
		}
		else {
			subsceneManager.activate(gameInterface);
			world.unlock();
		}
	}

	@Override
	protected void createScene() {
		setWorldData(worldData);
		subsceneManager.load();
		subsceneManager.activate(gameInterface);
	}

	// Setters
	public void setWorldData(WorldData worldData) {
		if (world != null) {
			world.detachSelf();
			world.dispose();
		}
		this.worldData = worldData;
		world = new World(worldData, resource);
		attachChild(world);
		controller.setActor(world.getPlayer());
		world.subscribe(new EnemyManager(world, resource));
		
		eventManager = new EventManager(eventsData, world, gameInterface, resource);
		this.registerUpdateHandler(eventManager);
	}
	
}
