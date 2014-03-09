package ca.nigelchan.silenttempest.reactors;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Vector2;

public class PositionReactor extends Reactor {
	
	private Actor actor;
	private Vector2 position;
	private float radiusSquare;
	
	public PositionReactor(Actor actor, Vector2 position, float radius) {
		this.actor = actor;
		this.position = position;
		radiusSquare = radius * radius;
	}

	@Override
	public void update(float elapsedTime) {
		if (actor.getPosition().distanceSquare(position) <= radiusSquare)
			react();
	}

}
