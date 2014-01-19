package ca.nigelchan.operationbanana.data.actors.sequences;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Turn;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.Direction;

public class TurnData extends SequenceData {
	
	private Direction direction;

	public TurnData(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Sequence create(Actor actor, Coordinate initialPosition) {
		return new Turn(actor, initialPosition, direction);
	}

}
