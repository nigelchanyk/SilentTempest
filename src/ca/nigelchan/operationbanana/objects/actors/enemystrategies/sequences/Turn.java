package ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.Direction;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

public class Turn extends Sequence {
	
	public static final float ROTATION_PER_SEC = MathHelper.TWO_PI * 2;
	
	private float angle;

	public Turn(Actor actor, Coordinate initPosition, Direction direction) {
		super(actor, initPosition);
		Coordinate t = MathHelper.getTranslation(direction);
		Vector2 dest = actor.getPosition().floor().add(t.x() + 0.5f, t.y() + 0.5f);
		angle = MathHelper.wrapAngle(MathHelper.getRotation(actor.getPosition(), dest));
	}

	@Override
	public void onUpdate(float elapsedTime) {
		float current = MathHelper.wrapAngle(actor.getRadianRotation());
		if (Math.abs(current - angle) > MathHelper.PI)
			current += angle < current ? -MathHelper.TWO_PI : MathHelper.TWO_PI;
		if (current < angle)
			current = Math.min(current + elapsedTime * ROTATION_PER_SEC, angle);
		else
			current = Math.max(current - elapsedTime * ROTATION_PER_SEC, angle);

		current = MathHelper.wrapAngle(current);
		if (Math.abs(current - angle) < 0.0001f) {
			current = angle;
			completed = true;
		}
		actor.setRadianRotation(current);
	}

}
