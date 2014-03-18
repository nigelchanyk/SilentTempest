package ca.nigelchan.silenttempest.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.resources.SplashResource;

public class SplashScene extends Scene {
	
	private SplashResource resource;

	public SplashScene(BaseGameActivity activity, Camera camera) {
		resource = new SplashResource(activity);
		resource.load();
		this.setBackground(new Background(Color.WHITE));
		Sprite splash = new Sprite(0, 0, resource.getSplashTextureRegion(), activity.getVertexBufferObjectManager());
		splash.setPosition((camera.getWidth() - splash.getWidth()) * 0.5f, (camera.getHeight() - splash.getHeight()) * 0.5f);
		attachChild(splash);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();
		super.dispose();
	}

}
