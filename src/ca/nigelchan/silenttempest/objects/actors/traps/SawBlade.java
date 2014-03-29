package ca.nigelchan.silenttempest.objects.actors.traps;

import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.silenttempest.data.actors.traps.SawBladeData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.MathHelper;

public class SawBlade extends Trap {
	
	public static final float ROTATION_SPEED = MathHelper.TWO_PI;
	
	private Sprite sprite;

	public SawBlade(SawBladeData data, World world, GameResource resource) {
		super(data, world, data.getDimension());
		sprite = new Sprite(0, 0, resource.getSawBlade(data.getSize()), resource.getVertexBufferObjectManager());
		attachChild(sprite);
	}

	@Override
	public boolean changeRotationOnMove() {
		return false;
	}

	@Override
	public void dispose() {
		sprite.dispose();
		super.dispose();
	}

	@Override
	protected void handleUpdate(float elapsedTime) {
		setRadianRotation((getRadianRotation() + ROTATION_SPEED * elapsedTime) % MathHelper.TWO_PI);
	}

}
