package ca.nigelchan.operationbanana.objects.actors.controllers;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.objects.actors.Enemy;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.EnemyStrategy;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.Patrol;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.PostponedList;
import ca.nigelchan.operationbanana.util.Vector2;

public class EnemyCore extends Controller {
	
	private static final float ALERT_ACCUMULATION_FACTOR = 2;
	private static final float HALF_FIELD_OF_VIEW = MathHelper.toRadians(75);

	private float alertLevel = 0;
	private Patrol patrol;
	private EnemyStrategy strategy;
	private PostponedList<IListener> subscribers = new PostponedList<IListener>(4);
	private int visionRange;

	public EnemyCore(Enemy actor, EnemyData data) {
		super(actor);
		patrol = new Patrol(actor, this, data);
		strategy = patrol;
		visionRange = data.getVisionRange();
		setEnabled(true);
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
		Vector2 target = actor.getWorld().getPlayer().getPosition();
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
