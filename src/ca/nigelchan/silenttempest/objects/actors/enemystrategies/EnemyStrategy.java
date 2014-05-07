package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;

public abstract class EnemyStrategy {
	
	protected Actor actor;
	protected EnemyCore core;
	
	public EnemyStrategy(Actor actor, EnemyCore core) {
		this.actor = actor;
		this.core = core;
	}

	public abstract void onUpdate(float elapsedTime);
	public abstract EnemyStrategy nextMove();
	
	protected Enemy noticeKnockedOutEnemy() {
		float distanceSq = Float.MAX_VALUE;
		Enemy closest = null;
		for (Enemy enemy : actor.getWorld().getEnemies()) {
			if (!enemy.isKnockedOut())
				continue;
			if (!core.canSee(enemy.getPosition()))
				continue;

			float current = actor.getPosition().distanceSquare(enemy.getPosition());
			if (current >= distanceSq)
				continue;

			closest = enemy;
			distanceSq = current;
		}
		
		return closest;
	}

}
