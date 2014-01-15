package ca.nigelchan.operationbanana.objects.actors;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;

import android.graphics.Point;
import ca.nigelchan.operationbanana.data.actors.ActorData;
import ca.nigelchan.operationbanana.objects.World;
import ca.nigelchan.operationbanana.objects.WorldObject;
import ca.nigelchan.operationbanana.objects.actors.action.Action;
import ca.nigelchan.operationbanana.util.Vector2;

public abstract class Actor extends WorldObject {
	
	public enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}

	private ArrayList<Action> actions = new ArrayList<Action>(4);
	private float speed;

	public Actor(ActorData data, World world) {
		super(data.getInitialPosition(), world);

		setRotation(data.getInitialRotation());
		speed = data.getSpeed();

		registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				handleUpdate(pSecondsElapsed);
			}
		});
	}
	
	public void addAction(Action action) {
		if (actions.contains(action))
			return;
		actions.add(action);
	}
	
	public void moveTo(Vector2 position) {
		if (world.isValidPosition(position, this)) {
			setPosition(position);
			return;
		}
		moveToX(position.x());
		moveToY(position.y());
	}
	
	public void removeAction(Action action) {
		actions.remove(action);
	}
	
	public void snapToGrid() {
		Vector2 pos = world.convertPointToVector2(getGridPosition());
		setPosition(pos.x(), pos.y());
	}
	
	private void handleUpdate(float elapsedTime) {
		for (Action action : actions)
			action.update(elapsedTime);
	}
	
	private void moveToX(float x) {
		if (world.isValidPosition(new Vector2(x, getY()), this)) {
			setX(x);
			return;
		}

		float minX = getX();
		float maxX = x;
		for (int i = 0; i < 4; ++i) {
            float mid = (minX + maxX) * 0.5f;
			if (world.isValidPosition(new Vector2(mid, getY()), this))
				minX = mid;
			else
				maxX = mid;
		}
		
		setX(minX);
	}

	private void moveToY(float y) {
		if (world.isValidPosition(new Vector2(getX(), y), this)) {
			setY(y);
			return;
		}

		float minY = getY();
		float maxY = y;
		for (int i = 0; i < 4; ++i) {
            float mid = (minY + maxY) * 0.5f;
			if (world.isValidPosition(new Vector2(getX(), mid), this))
				minY = mid;
			else
				maxY = mid;
		}
		
		setY(minY);
	}
	
	private int toGridCoordinate(float precise) {
		return Math.round(precise);
	}

	// Getters
	public Point getGridPosition() {
		return new Point(toGridCoordinate(getX()), toGridCoordinate(getY()));
	}
	
	public float getRadius() {
		return 0.4f;
	}
	
	public float getSpeed() {
		return speed;
	}

	public World getWorld() {
		return world;
	}
}
