package ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.util.Coordinate;

public abstract class Sequence {
	
	protected Actor actor;
	protected boolean completed;
	
	private Coordinate initPosition;
	
	public Sequence(Actor actor, Coordinate initPosition) {
		this.actor = actor;
		this.initPosition = initPosition;
	}
	
	public void onSpawn() {
	}
	
	public void onStart() {
		completed = false;
		actor.setPosition(initPosition.toCenterVector2());
	}
	
	public abstract void onUpdate(float elapsedTime);
	
	// Getters
	public Coordinate getInitialPosition() {
		return initPosition;
	}

	public boolean isCompleted() {
		return completed;
	}

}
