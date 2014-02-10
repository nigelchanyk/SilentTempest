package ca.nigelchan.silenttempest.data.actors.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.util.Coordinate;

public abstract class SequenceData {
	
	public abstract Sequence create(Actor actor, Coordinate initialPosition);
	
	public Coordinate getTranslation() {
		return Coordinate.ZERO;
	}

}
