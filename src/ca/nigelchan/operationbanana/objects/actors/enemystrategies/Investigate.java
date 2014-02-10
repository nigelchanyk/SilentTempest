package ca.nigelchan.operationbanana.objects.actors.enemystrategies;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.Player;
import ca.nigelchan.operationbanana.objects.actors.controllers.EnemyCore;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

public class Investigate extends EnemyStrategy {
	
	private Coordinate lastSeenPosition;
	
	public Investigate(Actor actor, EnemyCore core) {
		super(actor, core);
		this.lastSeenPosition = actor.getWorld().getPlayer().getGridPosition();
	}

	@Override
	public void onUpdate(float elapsedTime) {
        lastSeenPosition = actor.getWorld().getPlayer().getGridPosition();
        Player player = actor.getWorld().getPlayer();
        float angle = MathHelper.getRotation(actor.getPosition(), player.getPosition());
        actor.setRadianRotation(
        	MathHelper.interpolateAngle(
                actor.getRadianRotation(),
                angle,
                Actor.ROTATION_PER_SEC * elapsedTime
            )
        );

        Vector2 unitVector = MathHelper.getUnitVector(angle);
		if (actor.getPosition().distanceSquare(player.getPosition()) > MathHelper.sq(elapsedTime * actor.getSpeed())) {
            actor.setPosition(actor.getPosition().add(unitVector.multiply(elapsedTime * actor.getSpeed())));
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		Player player = actor.getWorld().getPlayer();
		if (!actor.getWorld().isValidPath(actor.getPosition(), player.getPosition())) {
            return new Seek(actor, core, lastSeenPosition);
		}
		return this;
	}

}
