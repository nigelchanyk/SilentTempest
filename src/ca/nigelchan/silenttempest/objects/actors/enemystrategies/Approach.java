package ca.nigelchan.silenttempest.objects.actors.enemystrategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Move;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.SequenceSet;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;

public class Approach extends EnemyStrategy {
	
	private Sequence current = null;
	private Coordinate destination;
	private boolean firstUpdate = true;
	private EnemyStrategy nextStrategy;
	private Snap snap = null;
	private Iterable<Move> path;
	private Iterator<Move> pathItr;
	
	public Approach(Actor actor, EnemyCore core, SequenceSet sequenceSet, EnemyStrategy nextStrategy) {
		super(actor, core);
		this.nextStrategy = nextStrategy;
		path = findPath(actor, sequenceSet);
		if (path != null) {
			pathItr = path.iterator();
			if (pathItr.hasNext()) {
				Move first = pathItr.next();
				current = first;
				snap = new Snap(
					actor,
					core,
					first.getInitialPosition().add(MathHelper.getTranslation(first.getDirection())),
					this
				);
			}
			else {
				destination = actor.getGridPosition();
				snap = new Snap(
					actor,
					core,
					destination,
					nextStrategy
				);
			}
		}
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (firstUpdate) {
            firstUpdate = false;
			if (!pathItr.hasNext()) {
				current = null;
				return;
			}
            current = pathItr.next();
            current.onStart();
		}
		current.onUpdate(elapsedTime);
		if (current.isCompleted()) {
			if (pathItr.hasNext()) {
				current = pathItr.next();
				current.onStart();
			}
			else {
				current = null;
			}
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		if (core.getAlertLevel() >= 0.5f && core.canSee(actor.getWorld().getPlayer().getPosition()))
			return new Investigate(actor, core);
		if (snap != null) {
			Snap next = snap;
			snap = null;
			return next;
		}
		if (current == null)
			return nextStrategy;
		return this;
	}
	
	public Iterable<Move> findPath(Actor actor, SequenceSet sequenceSet) {
		if (sequenceSet.contains(actor.getGridPosition())) {
			return new ArrayList<Move>();
		}
		Direction[][] directions = new Direction[actor.getWorld().getWidth()][actor.getWorld().getHeight()];
		LinkedList<Coordinate> queue = new LinkedList<Coordinate>();
		Coordinate start = actor.getGridPosition();
		queue.add(start);
        Coordinate next = null;
		while (!queue.isEmpty()) {
			Coordinate current = queue.poll();
			for (Direction direction : MathHelper.getNonDiagonalDirections()) {
				Coordinate translation = MathHelper.getTranslation(direction);
				next = current.add(translation);
				if (!actor.getWorld().isWalkable(next))
					continue;
				if (directions[next.y()][next.x()] != null)
					continue;
				directions[next.y()][next.x()] = direction;
				if (sequenceSet.contains(next)) {
					destination = next;
					current = next;
                    LinkedList<Move> path = new LinkedList<Move>();
					while (!current.equals(start)) {
						direction = directions[current.y()][current.x()];
						next = current.add(MathHelper.getTranslation(MathHelper.reverse(direction)));
						path.addFirst(new Move(actor, next, direction));
						current = next;
					}
					return path;
				}
				queue.push(next);
			}
		}
		
		return null;
	}

	public Coordinate getDestination() {
		return destination;
	}

}
