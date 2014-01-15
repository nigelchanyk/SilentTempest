package ca.nigelchan.operationbanana.objects;

import ca.nigelchan.operationbanana.entity.CenteredEntity;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

/**
 * @author nigelchan
 * 
 * WorldObject abstracts out the underlying dimensions.
 * All position is in world grid unit.
 *
 */
public class WorldObject extends CenteredEntity {

	protected World world;
	
	private Vector2 position;
	
	public WorldObject(Vector2 position, World world) {
		super(world.getUnitScale(), world.getUnitScale());
		this.world = world;
		setPosition(position);
		setRotationCenter(world.getUnitScale() * 0.5f, world.getUnitScale() * 0.5f);
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
		this.position = position;
		super.setPosition(position.x() * world.getUnitScale(), position.y() * world.getUnitScale());
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

	public void setRadianRotation(float rotation) {
		super.setRotation(MathHelper.toDegrees(rotation));
	}

}
