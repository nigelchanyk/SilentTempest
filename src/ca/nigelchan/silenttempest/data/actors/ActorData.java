package ca.nigelchan.silenttempest.data.actors;

import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Vector2;

public class ActorData {
	
	private Vector2 initPosition;
	private float initRotation;
	private float speed;
	
	public ActorData(Vector2 initPosition, float initRotation, float speed) {
		this.initPosition = initPosition;
		this.initRotation = initRotation;
		this.speed = speed;
	}

	public Vector2 getInitialPosition() {
		return initPosition;
	}
	
	public Coordinate getInitialCoordinate() {
		return initPosition.toCoordinate();
	}

	public float getInitialRotation() {
		return initRotation;
	}
	
	public float getSpeed() {
		return speed;
	}

}
