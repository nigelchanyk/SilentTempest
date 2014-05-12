package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Enemy;

public abstract class EnemyMonitor extends EventComponent {
	
	private Enemy enemy;
	private String enemyID;
	private World world;
	
	public EnemyMonitor(World world, String enemyID) {
		this.world = world;
		this.enemyID = enemyID;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
		enemy = world.getEnemy(enemyID);
	}
	
	protected Enemy getEnemy() {
		return enemy;
	}

}
