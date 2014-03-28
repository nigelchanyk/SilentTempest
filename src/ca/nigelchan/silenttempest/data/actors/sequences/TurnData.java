package ca.nigelchan.silenttempest.data.actors.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Turn;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.Vector2;

public class TurnData extends SequenceData {
	
	private Direction direction;

	public TurnData(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Sequence create(Actor actor, Vector2 initialPosition) {
		return new Turn(actor, initialPosition, direction);
	}

}
