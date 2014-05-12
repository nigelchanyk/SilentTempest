package ca.nigelchan.silenttempest.objects.actors;

import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.silenttempest.data.actors.PlayerData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.controllers.PlayerStatus;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Player extends Actor {
	
	private boolean alive = true;
	private Enemy enemyDragging = null;
	private TiledSprite sprite;
	private PlayerStatus status;
	
	public Player(PlayerData data, World world, GameResource resource) {
		super(data, world);
		sprite = new TiledSprite(0, 0, resource.getActors(), resource.getVertexBufferObjectManager());
		sprite.setCurrentTileIndex(GameResource.ACTOR_PENGUIN_INDEX);
		attachChild(sprite);
		status = new PlayerStatus(this);
		addController(status);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		sprite.dispose();
	}
	
	@Override
	public void move(Vector2 delta) {
		Vector2 newPosition = getPosition().add(delta);
		for (Enemy enemy : world.getEnemies()) {
			if (MathHelper.collided(newPosition, enemy.getPosition(), getRadius(), enemy.getRadius()) && !enemy.isKnockedOut())
				return;
		}
		super.move(delta);
	}

	// Getters
	public Enemy getEnemyDragging() {
		return enemyDragging;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isHidden() {
		return status.isHidden();
	}

	// Setters
	public void setEnemyDragging(Enemy enemyDragging) {
		this.enemyDragging = enemyDragging;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
