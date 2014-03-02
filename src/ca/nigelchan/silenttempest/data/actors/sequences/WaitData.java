package ca.nigelchan.silenttempest.data.actors.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Wait;
import ca.nigelchan.silenttempest.util.Coordinate;

public class WaitData extends SequenceData {
	
	private float time;
	
	public WaitData(float time) {
		this.time = time;
	}

	@Override
	public Sequence create(Actor actor, Coordinate initialPosition) {
		return new Wait(actor, initialPosition, time);
	}

}
