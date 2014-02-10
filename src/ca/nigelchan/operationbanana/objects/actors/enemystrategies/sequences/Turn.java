package ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.Direction;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

public class Turn extends Sequence {
	
	private float angle;

	public Turn(Actor actor, Coordinate initPosition, Direction direction) {
		super(actor, initPosition);
		Coordinate t = MathHelper.getTranslation(direction);
		Vector2 dest = actor.getPosition().floor().add(t.x() + 0.5f, t.y() + 0.5f);
		angle = MathHelper.wrapAngle(MathHelper.getRotation(actor.getPosition(), dest));
	}

	public Turn(Actor actor, Coordinate initPosition, float angle) {
		super(actor, initPosition);
		this.angle = angle;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		float current = MathHelper.interpolateAngle(
            actor.getRadianRotation(),
            angle,
            elapsedTime * Actor.ROTATION_PER_SEC
        );
		if (Math.abs(current - angle) < 0.0001f) {
			current = angle;
			completed = true;
		}
		actor.setRadianRotation(current);
	}

}
