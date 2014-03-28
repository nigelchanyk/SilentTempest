package ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Move extends Sequence {
	
	private Vector2 dest;
	private Direction direction;
	private Turn turn = null;
	
	public Move(Actor actor, Vector2 initPosition, Direction direction) {
		super(actor, initPosition);
		this.direction = direction;
		dest = initPosition.add(MathHelper.getTranslation(direction).toVector2());
	}

	@Override
	public void onSpawn() {
		actor.setRadianRotation(MathHelper.getRotation(direction));
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (actor.changeRotationOnMove())
			turn = new Turn(actor, getInitialPosition(), MathHelper.getRotation(actor.getPosition(), dest));
		else
			actor.setPosition(getInitialPosition());
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (turn != null) {
			if (!turn.isCompleted()) {
				turn.onUpdate(elapsedTime);
				if (!turn.isCompleted())
					return;
			}
		}
		
		if (actor.getPosition().distanceSquare(dest) < MathHelper.sq(elapsedTime * actor.getSpeed())) {
			actor.setPosition(dest);
			completed = true;
		}

		Vector2 unitVector = dest.minus(actor.getPosition()).normal();
		actor.setPosition(actor.getPosition().add(unitVector.multiply(elapsedTime * actor.getSpeed())));
	}

	// Getters
	public Direction getDirection() {
		return direction;
	}

}
