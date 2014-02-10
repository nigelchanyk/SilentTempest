package ca.nigelchan.silenttempest.objects.layers;

import java.util.ArrayList;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.data.actors.EnemyData;
import ca.nigelchan.silenttempest.data.layers.ActorLayerData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.Player;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Vector2;

public class ActorLayer extends Layer {
	
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private Entity enemyLayer = new Entity();
	private Player player;
	private Entity playerLayer = new Entity();

	public ActorLayer(ActorLayerData data, World world, GameResource resource) {
		super(data);
		for (EnemyData enemyData : data.getEnemies()) {
			Enemy enemy = new Enemy(enemyData, world, resource);
			enemies.add(enemy);
			enemyLayer.attachChild(enemy);
		}
		
		player = new Player(data.getPlayer(), world, resource);
		playerLayer.attachChild(player);
		
		attachChild(enemyLayer);
		attachChild(playerLayer);
	}
	
	@Override
	public void dispose() {
		for (Enemy enemy : enemies)
			enemy.dispose();
		player.dispose();
		// Parent class will dispose of anything that is directly attached
		super.dispose();
	}
	
	// Getters
	public Iterable<Enemy> getEnemies() {
		return enemies;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isHidingSpot(Coordinate position) {
		return false;
	}

	@Override
	public boolean isWalkable(Coordinate position) {
		return true;
	}

	@Override
	public boolean isValidPosition(Vector2 position, Actor actor) {
		return true;
	}

}
