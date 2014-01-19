package ca.nigelchan.operationbanana.objects.actors.enemystrategies;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.controllers.EnemyCore;
import ca.nigelchan.operationbanana.util.Coordinate;

public class Approach extends EnemyStrategy {
	
	private Coordinate dest;

	public Approach(Actor actor, EnemyCore core, Coordinate dest) {
		super(actor, core);
		this.dest = dest;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnemyStrategy nextMove() {
		// TODO Auto-generated method stub
		return null;
	}

}
