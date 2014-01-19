package ca.nigelchan.operationbanana.objects.actors.enemystrategies;

import java.util.HashSet;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.data.actors.sequences.SequenceData;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.controllers.EnemyCore;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.operationbanana.util.Coordinate;

public class Patrol extends EnemyStrategy {

	private int current = 0;
	private Sequence[] sequence;
	private SequenceSet sequenceSet = new SequenceSet();

	public Patrol(Actor actor, EnemyCore core, EnemyData data) {
		super(actor, core);
		sequence = new Sequence[data.getSequenceSize()];
		int i = 0;
		Coordinate pos = data.getInitialPosition();
		for (SequenceData sequenceItem : data.getSequence()) {
			sequence[i] = sequenceItem.create(actor, pos);
			sequenceSet.add(pos);
			pos = pos.add(sequenceItem.getTranslation());
			i++;
		}
		sequence[current].onSpawn();
		sequence[current].onStart();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		sequence[current].onUpdate(elapsedTime);
		if (sequence[current].isCompleted()) {
			current = next();
			sequence[current].onStart();
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		return this;
	}
	
	private int next() {
		return (current + 1) % sequence.length;
	}


	private static class SequenceSet {
		
		private HashSet<Integer> set = new HashSet<Integer>();
		
		public void add(Coordinate coordinate) {
			set.add(coordinateHash(coordinate));
		}
		
		public boolean contains(Coordinate coordinate) {
			return set.contains(coordinateHash(coordinate));
		}
		
		private int coordinateHash(Coordinate coordinate) {
			return (coordinate.x() << 16) + coordinate.y();
		}
	}

}
