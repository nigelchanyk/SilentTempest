package ca.nigelchan.operationbanana.util;

import java.util.EnumMap;

public final class MathHelper {

	public static final float PI = (float)Math.PI;
	public static final float PI_OVER_2 = (float)Math.PI * 0.5f;
	public static final float PI_OVER_4 = (float)Math.PI * 0.25f;
	public static final float PI_OVER_8 = (float)Math.PI * 0.125f;
	public static final float THREE_PI_OVER_2 = (float)Math.PI * 1.5f;
	public static final float THREE_PI_OVER_4 = (float)Math.PI * 0.75f;
	private static final EnumMap<Direction, Float> ROTATION_MAPPER = new EnumMap<Direction, Float>(Direction.class);
	private static final EnumMap<Direction, Coordinate> TRANSLATION_MAPPER = new EnumMap<Direction, Coordinate>(Direction.class);
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
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
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
