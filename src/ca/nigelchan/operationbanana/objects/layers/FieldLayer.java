package ca.nigelchan.operationbanana.objects.layers;

import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.operationbanana.data.layers.FieldLayerData;
import ca.nigelchan.operationbanana.resources.GameResource;

public class FieldLayer extends Layer {

	private ArrayList<ArrayList<Entity>> entities = new ArrayList<ArrayList<Entity>>();

	public FieldLayer(FieldLayerData data, GameResource resource) {
		super(data);
		for (int r = 0; r < height; ++r) {
			ArrayList<Entity> row = new ArrayList<Entity>(width);
			for (int c = 0; c < width; ++c) {
				TiledSprite sprite = new TiledSprite(
					r * resource.getFieldTileSize(),
					c * resource.getFieldTileSize(),
					resource.getFieldTextureRegion(),
					resource.getVertexBufferObjectManager()
				);
				sprite.setCurrentTileIndex(data.getID(r, c));
				attachChild(sprite);
			}
			entities.add(row);
		}
	}
}
