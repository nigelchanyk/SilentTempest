package ca.nigelchan.silenttempest.objects.actors;

import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.silenttempest.data.actors.PlayerData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.controllers.PlayerStatus;
import ca.nigelchan.silenttempest.resources.GameResource;

public class Player extends Actor {
	
	private Sprite sprite;
	private PlayerStatus status;
	
	public Player(PlayerData data, World world, GameResource resource) {
		super(data, world);
		sprite = new Sprite(0, 0, resource.getMonkeyBase(), resource.getVertexBufferObjectManager());
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
	public boolean isHidden() {
		return status.isHidden();
	}

}
