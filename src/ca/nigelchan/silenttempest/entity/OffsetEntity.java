package ca.nigelchan.silenttempest.entity;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class OffsetEntity extends Entity {
	
	public enum OffsetType {
		NONE,
		CENTER
	}
	
	private float height;
	private Vector2 offset;
	private float radius = 0.5f;
	private OffsetType type;
	private float width;

	public OffsetEntity(float width, float height, OffsetType offsetType) {
		offset = offsetType == OffsetType.NONE ? Vector2.ZERO : new Vector2(width * radius, height * radius);
		type = offsetType;
		this.width = width;
		this.height = height;
		setRotationCenter(offset.x(), offset.y());
	}
	
	public float getRadianRotation() {
		return MathHelper.toRadians(getRotation());
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

	public void setRadianRotation(float rotation) {
		super.setRotation(MathHelper.toDegrees(rotation));
	}
	
	public void setRadius(float radius) {
		float difference = radius - this.radius;
		this.radius = radius;
		offset = type == OffsetType.NONE ? Vector2.ZERO : new Vector2(radius * width, radius * height);
		setRotationCenter(offset.x(), offset.y());
		setX(getX() - difference * width);
		setY(getY() - difference * height);
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
