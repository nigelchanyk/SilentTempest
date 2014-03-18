package ca.nigelchan.silenttempest.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public final class MathHelper {

	public static final float PI = (float)Math.PI;
	public static final float PI_OVER_2 = (float)Math.PI * 0.5f;
	public static final float PI_OVER_4 = (float)Math.PI * 0.25f;
	public static final float PI_OVER_8 = (float)Math.PI * 0.125f;
	public static final float THREE_PI_OVER_2 = (float)Math.PI * 1.5f;
	public static final float THREE_PI_OVER_4 = (float)Math.PI * 0.75f;
	private static final EnumMap<Direction, Direction> REVERSE_MAPPER = new EnumMap<Direction, Direction>(Direction.class);
	private static final EnumMap<Direction, Float> ROTATION_MAPPER = new EnumMap<Direction, Float>(Direction.class);
	private static final EnumMap<Direction, Coordinate> TRANSLATION_MAPPER = new EnumMap<Direction, Coordinate>(Direction.class);
	private static final ArrayList<Direction> DIAGONAL_DIRECTIONS = new ArrayList<Direction>();
	private static final ArrayList<Direction> NON_DIAGONAL_DIRECTIONS = new ArrayList<Direction>();
	public static final float TWO_PI = (float)Math.PI * 2;
	
	static {
		TRANSLATION_MAPPER.put(Direction.NORTH, new Coordinate(0, -1));
		TRANSLATION_MAPPER.put(Direction.NORTHEAST, new Coordinate(1, -1));
		TRANSLATION_MAPPER.put(Direction.EAST, new Coordinate(1, 0));
		TRANSLATION_MAPPER.put(Direction.SOUTHEAST, new Coordinate(1, 1));
		TRANSLATION_MAPPER.put(Direction.SOUTH, new Coordinate(0, 1));
		TRANSLATION_MAPPER.put(Direction.SOUTHWEST, new Coordinate(1, 1));
		TRANSLATION_MAPPER.put(Direction.WEST, new Coordinate(-1, 0));
		TRANSLATION_MAPPER.put(Direction.NORTHWEST, new Coordinate(-1, -1));
		
		ROTATION_MAPPER.put(Direction.NORTH, 0f);
		ROTATION_MAPPER.put(Direction.NORTHEAST, PI_OVER_4);
		ROTATION_MAPPER.put(Direction.EAST, PI_OVER_2);
		ROTATION_MAPPER.put(Direction.SOUTHEAST, THREE_PI_OVER_4);
		ROTATION_MAPPER.put(Direction.SOUTH, PI);
		ROTATION_MAPPER.put(Direction.SOUTHWEST, PI + PI_OVER_4);
		ROTATION_MAPPER.put(Direction.WEST, THREE_PI_OVER_2);
		ROTATION_MAPPER.put(Direction.NORTHWEST, PI + THREE_PI_OVER_4);
		
		REVERSE_MAPPER.put(Direction.NORTH, Direction.SOUTH);
		REVERSE_MAPPER.put(Direction.NORTHEAST, Direction.SOUTHWEST);
		REVERSE_MAPPER.put(Direction.EAST, Direction.WEST);
		REVERSE_MAPPER.put(Direction.SOUTHEAST, Direction.NORTHWEST);
		REVERSE_MAPPER.put(Direction.SOUTH, Direction.NORTH);
		REVERSE_MAPPER.put(Direction.SOUTHWEST, Direction.NORTHEAST);
		REVERSE_MAPPER.put(Direction.WEST, Direction.EAST);
		REVERSE_MAPPER.put(Direction.NORTHWEST, Direction.SOUTHEAST);
		
		NON_DIAGONAL_DIRECTIONS.add(Direction.NORTH);
		NON_DIAGONAL_DIRECTIONS.add(Direction.EAST);
		NON_DIAGONAL_DIRECTIONS.add(Direction.SOUTH);
		NON_DIAGONAL_DIRECTIONS.add(Direction.WEST);

		DIAGONAL_DIRECTIONS.add(Direction.NORTHEAST);
		DIAGONAL_DIRECTIONS.add(Direction.NORTHWEST);
		DIAGONAL_DIRECTIONS.add(Direction.SOUTHEAST);
		DIAGONAL_DIRECTIONS.add(Direction.SOUTHWEST);
	}
	
	private static final Random random = new Random();
	
	public static Coordinate abs(Coordinate coordinate) {
		return new Coordinate(Math.abs(coordinate.x()), Math.abs(coordinate.y()));
	}
	
	public static Vector2 abs(Vector2 vector) {
		return new Vector2(Math.abs(vector.x()), Math.abs(vector.y()));
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public static float getAngleDifference(float a, float b) {
		a = wrapAngle(a);
		b = wrapAngle(b);
		
		float difference = Math.abs(a - b);
		return difference > PI ? TWO_PI - difference : difference;
	}
	
	public static Iterable<Direction> getDiagonalDirections() {
		return DIAGONAL_DIRECTIONS;
	}
	
	public static Iterable<Direction> getNonDiagonalDirections() {
		return NON_DIAGONAL_DIRECTIONS;
	}
	
	public static Random getRandom() {
		return random;
	}
	
	public static float getRotation(Direction direction) {
		return ROTATION_MAPPER.get(direction);
	}
	
	public static float getRotation(Vector2 origin, Vector2 dest) {
		Vector2 v = dest.minus(origin);
		// Invalid angle
		if (v.x() == 0 && v.y() == 0)
			return 0;
		// Note that atan2 is in polar coordinate [-pi, pi]
		return (float)Math.atan2(v.y(), v.x()) + PI_OVER_2;
	}
	
	public static Coordinate getTranslation(Direction direction) {
		return TRANSLATION_MAPPER.get(direction);
	}
	
	public static Vector2 getUnitVector(float rotation) {
		return new Vector2((float)Math.sin(rotation), (float)-Math.cos(rotation));
	}
	
	public static float interpolateAngle(float src, float dest, float delta) {
		src = wrapAngle(src);
		dest = wrapAngle(dest);
		if (Math.abs(src - dest) > MathHelper.PI)
			src += dest < src ? -MathHelper.TWO_PI : MathHelper.TWO_PI;
		if (src < dest)
			src = Math.min(src + delta, dest);
		else
			src = Math.max(src - delta, dest);

		return MathHelper.wrapAngle(src);
	}
	
	public static Direction reverse(Direction direction) {
		return REVERSE_MAPPER.get(direction);
	}

	public static int sq(int value) {
		return value * value;
	}

	public static float sq(float value) {
		return value * value;
	}
	
	public static float toRadians(float degrees) {
		return (float)Math.toRadians(degrees);
	}
	
	public static float toDegrees(float radians) {
		return (float)Math.toDegrees(radians);
	}
	
	public static float wrapAngle(float angle) {
		return ((angle % TWO_PI) + TWO_PI) % TWO_PI;
	}
	
	private MathHelper() {
	}

}
