package ca.nigelchan.silenttempest.userinterface.game;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Pool;

public class EnemyAlertIndicatorPool extends Pool<EnemyAlertIndicator> {
	
	private GameResource resource;
	private World world;

	public EnemyAlertIndicatorPool(int initialSize, World world, GameResource resource) {
		this.resource = resource;
		this.world = world;
		initialize(initialSize);
	}

	@Override
	protected EnemyAlertIndicator create() {
		return new EnemyAlertIndicator(world, resource);
	}

}
