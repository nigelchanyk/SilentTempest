package ca.nigelchan.operationbanana.data.actors;

import ca.nigelchan.operationbanana.util.Vector2;

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

	public float getInitialRotation() {
		return initRotation;
	}
	
	public float getSpeed() {
		return speed;
	}

}
