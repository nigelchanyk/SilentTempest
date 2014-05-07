package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Investigate extends EnemyStrategy {
	
	private Actor target;
	
	public Investigate(Actor actor, EnemyCore core, Actor target) {
		super(actor, core);
		this.target = target;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		float angle = MathHelper.getRotation(actor.getPosition(), target.getPosition());
		actor.setRadianRotation(
			MathHelper.interpolateAngle(
				actor.getRadianRotation(),
				angle,
				Actor.ROTATION_PER_SEC * elapsedTime
			)
		);

		Vector2 unitVector = MathHelper.getUnitVector(angle);
		if (actor.getPosition().distanceSquare(target.getPosition()) > MathHelper.sq(elapsedTime * actor.getSpeed())) {
			actor.setPosition(actor.getPosition().add(unitVector.multiply(elapsedTime * actor.getSpeed())));
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		if (!actor.getWorld().isValidPath(actor.getPosition(), target.getPosition())) {
			return new Seek(actor, core, target);
		}
		return this;
	}

}
