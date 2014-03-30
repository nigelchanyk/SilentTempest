package ca.nigelchan.silenttempest.scenes;

import java.io.IOException;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;
import org.json.JSONException;

import ca.nigelchan.silenttempest.controllers.ActorController;
import ca.nigelchan.silenttempest.data.EventsData;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.data.actors.ActorConfiguration;
import ca.nigelchan.silenttempest.events.EventLayer;
import ca.nigelchan.silenttempest.importer.EventImporter;
import ca.nigelchan.silenttempest.importer.WorldImporter;
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
	
	private CommonResource commonResource;
	private ActorController controller = new ActorController();
	private String dataFilePath;
	private EventLayer eventLayer = new EventLayer();
	private EventManager eventManager = null;
	private EventsData eventsData;
	private Entity gameCore = new Entity();
	private GameInterface gameInterface;
	private GameMenu gameMenu;
	private GameResource resource;
	private SubsceneManager subsceneManager;
	private World world;
	private WorldData worldData;

	public GameScene(
		SceneManager manager,
		CommonResource commonResource,
		String dataFilePath
	) {
		super(manager);
		this.commonResource = commonResource;
		this.dataFilePath = dataFilePath;
		resource = new GameResource(activity);
		setResource(resource);
		setLoadAsynchronous(true);
		setDefualtPopOperation(PopOperation.DISPOSE);
		setBackground(new Background(Color.WHITE));
		attachChild(gameCore);
	}

	@Override
	public void disposeScene() {
		if (eventManager != null)
			eventManager.dispose();
		if (world != null)
			world.dispose();
		if (subsceneManager != null)
			subsceneManager.dispose();
		super.disposeScene();
	}

	@Override
	public void onBackKeyPressed() {
		subsceneManager.onBackKeyPressed();
	}

	@Override
	protected void createScene() {
		loadData();
		setWorldData(worldData);
		gameInterface = new GameInterface(this, resource, this.controller);
		gameMenu = new GameMenu(this, commonResource);

		Entity uiLayer = new Entity();
		attachUI(uiLayer);
		subsceneManager = new SubsceneManager(uiLayer);
		subsceneManager.add(gameInterface, gameMenu);
		setOnSceneTouchListener(subsceneManager);
		registerUpdateHandler(subsceneManager);
		subsceneManager.load();
		subsceneManager.activate(gameInterface);
	}
	
	private void loadData() {
		ActorConfiguration actorConfiguration = new ActorConfiguration();
		try {
			worldData = WorldImporter.load(dataFilePath, activity, actorConfiguration);
			eventsData = EventImporter.load(dataFilePath, activity);
		} catch (IOException e) {
			e.printStackTrace();
			activity.finish();
		} catch (JSONException e) {
			e.printStackTrace();
			activity.finish();
		}
	}
	
	public void returnToMainMenu() {
		manager.popScene();
		manager.pushScene(new MainMenuScene(manager, commonResource));
	}

	// Getters
	public GameInterface getGameInterface() {
		return gameInterface;
	}

	public GameMenu getGameMenu() {
		return gameMenu;
	}

	// Setters
	public void setGamePaused(boolean paused) {
		gameCore.setIgnoreUpdate(paused);
	}

	public void setWorldData(WorldData worldData) {
		if (world != null) {
			world.detachSelf();
			world.dispose();
		}
		this.worldData = worldData;
		world = new World(worldData, resource);
		gameCore.attachChild(world);
		controller.setActor(world.getPlayer());
		world.subscribe(new EnemyManager(world, resource));
		
		eventManager = new EventManager(eventsData, world, eventLayer, resource, commonResource);
		gameCore.attachChild(eventLayer);
		gameCore.registerUpdateHandler(eventManager);
	}
	
}
