package ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Coordinate;

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
