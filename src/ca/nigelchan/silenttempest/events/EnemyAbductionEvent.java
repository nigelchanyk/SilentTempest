package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.util.Coordinate;

public class EnemyAbductionEvent extends EnemyStatusMonitor {
	
	private Coordinate position;

	public EnemyAbductionEvent(World world, String enemyID, Coordinate position) {
		super(world, enemyID);
		this.position = position;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		super.onUpdate(elapsedTime);
		completed &= getEnemy().getGridPosition().equals(position);
	}

}
