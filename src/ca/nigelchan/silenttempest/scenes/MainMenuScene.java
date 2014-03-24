package ca.nigelchan.silenttempest.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.background.MainMenuBackground;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.managers.SubsceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.MainMenuResource;
import ca.nigelchan.silenttempest.scenes.subscenes.MainMenu;

public class MainMenuScene extends BaseScene {
	
	private CommonResource commonResource;
	private MainMenu mainMenu;
	private MainMenuResource resource;
	private SubsceneManager subsceneManager;

	public MainMenuScene(SceneManager manager, CommonResource commonResource) {
		super(manager);
		this.commonResource = commonResource;
		resource = new MainMenuResource(activity);
		setResource(resource);
		loadAsynchronous = true;
	}

	@Override
	public void disposeScene() {
		if (subsceneManager != null)
			subsceneManager.dispose();
		super.disposeScene();
	}

	@Override
	public void onBackKeyPressed() {
	}

	@Override
	protected void createScene() {
		setBackground(new Background(new Color(0.2f, 0.2f, 0.2f)));
		attachChild(new MainMenuBackground(resource, commonResource));
		Entity uiLayer = new Entity();
		attachChild(uiLayer);
		subsceneManager = new SubsceneManager(uiLayer);
		mainMenu = new MainMenu(commonResource) {
			
			@Override
			public void onPlay() {
			}
		};
		
		subsceneManager.add(mainMenu);
		subsceneManager.load();
		subsceneManager.activate(mainMenu);
		setOnSceneTouchListener(subsceneManager);
		registerUpdateHandler(subsceneManager);
	}
}
