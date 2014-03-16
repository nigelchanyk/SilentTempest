package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Vector2;

public class PositionSenser extends EventComponent {

	private Actor actor;
	private Vector2 position;
	private float radiusSquare;
	
	public PositionSenser(Actor actor, Vector2 position, float radius) {
		this.actor = actor;
		this.position = position;
		radiusSquare = radius * radius;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (actor.getPosition().distanceSquare(position) <= radiusSquare)
			completed = true;
	}

}
