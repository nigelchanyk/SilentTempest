package ca.nigelchan.operationbanana.objects.actors.action;

import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

public class Move extends Action {
	
	private float rotation;
	private Vector2 unitVector;

	@Override
	public void onUpdate(float elapsedTime) {
		actor.setRadianRotation(rotation);
		actor.moveTo(actor.getPosition().add(unitVector.multiply(actor.getSpeed() * elapsedTime)));
	}

	// Setters
	public void setRotation(float rotation) {
		this.rotation = rotation;
		this.unitVector = MathHelper.getUnitVector(rotation);
	}
}
