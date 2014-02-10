package ca.nigelchan.silenttempest.objects.layers;

import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.silenttempest.data.layers.TileTemplate;
import ca.nigelchan.silenttempest.resources.GameResource;

public class Tile extends TiledSprite {
	
	private TileTemplate template;
	
	public Tile(float x, float y, GameResource resource, TileTemplate template) {
		super(x, y, resource.getFieldTextureRegion(), resource.getVertexBufferObjectManager());
		this.template = template;
		setCurrentTileIndex(template.getId());
	}

	// Getters
	public boolean isHidingSpot() {
		return template.isHidingSpot();
	}

	public boolean isObstacle() {
		return template.isObstacle();
	}
}
