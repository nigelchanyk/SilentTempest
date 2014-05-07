package ca.nigelchan.silenttempest.objects.actors;

import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.silenttempest.data.actors.PlayerData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.controllers.PlayerStatus;
import ca.nigelchan.silenttempest.resources.GameResource;

public class Player extends Actor {
	
	private boolean alive = true;
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
	
	// Getters
	public boolean isAlive() {
		return alive;
	}

	public boolean isHidden() {
		return status.isHidden();
	}

	// Setters
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
