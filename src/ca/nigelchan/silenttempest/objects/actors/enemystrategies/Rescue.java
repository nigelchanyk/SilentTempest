package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.util.MathHelper;

public class Rescue extends Investigate {
	
	private Enemy rescuee;

	public Rescue(Actor actor, EnemyCore core, Enemy rescuee) {
		super(actor, core, rescuee);
		this.rescuee = rescuee;
		core.setAlertLevel(Math.max(0.5f, core.getAlertLevel()));
	}

	@Override
	public void onUpdate(float elapsedTime) {
		super.onUpdate(elapsedTime);
		if (actor.getPosition().distanceSquare(rescuee.getPosition()) < MathHelper.sq(actor.getRadius()))
			rescuee.setKnockedOut(false);
	}

	@Override
	public EnemyStrategy nextMove() {
		if (core.canSee(actor.getWorld().getPlayer().getPosition()))
			return new Investigate(actor, core, actor.getWorld().getPlayer());
		if (!rescuee.isKnockedOut())
			return new Patrol(actor, core, core.getEnemyData(), false);
		return super.nextMove();
	}

}
