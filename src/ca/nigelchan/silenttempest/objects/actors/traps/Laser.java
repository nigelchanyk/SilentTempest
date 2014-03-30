package ca.nigelchan.silenttempest.objects.actors.traps;

import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.silenttempest.data.actors.traps.LaserData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Laser extends Trap {
	
	public static final Vector2 CANNON_DIMENSION = new Vector2(0.5f, 1);
	
	private float rotationSpeed;
	private Sprite laserBeam;
	private Sprite laserBeamCap;
	private Sprite laserCannon;
	
	public Laser(LaserData data, World world, GameResource resource) {
		super(data, world, CANNON_DIMENSION);
		this.rotationSpeed = data.getRotationSpeed();
		laserBeam = new Sprite(0, 0, resource.getLaserBeam(), resource.getVertexBufferObjectManager());
		laserBeam.setScaleCenter(0, 0);
		laserBeam.setScaleY(0);
		laserBeam.setY(resource.getLaserCannon().getHeight() / 2);
		attachChild(laserBeam);

		laserBeamCap = new Sprite(0, 0, resource.getLaserBeamCap(), resource.getVertexBufferObjectManager());
		attachChild(laserBeamCap);

		laserCannon = new Sprite(0, 0, resource.getLaserCannon(), resource.getVertexBufferObjectManager());
		attachChild(laserCannon);
	}

	@Override
	public void dispose() {
		laserBeam.dispose();
		laserBeamCap.dispose();
		laserCannon.dispose();
		super.dispose();
	}

	@Override
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	@Override
	protected void handleUpdate(float elapsedTime) {
		laserBeam.setScaleY(-2 * getLaserDistance());
		laserBeamCap.setY(laserBeam.getHeightScaled() - laserBeamCap.getHeight() + laserBeam.getY());
	}
	
	private float getLaserDistance() {
		Coordinate blockingTile = world.findBlockingTile(getPosition(), getRadianRotation());
		Vector2 position = getPosition();
		Vector2 unitVector = MathHelper.getUnitVector(getRadianRotation());
		float distance = 0;
		if (unitVector.x() != 0) {
			float x = blockingTile.x();
			if (unitVector.x() < 0)
				x++;
			float scalar = (x - position.x()) / unitVector.x();
			float y = position.y() + scalar * unitVector.y();
			if (y >= blockingTile.y() && y <= blockingTile.y() + 1)
				distance = scalar;
		}
		if (unitVector.y() != 0) {
			float y = blockingTile.y();
			if (unitVector.y() < 0)
				y++;
			float scalar = (y - position.y()) / unitVector.y();
			float x = position.x() + scalar * unitVector.x();
			if (x >= blockingTile.x() && x <= blockingTile.x() + 1)
				distance = scalar;
		}
		
		return distance;
	}

}
