package ca.nigelchan.operationbanana.managers;

import java.util.ArrayList;

import ca.nigelchan.operationbanana.objects.World;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.Enemy;
import ca.nigelchan.operationbanana.objects.actors.controllers.EnemyCore;
import ca.nigelchan.operationbanana.resources.GameResource;
import ca.nigelchan.operationbanana.userinterface.game.EnemyAlertIndicator;
import ca.nigelchan.operationbanana.userinterface.game.EnemyAlertIndicatorPool;
import ca.nigelchan.operationbanana.util.Vector2;

public class EnemyManager implements World.IListener {

	private ArrayList<EnemyListener> enemyListeners = new ArrayList<EnemyListener>();
	private EnemyAlertIndicatorPool indicatorPool;
	
	public EnemyManager(World world, GameResource resource) {
		indicatorPool = new EnemyAlertIndicatorPool(5, world, resource);
		for (Enemy enemy : world.getEnemies()) {
			enemyListeners.add(new EnemyListener(enemy));
		}
	}

	@Override
	public void onWorldDisposed() {
		indicatorPool.dispose();
	}

	private class EnemyListener implements Actor.IListener, EnemyCore.IListener {
		
		private Enemy enemy;
		private EnemyAlertIndicator alertIndicator;
		
		public EnemyListener(Enemy enemy) {
			this.enemy = enemy;
			enemy.subscribe(this);
			enemy.getCore().subscribe(this);
		}

		@Override
		public void onPositionChanged(Vector2 position) {
		}

		@Override
		public void onRotationChanged(float rotation) {
		}

		@Override
		public void onAlertLevelChanged(float alertLevel) {
			if (alertLevel > 0.1f && alertIndicator == null) {
				alertIndicator = indicatorPool.get();
				alertIndicator.follow(enemy);
			}
			else if (alertLevel == 0 && alertIndicator != null) {
				alertIndicator.unfollow();
				indicatorPool.recycle(alertIndicator);
				alertIndicator = null;
			}
		}
	}
}
