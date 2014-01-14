package ca.nigelchan.operationbanana.objects.actors;

import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.operationbanana.data.actors.PlayerData;
import ca.nigelchan.operationbanana.objects.World;
import ca.nigelchan.operationbanana.resources.GameResource;

public class Player extends Actor {
	
	private Sprite sprite;
	
	public Player(PlayerData data, World world, GameResource resource) {
		super(data, world);
		sprite = new Sprite(0, 0, resource.getMonkeyBase(), resource.getVertexBufferObjectManager());
		attachChild(sprite);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		sprite.dispose();
	}

}
