package ca.nigelchan.operationbanana.data.actors;

import ca.nigelchan.operationbanana.util.Coordinate;

public class ActorData {
	
	private Coordinate initPosition;
	private float initRotation;
	private float speed;
	
	public ActorData(Coordinate initPosition, float initRotation, float speed) {
		this.initPosition = initPosition;
		this.initRotation = initRotation;
		this.speed = speed;
	}

	public Coordinate getInitialPosition() {
		return initPosition;
	}

	public float getInitialRotation() {
		return initRotation;
	}
	
	public float getSpeed() {
		return speed;
	}

}
