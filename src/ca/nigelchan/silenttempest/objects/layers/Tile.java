package ca.nigelchan.silenttempest.objects.layers;

import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.silenttempest.data.layers.TileTemplate;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Coordinate;

public class Tile extends TiledSprite {
	
	private Coordinate coordinate = Coordinate.ZERO;
	private GameResource resource;
	
	public Tile(GameResource resource) {
		super(0, 0, resource.getFieldTextureRegion(), resource.getVertexBufferObjectManager());
		this.resource = resource;
	}

	// Getters
	public Coordinate getCoordinate() {
		return coordinate;
	}

	// Setters
	public void setTemplate(TileTemplate template) {
		setVisible(template != null);
		if (template != null)
			setCurrentTileIndex(template.getId());
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
		setX(coordinate.x() * resource.getFieldTileSize());
		setY(coordinate.y() * resource.getFieldTileSize());
	}

}
