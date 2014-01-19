package ca.nigelchan.operationbanana.objects.actors.controllers;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

public class FreeMove extends Controller {

	private float rotation;
	private Vector2 unitVector;
	
	public FreeMove(Actor actor) {
		super(actor);
	}

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
