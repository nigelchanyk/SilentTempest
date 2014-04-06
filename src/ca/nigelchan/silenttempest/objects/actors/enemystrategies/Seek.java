package ca.nigelchan.silenttempest.objects.actors.enemystrategies;


import java.util.Iterator;
import java.util.LinkedList;

import android.util.Log;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Move;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Seek extends EnemyStrategy {
	
	private static final float MAX_SEEK_TIME = 20;
	public static final int SCAN_RADIUS = 20;
	
	private Move currentSequence;
	private Iterator<Move> path = null;
	private int[][] suspicionLevel;
	private float timeSinceLastSuspicionIncrement = 0;
	private float totalSeekTime = 0;

	public Seek(Actor actor, EnemyCore core, Coordinate lastSeenPosition, float lastSeenRotation) {
		super(actor, core);
		suspicionLevel = new int[actor.getWorld().getHeight()][actor.getWorld().getWidth()];
		for (int y = 0; y < suspicionLevel.length; ++y) {
			for (int x = 0; x <suspicionLevel[0].length; ++x) {
				suspicionLevel[y][x] = 1;
			}
		}
		raytraceVisibleGrids();
		fillSuspicion(lastSeenPosition, lastSeenRotation);
	}
	
	@Override
	public void onUpdate(float elapsedTime) {
		timeSinceLastSuspicionIncrement += elapsedTime;
		totalSeekTime += elapsedTime;
		if (timeSinceLastSuspicionIncrement >= 1) {
			timeSinceLastSuspicionIncrement -= 1;
			increaseSuspicionLevel();
		}
		if (path == null) {
			raytraceVisibleGrids();
			if (!startNewPath()) {
				return;
			}
			currentSequence.onStart();
		}
		
		currentSequence.onUpdate(elapsedTime);
		if (currentSequence.isCompleted()) {
			raytraceVisibleGrids();
			if (path.hasNext())
				currentSequence = path.next();
			else if (!startNewPath()) {
				return;
			}
			currentSequence.onStart();
		}
	}

	@Override
	public EnemyStrategy nextMove() {
		if (core.getAlertLevel() >= 0.5f && core.canSee(actor.getWorld().getPlayer().getPosition()))
			return new Investigate(actor, core);
		if (totalSeekTime >= MAX_SEEK_TIME)
			return new Patrol(actor, core, core.getEnemyData(), false);
		return this;
	}
	
	private void castVisibleArea(
		Coordinate startPosition,
		Vector2 unitVector,
		float minimumDotProduct,
		int row,
		float start,
		float end,
		int xx,
		int xy,
		int yx,
		int yy
	) {
		float newStart = 0.0f;
		if (start < end) {
			return;
		}
		boolean blocked = false;
		for (int distance = row; distance <= SCAN_RADIUS && !blocked; distance++) {
			int deltaY = -distance;
			for (int deltaX = -distance; deltaX <= 0; deltaX++) {
				Coordinate current = new Coordinate(
					startPosition.x() + deltaX * xx + deltaY * xy,
					startPosition.y() + deltaX * yx + deltaY * yy
				);
				float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
				float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);
	 
				if (actor.getWorld().isOutOfBound(current) || start < rightSlope) {
					continue;
				} else if (end > leftSlope) {
					break;
				}
	 
				if (suspicionLevel[current.y()][current.x()] > 0) {
					float dotProduct = unitVector.dot(current.toCenterVector2().normal());
					if (dotProduct > minimumDotProduct)
						suspicionLevel[current.y()][current.x()] = 0;
				}
	 
				if (blocked) {
					//previous cell was a blocking one
					if (!actor.getWorld().isWalkable(current)) {
						//hit a wall
						newStart = rightSlope;
						continue;
					} else {
						blocked = false;
						start = newStart;
					}
				} else {
					if (!actor.getWorld().isWalkable(current) && distance < SCAN_RADIUS) {
						//hit a wall within sight line
						blocked = true;
						castVisibleArea(
							startPosition,
							unitVector,
							minimumDotProduct,
							distance + 1,
							start,
							leftSlope,
							xx,
							xy,
							yx,
							yy
						);
						newStart = rightSlope;
					}
				}
			}
		}
	}
	
	private void fillSuspicion(Coordinate peak, float lastSeenRotation) {
		World world = actor.getWorld();
		boolean[][] visited = new boolean[world.getHeight()][world.getWidth()];
		LinkedList<Coordinate> queue = new LinkedList<Coordinate>();
		int max = world.getWidth() * world.getHeight();
		Vector2 predictedDelta = MathHelper.getUnitVector(lastSeenRotation);
		suspicionLevel[peak.y()][peak.x()] = max;
		visited[peak.y()][peak.x()] = true;
		queue.add(peak);
		Coordinate predicted = new Coordinate(predictedDelta.x() > 0 ? 1 : -1, peak.y());
		if (Math.abs(predictedDelta.x()) > 0.5f && world.isWalkable(predicted)) {
			suspicionLevel[predicted.y()][predicted.x()] = max;
			visited[predicted.y()][predicted.x()] = true;
			queue.add(predicted);
		}
		else {
			predicted = new Coordinate(peak.x(), predictedDelta.y() > 0 ? 1 : -1);
			if (Math.abs(predictedDelta.y()) > 0.5f && world.isWalkable(predicted)) {
				suspicionLevel[predicted.y()][predicted.x()] = max;
				visited[predicted.y()][predicted.x()] = true;
				queue.add(predicted);
			}
		}
		while (!queue.isEmpty()) {
			Coordinate current = queue.poll();
			int nextSuspicionLevel = suspicionLevel[current.y()][current.x()] - 1;
			for (Direction direction : Direction.values()) {
				Coordinate next = current.add(MathHelper.getTranslation(direction));
				if (!actor.getWorld().isWalkable(next))
					continue;
				if (visited[next.y()][next.x()])
					continue;
				visited[next.y()][next.x()] = true;
				if (suspicionLevel[next.y()][next.x()] <= 0)
					continue;
				suspicionLevel[next.y()][next.x()] = nextSuspicionLevel;
				queue.addLast(next);
			}
		}
	}
	
	private Coordinate getSuspicionPeak() {
		Coordinate position = actor.getGridPosition();
		Coordinate peak = position;
		int bestDistance = 0;
		for (int y = 0; y < suspicionLevel.length; ++y) {
			for (int x = 0; x < suspicionLevel[0].length; ++x) {
				if (!actor.getWorld().isWalkable(new Coordinate(x, y)))
					continue;
				int distance = Math.abs(x - position.x()) + Math.abs(y - position.y());
				if (suspicionLevel[y][x] > suspicionLevel[peak.y()][peak.x()]
						|| (suspicionLevel[y][x] == suspicionLevel[peak.y()][peak.x()]
						&& distance < bestDistance)) {
					bestDistance = distance;
					peak = new Coordinate(x, y);
				}
			}
		}
		return peak;
	}
	
	private void increaseSuspicionLevel() {
		for (int y = 0; y < suspicionLevel.length; ++y) {
			for (int x = 0; x < suspicionLevel[0].length; ++x) {
				if (suspicionLevel[y][x] < Integer.MAX_VALUE)
					suspicionLevel[y][x]++;
			}
		}
	}
	
	private void raytraceVisibleGrids() {
		Coordinate startPosition = actor.getGridPosition();
		suspicionLevel[startPosition.y()][startPosition.x()] = 0;
		Vector2 unitVector = MathHelper.getUnitVector(actor.getRadianRotation());
		float minimumDotProduct = unitVector.dot(MathHelper.getUnitVector(actor.getRadianRotation() + EnemyCore.HALF_FIELD_OF_VIEW));
		for (Direction direction : MathHelper.getDiagonalDirections()) {
			Coordinate delta = MathHelper.getTranslation(direction);
			castVisibleArea(startPosition, unitVector, minimumDotProduct, 1, 1, 0, 0, delta.x(), delta.y(), 0);
			castVisibleArea(startPosition, unitVector, minimumDotProduct, 1, 1, 0, delta.x(), 0, 0, delta.y());
		}
	}
	
	// Return false if failed
	private boolean startNewPath() {
		path = null;
		Coordinate peak = getSuspicionPeak();
		// Should not happen
		if (peak.equals(actor.getGridPosition())) {
			throw new IllegalArgumentException();
		}
		Iterable<Move> movement = actor.getWorld().findPath(actor, peak);
		// Not supposed to happen
		if (movement == null) {
			Log.i("FUCK", actor.getGridPosition().toString() + " " + peak.toString());
			return false;
		}
		path = movement.iterator();
		// Not supposed to happen
		if (!path.hasNext()) {
			return false;
		}
		currentSequence = path.next();
		return true;
	}

}
