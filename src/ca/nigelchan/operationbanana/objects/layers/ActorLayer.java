package ca.nigelchan.operationbanana.objects.layers;

import java.util.ArrayList;

import org.andengine.entity.Entity;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.data.layers.ActorLayerData;
import ca.nigelchan.operationbanana.objects.World;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.Enemy;
import ca.nigelchan.operationbanana.objects.actors.Player;
import ca.nigelchan.operationbanana.resources.GameResource;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.Vector2;

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
