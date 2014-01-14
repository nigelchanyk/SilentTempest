package ca.nigelchan.operationbanana.objects.actors.enemystrategies;

import ca.nigelchan.operationbanana.objects.actors.Actor;

public abstract class EnemyStrategy {
	
	protected Actor actor;
	
	public EnemyStrategy(Actor actor) {
		this.actor = actor;
	}

	public abstract void onUpdate(float elapsedTime);
	public abstract EnemyStrategy nextMove();

}
