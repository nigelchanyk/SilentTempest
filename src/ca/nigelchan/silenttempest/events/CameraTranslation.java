package ca.nigelchan.silenttempest.events;

import android.util.Log;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class CameraTranslation extends EventComponent {
	
	private Actor actor = null; // Nullable
	private Vector2 dest;
	private float speed;
	private World world;
	
	public CameraTranslation(World world, Actor actor, float speed) {
		this.world = world;
		this.actor = actor;
		this.speed = speed;
	}
	
	public CameraTranslation(World world, Vector2 dest, float speed) {
		this.world = world;
		this.dest = dest;
		this.speed = speed;
	}

	@Override
	public void onLoad() {
		if (actor != null)
			dest = actor.getPosition();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (world.getCameraRelativePosition().distanceSquare(dest) <= MathHelper.sq(speed * elapsedTime)) {
			world.setCameraRelativePosition(dest);
			completed = true;
			return;
		}
		Vector2 unitVector = dest.minus(world.getCameraRelativePosition()).normal();
		world.setCameraRelativePosition(world.getCameraRelativePosition().add(unitVector.multiply(speed * elapsedTime)));
	}

}
