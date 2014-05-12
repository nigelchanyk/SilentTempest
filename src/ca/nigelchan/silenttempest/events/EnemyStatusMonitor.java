package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;

public class EnemyStatusMonitor extends EnemyMonitor {

	public EnemyStatusMonitor(World world, String enemyID) {
		super(world, enemyID);
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (getEnemy().isKnockedOut())
			completed = true;
	}

}
