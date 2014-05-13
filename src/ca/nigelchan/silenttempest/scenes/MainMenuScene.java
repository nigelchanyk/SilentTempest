package ca.nigelchan.silenttempest.scenes;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.background.MainMenuBackground;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.managers.SubsceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.MainMenuResource;
import ca.nigelchan.silenttempest.scenes.subscenes.ActSelectionMenu;
import ca.nigelchan.silenttempest.scenes.subscenes.MainMenu;
import ca.nigelchan.silenttempest.scenes.subscenes.SceneSelectionMenu;

public class MainMenuScene extends BaseScene {
	
	private ActSelectionMenu actSelectionMenu;
	private CommonResource commonResource;
	private MainMenu mainMenu;
	private MainMenuResource resource;
	private SceneSelectionMenu sceneSelectionMenu;
	private SubsceneManager subsceneManager;

	public MainMenuScene(SceneManager manager, CommonResource commonResource) {
		super(manager);
		this.commonResource = commonResource;
		resource = new MainMenuResource(activity);
		setResource(resource);
		setDefualtPopOperation(PopOperation.DISPOSE);
		setLoadAsynchronous(true);
	}

	@Override
	public void disposeScene() {
		if (subsceneManager != null)
			subsceneManager.dispose();
		super.disposeScene();
	}

	@Override
	public void onBackKeyPressed() {
		subsceneManager.onBackKeyPressed();
	}
	
	public void startGame(String dataFilePath) {
		try {
			InputStream stream = activity.getAssets().open(dataFilePath);
			stream.close();
		} catch (IOException e) {
			System.err.println(dataFilePath + " does not exist.");
			return;
		}
		manager.popScene();
		manager.pushScene(new GameScene(manager, commonResource, dataFilePath));
	}

	@Override
	protected void createScene() {
		setBackground(new Background(new Color(0.2f, 0.2f, 0.2f)));
		attachChild(new MainMenuBackground(resource, commonResource));
		Entity uiLayer = new Entity();
		attachChild(uiLayer);
		subsceneManager = new SubsceneManager(uiLayer);
		mainMenu = new MainMenu(this, commonResource);
		actSelectionMenu = new ActSelectionMenu(this, resource, commonResource);
		sceneSelectionMenu = new SceneSelectionMenu(this, resource, commonResource, actSelectionMenu);
		
		subsceneManager.add(mainMenu, actSelectionMenu, sceneSelectionMenu);
		subsceneManager.load();
		subsceneManager.activate(mainMenu);
		setOnSceneTouchListener(subsceneManager);
		registerUpdateHandler(subsceneManager);
	}

	// Getters
	public ActSelectionMenu getActSelectionMenu() {
		return actSelectionMenu;
	}

	public MainMenu getMainMenu() {
		return mainMenu;
	}

	public SceneSelectionMenu getSceneSelectionMenu() {
		return sceneSelectionMenu;
	}

}
