package ca.nigelchan.silenttempest.objects.actors.controllers;

import android.util.Log;
import ca.nigelchan.silenttempest.data.actors.EnemyData;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.Player;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.EnemyStrategy;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.Patrol;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.PostponedList;
import ca.nigelchan.silenttempest.util.Vector2;

public class EnemyCore extends Controller {
	
	public static final float ALERT_ACCUMULATION_FACTOR = 2;
	public static final float HALF_FIELD_OF_VIEW = MathHelper.toRadians(75);

	private float alertLevel = 0;
	private EnemyData enemyData;
	private EnemyStrategy strategy;
	private PostponedList<IListener> subscribers = new PostponedList<IListener>(4);
	private int visionRange;

	public EnemyCore(Enemy actor, EnemyData data) {
		super(actor);
		this.enemyData = data;
		strategy = new Patrol(actor, this, data, true);
		visionRange = data.getVisionRange();
		setEnabled(true);
	}

	public boolean canSee(Vector2 target) {
		float angleDifference = MathHelper.getAngleDifference(
			actor.getRadianRotation(),
			MathHelper.getRotation(actor.getPosition(), target)
		);
		if (angleDifference > HALF_FIELD_OF_VIEW)
			return false;
		if (!actor.getWorld().isValidPath(actor.getPosition(), target))
			return false;
		return true;
	}

	@Override
	protected void onUpdate(float elapsedTime) {
		computeAlertLevel(elapsedTime);
		EnemyStrategy previous = strategy;
		for (int i = 0; i < 10; ++i) {
			strategy = strategy.nextMove();
			if (previous == strategy) {
				strategy.onUpdate(elapsedTime);
				return;
			}
			Log.i("STRATEGY", strategy.getClass().getSimpleName());
			previous = strategy;
		}

        throw new IllegalStateException("Non deterministic enemy state.");
	}
	
	public void subscribe(IListener subscriber) {
		if (subscribers.contains(subscriber))
			return;
		subscribers.add(subscriber);
	}
	
	public void unsubscribe(IListener subscriber) {
		subscribers.remove(subscriber);
	}
	
	private void computeAlertLevel(float elapsedTime) {
		Player player = actor.getWorld().getPlayer();
		if (player.isHidden())
			return;

		Vector2 target = player.getPosition();
		float angleDifference = MathHelper.getAngleDifference(
			actor.getRadianRotation(),
			MathHelper.getRotation(actor.getPosition(), target)
		);
		if (angleDifference > HALF_FIELD_OF_VIEW)
			return;
		if (!actor.getWorld().isValidPath(actor.getPosition(), target))
			return;
		
		float distance = actor.getPosition().distance(target);

		// Why not play safe?
		if (distance == 0) {
			setAlertLevel(1);
			return;
		}

		// Divided by 1 + angleDifference so that enemy's alert level increases
		// faster when player is closer to enemy's center of view
		setAlertLevel(alertLevel + ALERT_ACCUMULATION_FACTOR * elapsedTime / distance / (1 + angleDifference));
		if (distance <= visionRange && alertLevel < 0.5f)
			setAlertLevel(0.5f);
	}
	
	// Getters
	public float getAlertLevel() {
		return alertLevel;
	}
	
	public EnemyData getEnemyData() {
		return enemyData;
	}

	public int getVisionRange() {
		return visionRange;
	}

	// Setters
	public void setAlertLevel(float alertLevel) {
		this.alertLevel = alertLevel = MathHelper.clamp(alertLevel, 0, 1);
		for (IListener subscriber : subscribers)
			subscriber.onAlertLevelChanged(alertLevel);
	}


	public static interface IListener {

		public void onAlertLevelChanged(float alertLevel);

	}

}
