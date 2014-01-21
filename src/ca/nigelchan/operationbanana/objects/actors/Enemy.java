package ca.nigelchan.operationbanana.objects.actors;

import org.andengine.entity.sprite.Sprite;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.objects.World;
import ca.nigelchan.operationbanana.objects.actors.controllers.EnemyCore;
import ca.nigelchan.operationbanana.resources.GameResource;

public class Enemy extends Actor {

	private EnemyCore core;
	private Sprite sprite;
	
	public Enemy(EnemyData data, World world, GameResource resource) {
		super(data, world);
		core = new EnemyCore(this, data);
		addController(core);
		sprite = new Sprite(0, 0, resource.getMonkeyBase(), resource.getVertexBufferObjectManager());
		attachChild(sprite);
	}

	@Override
	public void dispose() {
		super.dispose();
		sprite.dispose();
	}

	// Getters
	public EnemyCore getCore() {
		return core;
	}

}
