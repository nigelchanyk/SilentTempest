package ca.nigelchan.silenttempest.data.actors.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class SequenceData {
	
	public abstract Sequence create(Actor actor, Vector2 initialPosition);
	
	public Coordinate getTranslation() {
		return Coordinate.ZERO;
	}

}
