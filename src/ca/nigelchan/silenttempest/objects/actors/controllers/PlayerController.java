package ca.nigelchan.silenttempest.objects.actors.controllers;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class PlayerController extends Controller {

	private float rotation;
	private Vector2 unitVector;
	
	public PlayerController(Actor actor) {
		super(actor);
	}
	
	public void interact() {
	}

	@Override
	public void onUpdate(float elapsedTime) {
		actor.setRadianRotation(rotation);
		actor.move(unitVector.multiply(actor.getSpeed() * elapsedTime));
	}

	// Setters
	public void setRotation(float rotation) {
		this.rotation = rotation;
		this.unitVector = MathHelper.getUnitVector(rotation);
	}
}
