package ca.nigelchan.silenttempest.userinterface.game;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class KnockedOutIndicator extends FollowIndicator<Actor> {
	
	private static final float STAR_ROTATION_SPEED = 0.5f;
	private static final float STAR_REVOLUTION_SPEED = 1.5f;
	
	private Sprite[] stars = new Sprite[3];
	private SpriteGroup spriteGroup;

	public KnockedOutIndicator(World world, GameResource resource) {
		super(OffsetType.CENTER, world);
		spriteGroup = resource.createStarAtlas(stars.length);
		float offset = resource.getStar().getWidth() / 2;
		for (int i = 0; i < stars.length; ++i) {
			Vector2 unitVector = MathHelper.getUnitVector(MathHelper.TWO_PI * i / stars.length)
				.multiply(offset * 1.5f);
			stars[i] = new Sprite(
				unitVector.x() + offset,
				unitVector.y() + offset,
				resource.getStar(),
				resource.getVertexBufferObjectManager()
			);
			stars[i].setRotationCenter(offset, offset);
			spriteGroup.attachChild(stars[i]);
		}
		attachChild(spriteGroup);

		registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				float revolution = getRadianRotation() + pSecondsElapsed * STAR_REVOLUTION_SPEED;
				setRadianRotation(MathHelper.wrapAngle(revolution));
				for (int i = 0; i < stars.length; ++i) {
					float rotation = MathHelper.toRadians(stars[i].getRotation());
					float newRotation = rotation + pSecondsElapsed * STAR_ROTATION_SPEED;
					stars[i].setRotation(MathHelper.toDegrees(MathHelper.wrapAngle(newRotation)));
				}
			}
		});
	}

	@Override
	public void dispose() {
		for (int i = 0; i < stars.length; ++i)
			stars[i].dispose();
		spriteGroup.dispose();
		super.dispose();
	}

	@Override
	public void onKnockedOut() {
	}

	@Override
	public void onRecovered() {
	}

	@Override
	public void onRotationChanged(float rotation) {
	}

	@Override
	protected void onFollow(Actor actor) {
		world.attachAboveActorChild(this);
	}

	@Override
	protected void onUnfollow(Actor actor) {
		detachSelf();
	}

	// Getters
	@Override
	protected Vector2 getFollowOffset() {
		return Vector2.ZERO;
	}

}
