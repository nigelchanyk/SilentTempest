package ca.nigelchan.silenttempest.managers;

import java.util.ArrayList;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.userinterface.game.EnemyAlertIndicator;
import ca.nigelchan.silenttempest.userinterface.game.EnemyAlertIndicatorPool;
import ca.nigelchan.silenttempest.userinterface.game.KnockedOutIndicator;
import ca.nigelchan.silenttempest.userinterface.game.KnockedOutIndicatorPool;
import ca.nigelchan.silenttempest.util.Vector2;

public class EnemyManager implements World.IListener {

	private ArrayList<EnemyListener> enemyListeners = new ArrayList<EnemyListener>();
	private KnockedOutIndicatorPool knockedOutIndicatorPool;
	private EnemyAlertIndicatorPool alertIndicatorPool;
	
	public EnemyManager(World world, GameResource resource) {
		knockedOutIndicatorPool = new KnockedOutIndicatorPool(5, world, resource);
		alertIndicatorPool = new EnemyAlertIndicatorPool(5, world, resource);
		for (Enemy enemy : world.getEnemies()) {
			enemyListeners.add(new EnemyListener(enemy));
		}
	}

	@Override
	public void onCameraPositionChanged(Vector2 position) {
	}

	@Override
	public void onWorldDisposed() {
		knockedOutIndicatorPool.dispose();
		alertIndicatorPool.dispose();
	}

	private class EnemyListener implements Actor.IListener, EnemyCore.IListener {
		
		private Enemy enemy;
		private EnemyAlertIndicator alertIndicator;
		private KnockedOutIndicator knockedOutIndicator;
		
		public EnemyListener(Enemy enemy) {
			this.enemy = enemy;
			enemy.subscribe(this);
			enemy.getCore().subscribe(this);
		}

		@Override
		public void onKnockedOut() {
			knockedOutIndicator = knockedOutIndicatorPool.get();
			knockedOutIndicator.follow(enemy);
		}

		@Override
		public void onPositionChanged(Vector2 position) {
		}

		@Override
		public void onRecovered() {
			if (knockedOutIndicator == null)
				return;
			knockedOutIndicator.unfollow();
			knockedOutIndicatorPool.recycle(knockedOutIndicator);
			knockedOutIndicator = null;
		}

		@Override
		public void onRotationChanged(float rotation) {
		}

		@Override
		public void onAlertLevelChanged(float alertLevel) {
			if (alertLevel > 0.1f && alertIndicator == null) {
				alertIndicator = alertIndicatorPool.get();
				alertIndicator.follow(enemy);
			}
			else if (alertLevel == 0 && alertIndicator != null) {
				alertIndicator.unfollow();
				alertIndicatorPool.recycle(alertIndicator);
				alertIndicator = null;
			}
		}
	}
}
