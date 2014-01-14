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
	
	private int toGridCoordinate(float precise) {
		return Math.round(precise);
	}

	// Getters
	public Point getGridPosition() {
		return new Point(toGridCoordinate(getX()), toGridCoordinate(getY()));
	}
	
	public float getSpeed() {
		return speed;
	}

	public World getWorld() {
		return world;
	}
}
