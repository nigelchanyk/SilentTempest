package ca.nigelchan.operationbanana.objects.actors.controllers;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.objects.actors.Enemy;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.EnemyStrategy;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.Patrol;

public class EnemyCore extends Controller {

	private Patrol patrol;
	private EnemyStrategy strategy;

	public EnemyCore(Enemy actor, EnemyData data) {
		super(actor);
		patrol = new Patrol(actor, this, data);
		strategy = patrol;
		setEnabled(true);
	}

	@Override
	protected void onUpdate(float elapsedTime) {
		EnemyStrategy previous = strategy;
		for (int i = 0; i < 10; ++i) {
			strategy = strategy.nextMove();
			if (previous == strategy) {
				strategy.onUpdate(elapsedTime);
				return;
			}
			previous = strategy;
		}

        throw new IllegalStateException("Non deterministic enemy state.");
	}

}
