package ca.nigelchan.operationbanana.objects.actors.enemystrategies;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.data.actors.sequences.SequenceData;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.controllers.EnemyCore;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.SequenceSet;
import ca.nigelchan.operationbanana.util.Coordinate;

public class Patrol extends EnemyStrategy {
	
	private static final float ALERT_REDUCTION_FACTOR = 0.2f;

	private Approach approach;
	private int current = 0;
	private Sequence[] sequence;
	private SequenceSet sequenceSet = new SequenceSet();
	private boolean firstUpdate = true;

	public Patrol(Actor actor, EnemyCore core, EnemyData data, boolean justSpawned) {
		super(actor, core);
		sequence = new Sequence[data.getSequenceSize()];
		int i = 0;
		Coordinate pos = data.getInitialPosition();
		for (SequenceData sequenceItem : data.getSequence()) {
			sequence[i] = sequenceItem.create(actor, pos);
			sequenceSet.add(pos, i);
			pos = pos.add(sequenceItem.getTranslation());
			i++;
		}
		if (justSpawned) {
            sequence[current].onSpawn();
            sequence[current].onStart();
		}
		else if (!sequenceSet.contains(actor.getGridPosition()))
			approach = new Approach(actor, core, sequenceSet, this);
		else
            sequence[current].onStart();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (firstUpdate) {
			firstUpdate = false;
			current = sequenceSet.getIndex(actor.getGridPosition());
			sequence[current].onStart();
		}
		core.setAlertLevel(core.getAlertLevel() - ALERT_REDUCTION_FACTOR * elapsedTime);
		sequence[current].onUpdate(elapsedTime);
		if (sequence[current].isCompleted()) {
			current = next();
			sequence[current].onStart();
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		if (core.getAlertLevel() >= 0.5f && core.canSee(actor.getWorld().getPlayer().getPosition()))
			return new Investigate(actor, core);
		if (approach != null) {
			Approach next = approach;
			approach = null;
			return next;
		}
		return this;
	}
	
	private int next() {
		return (current + 1) % sequence.length;
	}

}
