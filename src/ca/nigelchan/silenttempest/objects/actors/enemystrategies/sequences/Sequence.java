package ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Sequence {
	
	protected Actor actor;
	protected boolean completed;
	
	private Vector2 initPosition;
	
	public Sequence(Actor actor, Vector2 initPosition) {
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
	public Vector2 getInitialPosition() {
		return initPosition;
	}
	
	public Coordinate getInitialCoordinate() {
		return initPosition.toCoordinate();
	}

	public boolean isCompleted() {
		return completed;
	}

}
