package ca.nigelchan.silenttempest.objects.actors.controllers;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class PlayerController extends Controller {
	
	private static final float KNOCK_OUT_ENEMY_ANGLE_THRESHOLD = MathHelper.THREE_PI_OVER_4;
	private static final float INTERACT_PLAYER_ANGLE_THRESHOLD = MathHelper.THREE_PI_OVER_4;
	private static final float INTERACT_DISTANCE_SQ_THRESHOLD = MathHelper.sq(1.5f);

	private Enemy enemyDragged = null;
	private float rotation;
	private Vector2 unitVector;
	
	public PlayerController(Actor actor) {
		super(actor);
	}
	
	public void interact() {
		if (enemyDragged != null) {
			releaseEnemy();
			return;
		}
		if (attemptDragEnemy())
			return;
		if (attemptKnockOut())
			return;
		return;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		actor.setRadianRotation(rotation);
		actor.move(unitVector.multiply(actor.getSpeed() * elapsedTime));
		
		if (enemyDragged != null)
			dragEnemy();
	}
	
	private boolean attemptDragEnemy() {
		for (Enemy enemy : actor.getWorld().getEnemies()) {
			if (!enemy.isKnockedOut())
				continue;
			if (actor.getPosition().distanceSquare(enemy.getPosition()) > INTERACT_DISTANCE_SQ_THRESHOLD)
				continue;
			
			enemyDragged = enemy;
			dragEnemy();
			return true;
		}
		
		return false;
	}
	
	private boolean attemptKnockOut() {
		for (Enemy enemy : actor.getWorld().getEnemies()) {
			if (actor.getPosition().distanceSquare(enemy.getPosition()) > INTERACT_DISTANCE_SQ_THRESHOLD)
				continue;
			float playerToEnemyAngle = MathHelper.getRotation(actor.getPosition(), enemy.getPosition());
			if (MathHelper.getAngleDifference(playerToEnemyAngle, actor.getRadianRotation()) > INTERACT_PLAYER_ANGLE_THRESHOLD) {
				continue;
			}
			float enemyToPlayerAngle = playerToEnemyAngle + MathHelper.PI;
			if (MathHelper.getAngleDifference(enemyToPlayerAngle, enemy.getRadianRotation()) < KNOCK_OUT_ENEMY_ANGLE_THRESHOLD) {
				continue;
			}
			
			enemy.setKnockedOut(true);
			return true;
		}
		
		return false;
	}
	
	private void dragEnemy() {
		float angle = MathHelper.getRotation(enemyDragged.getPosition(), actor.getPosition());
		enemyDragged.setRadianRotation(MathHelper.wrapAngle(angle + MathHelper.PI));
		float dist = enemyDragged.getPosition().distance(actor.getPosition());
		if (dist > actor.getRadius()) {
			Vector2 delta = MathHelper.getUnitVector(angle + MathHelper.PI).multiply(actor.getRadius());
			enemyDragged.setPosition(actor.getPosition().add(delta));
		}
	}
	
	private void releaseEnemy() {
		enemyDragged = null;
	}

	// Setters
	public void setRotation(float rotation) {
		this.rotation = rotation;
		this.unitVector = MathHelper.getUnitVector(rotation);
	}
}
