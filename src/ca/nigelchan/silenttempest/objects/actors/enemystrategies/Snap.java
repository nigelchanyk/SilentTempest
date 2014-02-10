package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Snap extends EnemyStrategy {
	
	private Vector2 dest;
	private EnemyStrategy nextStrategy;

	public Snap(Actor actor, EnemyCore core, Coordinate dest, EnemyStrategy nextStrategy) {
		super(actor, core);
		this.dest = dest.toCenterVector2();
		this.nextStrategy = nextStrategy;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		float angle = MathHelper.getRotation(actor.getPosition(), dest);
		float current = MathHelper.interpolateAngle(
            actor.getRadianRotation(),
            angle,
            elapsedTime * Actor.ROTATION_PER_SEC
        );
		actor.setRadianRotation(current);
		if (MathHelper.getAngleDifference(angle, current) < 0.0001f) {
            if (actor.getPosition().distanceSquare(dest) < MathHelper.sq(elapsedTime * actor.getSpeed()))
            	actor.setPosition(dest);
            else {
                actor.setPosition(actor.getPosition().add(
                	MathHelper.getUnitVector(current).multiply(elapsedTime * actor.getSpeed())
                ));
            }
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		if (core.getAlertLevel() >= 0.5f && core.canSee(actor.getWorld().getPlayer().getPosition()))
			return new Investigate(actor, core);
		if (actor.getPosition().distanceSquare(dest) < 0.000001f)
			return nextStrategy;
		return this;
	}

}
