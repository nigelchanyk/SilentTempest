package ca.nigelchan.silenttempest.scenes;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;

import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.managers.SubsceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.LoadingResource;
import ca.nigelchan.silenttempest.scenes.subscenes.LoadingSceneMinigame;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class LoadingScene extends BaseScene {
	
	private volatile boolean abortReady = false;
	private volatile boolean abortSignal = false;
	private CommonResource commonResource;
	private LoadingResource resource;
	private LoadingSceneMinigame minigame;
	private SubsceneManager subsceneManager;

	public LoadingScene(SceneManager manager, CommonResource commonResource) {
		super(manager);
		resource = new LoadingResource(activity);
		setResource(resource);
		this.commonResource = commonResource;
	}
	
	public void abortWhenReady() {
		abortSignal = true;
		checkSignal();
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
		Text loadingText = new Text(0, 0, commonResource.getHeaderFont(), "Loading", resource.getVertexBufferObjectManager());
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.CENTER)
			.setAnchorY(PositionHelper.AnchorY.CENTER)
			.setWidth((int)loadingText.getWidth())
			.setHeight((int)loadingText.getHeight());
		Coordinate textPos = pos.getPosition();
		loadingText.setPosition(textPos.x(), textPos.y());
		attachChild(loadingText);

		Entity uiLayer = new Entity();
		attachChild(uiLayer);
		subsceneManager = new SubsceneManager(uiLayer);
		minigame = new LoadingSceneMinigame(resource);
		subsceneManager.add(minigame);
		subsceneManager.load();
		subsceneManager.activate(minigame);
		setOnSceneTouchListener(subsceneManager);
		registerUpdateHandler(subsceneManager);
		
		resetMinigame();
	}
	
	private void checkSignal() {
		if (abortSignal && abortReady) {
			abortReady = false;
			abortSignal = false;
			resetMinigame();
			manager.popScene();
		}
	}
	
	private void resetMinigame() {
		minigame.prepare(new LoadingSceneMinigame.ICallback() {

			@Override
			public void onCompleted() {
				abortReady = true;
				checkSignal();
			}
			
		});
	}

}
