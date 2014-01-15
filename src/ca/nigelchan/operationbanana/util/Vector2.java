package ca.nigelchan.operationbanana.util;

public class Vector2 {
	
	public static final Vector2 ZERO = new Vector2(0, 0);
	
	private float _x;
	private float _y;
	
	public Vector2(float x, float y) {
		_x = x;
		_y = y;
	}
	
	public Vector2 add(Vector2 other) {
		return new Vector2(x() + other.x(), y() + other.y());
	}

	public float distanceSquare(Vector2 target) {
		return distanceSquare(target.x(), target.y());
	}
	
	public float distanceSquare(float x, float y) {
		return (x() - x) * (x() - x) + (y() - y) * (y() - y);
	}
	
	public Vector2 minus(Vector2 other) {
		return new Vector2(x() - other.x(), y() - other.y());
	}
	
	public Vector2 multiply(float scalar) {
		return new Vector2(x() * scalar, y() * scalar);
	}

	public float x() {
		return _x;
	}
	
	public float y() {
		return _y;
	}
}
