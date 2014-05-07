package ca.nigelchan.silenttempest.data.actors.traps;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.traps.SawBlade;
import ca.nigelchan.silenttempest.objects.actors.traps.Trap;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Vector2;

public class SawBladeData extends TrapData {
	
	public enum Size {
		SMALL,
		MEDIUM,
		LARGE
	}
	
	public static int diameter(Size size) {
		switch (size) {
		case SMALL:
			return 2;
		case MEDIUM:
			return 3;
		default:
			return 4;
		}
	}

	private Vector2 dimension;
	private Size size;

	public SawBladeData(Vector2 initPosition, float speed, Size size) {
		super(initPosition, 0, speed);
		this.size = size;
		this.dimension = new Vector2(diameter(size), diameter(size));
	}

	@Override
	public Trap createTrap(World world, GameResource resource) {
		return new SawBlade(this, world, resource);
	}

	// Getters
	public Vector2 getDimension() {
		return dimension;
	}
	
	public float getRadius() {
		return diameter(size) / 2.0f;
	}

	public Size getSize() {
		return size;
	}

}
