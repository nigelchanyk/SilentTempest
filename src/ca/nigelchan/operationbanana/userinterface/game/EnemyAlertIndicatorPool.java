package ca.nigelchan.operationbanana.userinterface.game;

import ca.nigelchan.operationbanana.objects.World;
import ca.nigelchan.operationbanana.resources.GameResource;
import ca.nigelchan.operationbanana.util.Pool;

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
