package ca.nigelchan.operationbanana.entity;

import org.andengine.entity.Entity;

import ca.nigelchan.operationbanana.util.Vector2;

public class CenteredEntity extends Entity {
	
	private Vector2 offset;

	public CenteredEntity(float width, float height) {
		offset = new Vector2(width / 2, height / 2);
		setRotationCenter(offset.x(), offset.y());
	}
	
	// Getters
	@Override
	public float getX() {
		return super.getX() + offset.x();
	}
	
	@Override
	public float getY() {
		return super.getY() + offset.y();
	}

	// Setters
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x - offset.x(), y - offset.y());
	}
	
	@Override
	public void setX(float x) {
		super.setX(x - offset.x());
	}

	@Override
	public void setY(float y) {
		super.setY(y - offset.y());
	}

}
