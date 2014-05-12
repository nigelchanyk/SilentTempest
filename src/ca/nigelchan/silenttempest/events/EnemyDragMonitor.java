package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.Player;

public class EnemyDragMonitor extends EventComponent {
	
	public enum DragEvent {
		START,
		END
	}
	
	private DragEvent dragEvent;
	private Enemy enemy;
	private String enemyID;
	private World world;
	
	public EnemyDragMonitor(World world, String enemyID, DragEvent dragEvent) {
		this.world = world;
		this.dragEvent = dragEvent;
		this.enemyID = enemyID;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
		enemy = world.getEnemy(enemyID);
	}

	@Override
	public void onUpdate(float elapsedTime) {
		Player player = world.getPlayer();
		if (dragEvent == DragEvent.START && player.getEnemyDragging() == enemy)
			completed = true;
		else if (dragEvent == DragEvent.END && player.getEnemyDragging() == null)
			completed = true;
	}

}
