package ca.nigelchan.silenttempest.objects.actors;

import org.andengine.engine.handler.IUpdateHandler;

import ca.nigelchan.silenttempest.data.actors.ActorData;
import ca.nigelchan.silenttempest.entity.OffsetEntity;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.WorldObject;
import ca.nigelchan.silenttempest.objects.actors.controllers.Controller;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.PostponedList;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Actor extends WorldObject {

	public static final float ROTATION_PER_SEC = MathHelper.TWO_PI * 2;

	private PostponedList<Controller> controllers = new PostponedList<Controller>(4);
	private float speed;
	private PostponedList<IListener> subscribers = new PostponedList<Actor.IListener>(4);

	public Actor(ActorData data, World world) {
		super(data.getInitialPosition().toCenterVector2(), OffsetEntity.OffsetType.CENTER, world);

		super.setRotation(data.getInitialRotation());
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
	
	public void addController(Controller controller) {
		if (controllers.contains(controller))
			return;
		controllers.add(controller);
	}

	public void moveTo(Vector2 position) {
		if (world.isValidPosition(position, this)) {
			setPosition(position);
			return;
		}
		moveToX(position.x());
		moveToY(position.y());
	}
	
	public void removeController(Controller controller) {
		controllers.remove(controller);
	}
	
	public void subscribe(IListener subscriber) {
		if (subscribers.contains(subscriber))
			return;
		subscribers.add(subscriber);
	}
	
	public void unsubscribe(IListener subscriber) {
		subscribers.remove(subscriber);
	}
	
	private void handleUpdate(float elapsedTime) {
		for (Controller controller : controllers)
			controller.update(elapsedTime);
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
	
	private void notifyPositionChanged() {
		Vector2 position = getPosition();
		for (IListener subscriber : subscribers)
			subscriber.onPositionChanged(position);
	}
	
	private void notifyRotationChanged() {
		float rotation = getRadianRotation();
		for (IListener subscriber : subscribers)
			subscriber.onRotationChanged(rotation);
	}

	// Getters
	public Coordinate getGridPosition() {
		return getPosition().toCoordinate();
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
	
	// Setters
	@Override
	public void setPosition(Vector2 position) {
		if (getWorld().isOutOfBound(position.toCoordinate()))
			throw new IllegalArgumentException("Invalid position: " + position.x() + ", " + position.y());
		super.setPosition(position);
		notifyPositionChanged();
	}

	@Override
	public void setPosition(float pX, float pY) {
		super.setPosition(pX, pY);
		notifyPositionChanged();
	}

	@Override
	public void setRadianRotation(float rotation) {
		super.setRadianRotation(rotation);
		notifyRotationChanged();
	}

	@Override
	public void setX(float pX) {
		super.setX(pX);
		notifyPositionChanged();
	}

	@Override
	public void setY(float pY) {
		super.setY(pY);
		notifyPositionChanged();
	}

	public static interface IListener {
		
		public void onPositionChanged(Vector2 position);
		public void onRotationChanged(float rotation);

	}
}