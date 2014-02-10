package ca.nigelchan.silenttempest.util;

public class Coordinate {
	
	public static final Coordinate ZERO = new Coordinate(0, 0);
	
	private int _x;
	private int _y;
	
	public Coordinate(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public Coordinate add(Coordinate other) {
		return add(other.x(), other.y());
	}
	
	public Coordinate add(int x, int y) {
		return new Coordinate(x() + x, y() + y);
	}
	
	public boolean equals(Coordinate other) {
		return x() == other.x() && y() == other.y();
	}
	
	public Coordinate minus(Coordinate other) {
		return new Coordinate(x() - other.x(), y() - other.y());
	}
	
	public Coordinate multiply(int scalar) {
		return new Coordinate(x() * scalar, y() * scalar);
	}
	
	public Vector2 toCenterVector2() {
		return new Vector2(x() + 0.5f, y() + 0.5f);
	}

	@Override
	public String toString() {
		return "(" + x() + ", " + y() + ")";
	}
	
	public Vector2 toVector2() {
		return new Vector2(x(), y());
	}

	public int x() {
		return _x;
	}
	
	public int y() {
		return _y;
	}

}
