package ca.nigelchan.silenttempest.userinterface.game;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Pool;

public class KnockedOutIndicatorPool extends Pool<KnockedOutIndicator> {
	
	private GameResource resource;
	private World world;
	
	public KnockedOutIndicatorPool(int initialSize, World world, GameResource resource) {
		this.world = world;
		this.resource = resource;
		initialize(initialSize);
	}

	@Override
	protected KnockedOutIndicator create() {
		return new KnockedOutIndicator(world, resource);
	}

}
