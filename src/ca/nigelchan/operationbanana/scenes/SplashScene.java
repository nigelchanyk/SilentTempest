package ca.nigelchan.operationbanana.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import ca.nigelchan.operationbanana.managers.SceneManager;
import ca.nigelchan.operationbanana.resources.SplashResource;

public class SplashScene extends BaseScene {
	
	private SplashResource resource;

	public SplashScene(SceneManager manager) {
		super(manager);
		resource = new SplashResource(activity);
		setResource(resource);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createScene() {
		this.setBackground(new Background(Color.WHITE));
		Sprite splash = new Sprite(0, 0, resource.getSplashTextureRegion(), activity.getVertexBufferObjectManager());
		splash.setPosition((camera.getWidth() - splash.getWidth()) * 0.5f, (camera.getHeight() - splash.getHeight()) * 0.5f);
		attachChild(splash);
	}
}
