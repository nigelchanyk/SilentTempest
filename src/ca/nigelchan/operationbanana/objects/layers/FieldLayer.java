package ca.nigelchan.operationbanana.objects.layers;

import java.util.ArrayList;

import ca.nigelchan.operationbanana.data.layers.FieldLayerData;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.resources.GameResource;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.MathHelper;
import ca.nigelchan.operationbanana.util.Vector2;

public class FieldLayer extends Layer {

	private ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();

	public FieldLayer(FieldLayerData data, GameResource resource) {
		super(data);
		for (int r = 0; r < height; ++r) {
			ArrayList<Tile> row = new ArrayList<Tile>(width);
			for (int c = 0; c < width; ++c) {
				if (data.getTile(r, c) == null) {
					row.add(null);
					continue;
				}
					
				Tile tile = new Tile(
					c * resource.getFieldTileSize(),
					r * resource.getFieldTileSize(),
					resource,
					data.getTile(r, c)
				);
				attachChild(tile);
				row.add(tile);
			}
			tiles.add(row);
		}
	}

	@Override
	public boolean isValidPosition(Vector2 position, Actor actor) {
		float radius = actor.getRadius();
		float radiusSq = MathHelper.sq(radius);
		for (int x = (int)(position.x() - radius); x <= position.x() + radius; ++x) {
			for (int y = (int)(position.y() - radius); y <= position.y() + radius; ++y) {
				Tile tile = tiles.get(y).get(x);
				if (tile == null)
					continue;
				if (tile.isObstacle()) {
					// Square-circle collision detection
					Vector2 closest = new Vector2(
						MathHelper.clamp(position.x(), x, x + 1),
						MathHelper.clamp(position.y(), y, y + 1)
					);
					if (closest.distanceSquare(position) < radiusSq)
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isWalkable(Coordinate position) {
		return !tiles.get(position.y()).get(position.x()).isObstacle();
	}
	
}
