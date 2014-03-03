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
	public static final float SNAPPING_BUFFER = 0.01f;

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

	public void move(Vector2 delta) {
		Vector2 newPos = _move(delta);
		// Can use equality for floating points because snapping always yield the same number
		if (newPos.x() == getPosition().x() && newPos.y() == getPosition().y()) {
			// Edge smoothening
			if (Math.abs(delta.y()) < 0.00001f) {
				Coordinate target = getGridPosition().add(delta.x() > 0 ? 1 : -1, 0);
				if (world.isWalkable(target)) {
					float targetCenterY = target.toCenterVector2().y();
					float magnitude = Math.min(Math.abs(delta.x()), Math.abs(targetCenterY - getPosition().y()));
					newPos = _move(
						new Vector2(
							0,
							targetCenterY > getPosition().y() ? magnitude : -magnitude
						)
					);
				}
			}
			else if (Math.abs(delta.x()) < 0.00001f) {
				Coordinate target = getGridPosition().add(0, delta.y() > 0 ? 1 : -1);
				if (world.isWalkable(target)) {
					float targetCenterX = target.toCenterVector2().x();
					float magnitude = Math.min(Math.abs(delta.y()), Math.abs(targetCenterX - getPosition().x()));
					newPos = _move(
						new Vector2(
							targetCenterX > getPosition().x() ? magnitude : -magnitude,
							0
						)
					);
				}
			}
		}
		setPosition(newPos);
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
	
	private Vector2 _move(Vector2 delta) {
		float newX = getPosition().x() + delta.x();
		if (!getWorld().isValidPosition(new Vector2(newX, getPosition().y()), this)) {
			if (delta.x() < 0)
				newX = getGridPosition().x() + getRadius() + SNAPPING_BUFFER;
			else if (delta.x() > 0)
				newX = getGridPosition().x() + 1 - getRadius() - SNAPPING_BUFFER;
			else
				newX = getPosition().x();
		}
		float newY = getPosition().y() + delta.y();
		if (!getWorld().isValidPosition(new Vector2(newX, newY), this)) {
			if (delta.y() < 0)
				newY = getGridPosition().y() + getRadius() + SNAPPING_BUFFER;
			else if (delta.y() > 0)
				newY = getGridPosition().y() + 1 - getRadius() - SNAPPING_BUFFER;
			else
				newY = getPosition().y();
		}
		return new Vector2(newX, newY);
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
