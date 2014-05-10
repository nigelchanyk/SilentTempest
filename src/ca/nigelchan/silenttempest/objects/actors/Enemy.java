package ca.nigelchan.silenttempest.objects.actors;

import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.silenttempest.data.actors.EnemyData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.controllers.EnemyCore;
import ca.nigelchan.silenttempest.resources.GameResource;

public class Enemy extends Actor {

	private EnemyCore core;
	private TiledSprite sprite;
	
	public Enemy(EnemyData data, World world, GameResource resource) {
		super(data, world);
		core = new EnemyCore(this, data);
		addController(core);
		sprite = new TiledSprite(0, 0, resource.getActors(), resource.getVertexBufferObjectManager());
		sprite.setCurrentTileIndex(GameResource.ACTOR_REGULAR_MONKEY_INDEX);
		attachChild(sprite);
	}

	@Override
	public void dispose() {
		super.dispose();
		sprite.dispose();
	}

	// Getters
	public float getAlertLevel() {
		return core.getAlertLevel();
	}

	public EnemyCore getCore() {
		return core;
	}
	
}
