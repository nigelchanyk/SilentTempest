package ca.nigelchan.silenttempest.data.actors.traps;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.traps.Laser;
import ca.nigelchan.silenttempest.objects.actors.traps.Trap;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class LaserData extends TrapData {
	
	private float rotationSpeed;

	public LaserData(Vector2 initPosition, Direction direction, float speed, float rotationSpeed) {
		super(initPosition, MathHelper.getRotation(direction), speed);
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	public Trap createTrap(World world, GameResource resource) {
		return new Laser(this, world, resource);
	}

	// Getters
	public float getRotationSpeed() {
		return rotationSpeed;
	}

}
