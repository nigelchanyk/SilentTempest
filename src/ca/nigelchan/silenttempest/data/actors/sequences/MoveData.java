package ca.nigelchan.silenttempest.data.actors.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Move;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;

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
