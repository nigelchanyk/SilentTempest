package ca.nigelchan.operationbanana.objects.actors.enemystrategies.fixedsteps;

import ca.nigelchan.operationbanana.util.Vector2;

public abstract class FixedStep {
	
	private Vector2 initialPosition;
	
	public FixedStep(Vector2 initialPosition) {
		this.initialPosition = initialPosition;
	}

}
