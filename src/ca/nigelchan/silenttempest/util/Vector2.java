package ca.nigelchan.silenttempest.util;

public class Vector2 {
	
	public static final Vector2 ZERO = new Vector2(0, 0);
	
	private float _x;
	private float _y;
	
	public Vector2(float x, float y) {
		_x = x;
		_y = y;
	}
	
	public Vector2 add(Vector2 other) {
		return add(other.x(), other.y());
	}
	
	public Vector2 add(float x, float y) {
		return new Vector2(x() + x, y() + y);
	}
	
	public float distance(Vector2 target) {
		return (float)Math.sqrt(distanceSquare(target));
	}

	public float distanceSquare(Vector2 target) {
		return distanceSquare(target.x(), target.y());
	}
	
	public float distanceSquare(float x, float y) {
		return (x() - x) * (x() - x) + (y() - y) * (y() - y);
	}
	
	public float dot(Vector2 other) {
		return x() * other.x() + y() * other.y();
	}
	
	public Vector2 floor() {
		return new Vector2((float)Math.floor(x()), (float)Math.floor(y()));
	}
	
	public float length() {
		return (int)Math.sqrt(x() * x() + y() * y());
	}
	
	public Vector2 minus(Vector2 other) {
		return new Vector2(x() - other.x(), y() - other.y());
	}
	
	public Vector2 multiply(float scalar) {
		return new Vector2(x() * scalar, y() * scalar);
	}
	
	public Vector2 normal() {
		float len = length();
		return new Vector2(x() / len, y() / len);
	}
	
	public Coordinate toCoordinate() {
		return new Coordinate((int)x(), (int)y());
	}

	@Override
	public String toString() {
		return "(" + x() + ", " + y() + ")";
	}

	public float x() {
		return _x;
	}
	
	public float y() {
		return _y;
	}
}
