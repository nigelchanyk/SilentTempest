package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Player;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Investigate extends EnemyStrategy {
	
	private Coordinate lastSeenPosition;
	private float lastSeenRotation;
	
	public Investigate(Actor actor, EnemyCore core) {
		super(actor, core);
		lastSeenPosition = actor.getWorld().getPlayer().getGridPosition();
		lastSeenRotation = actor.getWorld().getPlayer().getRadianRotation();
	}

	@Override
	public void onUpdate(float elapsedTime) {
        lastSeenPosition = actor.getWorld().getPlayer().getGridPosition();
        lastSeenRotation = actor.getWorld().getPlayer().getRadianRotation();
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
            return new Seek(actor, core, lastSeenPosition, lastSeenRotation);
		}
		return this;
	}

}
