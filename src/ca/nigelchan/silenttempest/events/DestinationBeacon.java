package ca.nigelchan.silenttempest.events;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;
import ca.nigelchan.silenttempest.util.MathHelper;

public class DestinationBeacon extends EventComponent {
	
	public static final int BEACON_COUNT = 3;
	
	private Sprite beacon;
	private int count = 0;
	private Entity entity = new Entity();
	private GameInterface gameInterface;
	private float phase = 0;
	
	public DestinationBeacon(GameInterface gameInterface, GameResource resource) {
		this.gameInterface = gameInterface;
		beacon = new Sprite(0, 0, resource.getBeacon(), resource.getVertexBufferObjectManager());
		beacon.setScale(0);
		beacon.setColor(Color.RED);
		entity.attachChild(beacon);
		entity.setPosition(
			(resource.getScreenWidth() - beacon.getWidth()) / 2,
			(resource.getScreenHeight() - beacon.getHeight() / 2) / 2
		);
	}

	@Override
	public void dispose() {
		entity.detachSelf();
		beacon.dispose();
		entity.dispose();
	}

	@Override
	public void onLoad() {
		this.gameInterface.getHUD().attachChild(entity);
	}

	@Override
	public void onUpdate(float elapsedTime) {
		phase += elapsedTime;
		if (phase > 1) {
			float scale = MathHelper.clamp(phase - 1, 0, 1);
			beacon.setScale(scale);
			beacon.setAlpha(1 - scale * scale);
			beacon.setPosition(0, 0);
			if (phase >= 2) {
				phase = 0;
				beacon.setScale(0);
				beacon.setAlpha(0);
				beacon.setPosition(0, 0);
				count++;
				if (count == 3)
					completed = true;
			}
		}
	}

}
