package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import ca.nigelchan.silenttempest.objects.actors.Actor;
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

}
