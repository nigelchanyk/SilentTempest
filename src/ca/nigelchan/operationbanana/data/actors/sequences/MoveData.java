package ca.nigelchan.operationbanana.data.actors.sequences;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Move;
import ca.nigelchan.operationbanana.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.Direction;
import ca.nigelchan.operationbanana.util.MathHelper;

public class MoveData extends SequenceData {
	
	private Direction direction;
	
	public MoveData(Direction direction) {
		this.direction = direction;
	}

	@Override
	public Sequence create(Actor actor, Coordinate initialPosition) {
		return new Move(actor, initialPosition, direction);
	}

	@Override
	public Coordinate getTranslation() {
		return MathHelper.getTranslation(direction);
	}

}
