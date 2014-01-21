package ca.nigelchan.operationbanana.objects;

import ca.nigelchan.operationbanana.entity.OffsetEntity;
import ca.nigelchan.operationbanana.util.Vector2;

/**
 * @author nigelchan
 * 
 * WorldObject abstracts out the underlying dimensions.
 * All position is in world grid unit.
 *
 */
public class WorldObject extends OffsetEntity {

	protected World world;
	
	private Vector2 position;
	
	public WorldObject(Vector2 position, OffsetEntity.OffsetType offsetType, World world) {
		super(world.getUnitScale(), world.getUnitScale(), offsetType);
		this.world = world;
		_setPosition(position);
		super.setRotationCenter(world.getUnitScale() * 0.5f, world.getUnitScale() * 0.5f);
	}
	
	// Getters
	public World getWorld() {
		return world;
	}
	
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public float getX() {
		return position.x();
	}

	@Override
	public float getY() {
		return position.y();
	}

	// Setters
	public void setPosition(Vector2 position) {
		_setPosition(position);
	}

	@Override
	public void setPosition(float pX, float pY) {
		setPosition(new Vector2(pX, pY));
	}
	
	@Override
	public void setX(float pX) {
		setPosition(new Vector2(pX, getY()));
	}
	
	@Override
	public void setY(float pY) {
		setPosition(new Vector2(getX(), pY));
	}

	// Encapsulated to prevent constructor calls being overridden
	private void _setPosition(Vector2 position) {
		this.position = position;
		super.setPosition(position.x() * world.getUnitScale(), position.y() * world.getUnitScale());
	}
}
