package ca.nigelchan.operationbanana.data.actors.sequences;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.operationbanana.util.Coordinate;

public abstract class SequenceData {
	
	public abstract Sequence create(Actor actor, Coordinate initialPosition);
	
	public Coordinate getTranslation() {
		return Coordinate.ZERO;
	}

}
