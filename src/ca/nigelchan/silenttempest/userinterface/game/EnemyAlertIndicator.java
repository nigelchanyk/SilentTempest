package ca.nigelchan.silenttempest.userinterface.game;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import ca.nigelchan.silenttempest.entity.OffsetEntity;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Vector2;

public class EnemyAlertIndicator extends FollowIndicator<Enemy> implements EnemyCore.IListener {
	
	private Sprite sprite;
	
	public EnemyAlertIndicator(World world, GameResource resource) {
		super(OffsetEntity.OffsetType.CENTER, world);
		sprite = new Sprite(0, 0, resource.getAlertIndicator(), resource.getVertexBufferObjectManager()) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}

		};
        sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        attachChild(sprite);
	}
	
	@Override
	public void dispose() {
		sprite.dispose();
		super.dispose();
	}

	@Override
	public void onAlertLevelChanged(float alertLevel) {
		setAlertLevel(alertLevel);
	}

	@Override
	public void onKnockedOut() {
	}

	@Override
	public void onRecovered() {
	}

	@Override
	public void onRotationChanged(float rotation) {
		setRadianRotation(rotation);
	}

	@Override
	protected Vector2 getFollowOffset() {
		return Vector2.ZERO;
	}


	@Override
	protected void onFollow(Enemy actor) {
		actor.getCore().subscribe(this);
		setRadianRotation(actor.getRadianRotation());
		setScale(actor.getCore().getVisionRange() * 0.5f);
		setRadius(actor.getCore().getVisionRange());
		world.attachBelowActorChild(this);
		setAlertLevel(actor.getCore().getAlertLevel());
	}

	@Override
	protected void onUnfollow(Enemy actor) {
		actor.getCore().unsubscribe(this);
		detachSelf();
	}
	
	private void setAlertLevel(float level) {
		if (level < 0.5f)
			sprite.setColor(1, 1, 1, 0.3f + level * 2 * 0.7f);
		else if (level < 1)
			sprite.setColor(1, 1.5f - level, 0);
		else
			sprite.setColor(1, 0, 0);
	}

}
