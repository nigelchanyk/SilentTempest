package ca.nigelchan.silenttempest.events;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class DestinationBeacon extends EventComponent {
	
	private Sprite beacon;
	private int count = 0;
	private Entity entity = new Entity();
	private EventLayer eventLayer;
	private float phase = 0;
	private Vector2 position;
	private int totalCount;
	private World world;
	
	public DestinationBeacon(Vector2 position, World world, EventLayer eventLayer, GameResource resource) {
		this(position, world, eventLayer, resource, -1);
	}

	public DestinationBeacon(Vector2 position, World world, EventLayer eventLayer, GameResource resource, int totalCount) {
		this.eventLayer = eventLayer;
		this.position = position;
		this.world = world;
		this.totalCount = totalCount;
		beacon = new Sprite(0, 0, resource.getBeacon(), resource.getVertexBufferObjectManager());
		beacon.setScale(0);
		beacon.setColor(Color.RED);
		entity.attachChild(beacon);
	}

	@Override
	public void dispose() {
		entity.detachSelf();
		beacon.dispose();
		entity.dispose();
	}

	@Override
	public void onLoad() {
		eventLayer.attachChild(entity);
		updatePosition();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		updatePosition();
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
				if (totalCount != -1 && count == totalCount)
					completed = true;
			}
		}
	}
	
	private void updatePosition() {
		Vector2 screenPos = world.toCameraRelativePixelPosition(position);
		entity.setPosition(screenPos.x() - beacon.getWidth() / 2, screenPos.y() - beacon.getHeight() / 2);
	}

}
