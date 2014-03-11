package ca.nigelchan.silenttempest.background;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.silenttempest.particlesystemfactories.RainFactory;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.MainMenuResource;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class MainMenuBackground extends Entity {
	
	private static final float MIN_LIGHTNING_INTERVAL = 6;
	private static final float MAX_LIGHTNING_INTERVAL = 9;

	private Rectangle lightFilter;
	private float lightningInterval;
	private Sprite penguinShine;
	
	public MainMenuBackground(MainMenuResource resource, CommonResource commonResource) {
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.RIGHT)
			.setAnchorY(PositionHelper.AnchorY.BOTTOM)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth((int)resource.getPenguin().getWidth())
			.setHeight((int)resource.getPenguin().getHeight());
		Coordinate position = pos.getPosition();

		attachChild(RainFactory.create(commonResource, 10, 15, 40));
		Sprite penguin = new Sprite(position.x(), position.y(), resource.getPenguin(), resource.getVertexBufferObjectManager());
		attachChild(penguin);
		
		penguinShine = new Sprite(position.x(), position.y(), resource.getPenguinShine(), resource.getVertexBufferObjectManager());
		penguinShine.setAlpha(0);
		attachChild(penguinShine);

		attachChild(RainFactory.create(commonResource, 10, 15, 40));

		lightFilter = new Rectangle(
			0,
			0,
			resource.getScreenWidth(),
			resource.getScreenHeight(),
			resource.getVertexBufferObjectManager()
		);
		lightFilter.setColor(0, 0, 0, 0.9f);
		attachChild(lightFilter);
		resetLightningInterval();
		
		Sprite leftNightVision = new Sprite(
			position.x() + MainMenuResource.LEFT_NIGHT_VISION_UV.x() * penguin.getWidth(),
			position.y() + MainMenuResource.LEFT_NIGHT_VISION_UV.y() * penguin.getHeight(),
			resource.getNightVisionGlow(),
			resource.getVertexBufferObjectManager()
		);
		Sprite rightNightVision = new Sprite(
			position.x() + MainMenuResource.RIGHT_NIGHT_VISION_UV.x() * penguin.getWidth(),
			position.y() + MainMenuResource.RIGHT_NIGHT_VISION_UV.y() * penguin.getHeight(),
			resource.getNightVisionGlow(),
			resource.getVertexBufferObjectManager()
		);
		attachChild(leftNightVision);
		attachChild(rightNightVision);
		
		registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				lightFilter.setAlpha(Math.min(0.9f, lightFilter.getAlpha() + pSecondsElapsed / 2));
				penguinShine.setAlpha(Math.max(0, penguinShine.getAlpha() - pSecondsElapsed / 2));
				lightningInterval -= pSecondsElapsed;
				if (lightningInterval <= 0) {
					resetLightningInterval();
					lightFilter.setAlpha(0);
					penguinShine.setAlpha(1);
				}
			}
		});
	}

	@Override
	public void dispose() {
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();
		super.dispose();
	}
	
	private void resetLightningInterval() {
        lightningInterval = (float)Math.random() * (MAX_LIGHTNING_INTERVAL - MIN_LIGHTNING_INTERVAL) + MIN_LIGHTNING_INTERVAL;
	}

}
