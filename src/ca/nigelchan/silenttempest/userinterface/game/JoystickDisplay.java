package ca.nigelchan.silenttempest.userinterface.game;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.entity.OffsetEntity;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class JoystickDisplay extends OffsetEntity implements Joystick.IListener {
	
	private AlphaModifier alphaModifier;
	private boolean firstMove = false;
	private Sprite sprite;
	
	public JoystickDisplay(GameResource resource) {
		super(
			resource.getJoystickDisplay().getWidth(),
			resource.getJoystickDisplay().getHeight(),
			OffsetEntity.OffsetType.CENTER
		);
		sprite = new Sprite(0, 0, resource.getJoystickDisplay(), resource.getVertexBufferObjectManager());
		sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);;
		attachChild(sprite);
		sprite.setAlpha(0);
	}
	
	@Override
	public void dispose() {
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();
		super.dispose();
	}
	
	@Override
	public void onAccept() {
		sprite.unregisterEntityModifier(alphaModifier);
		sprite.setAlpha(1);
	}

	@Override
	public void onDoubleTap() {
	}

	@Override
	public void onMove(float rotation) {
		if (firstMove) {
			alphaModifier = new AlphaModifier(0.3f, 0, 0.1f);
			alphaModifier.setAutoUnregisterWhenFinished(true);
			sprite.registerEntityModifier(alphaModifier);
			firstMove = false;
		}
		setRotation(MathHelper.toDegrees(rotation));
	}

	@Override
	public void onRelease() {
		alphaModifier = new AlphaModifier(0.5f, sprite.getAlpha(), 0);
		alphaModifier.setAutoUnregisterWhenFinished(true);
		sprite.registerEntityModifier(alphaModifier);
	}

	@Override
	public void onTouch(Vector2 position) {
		setPosition(position.x(), position.y());
		if (alphaModifier != null)
			sprite.unregisterEntityModifier(alphaModifier);

		sprite.setAlpha(0);
		firstMove = true;
	}

}
