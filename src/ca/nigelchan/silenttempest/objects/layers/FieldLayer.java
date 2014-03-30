package ca.nigelchan.silenttempest.objects.layers;

import java.util.LinkedList;
import java.util.Stack;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.batch.SpriteGroup;

import ca.nigelchan.silenttempest.data.layers.FieldLayerData;
import ca.nigelchan.silenttempest.data.layers.TileTemplate;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class FieldLayer extends Layer implements World.IListener {

	private Coordinate coordinate = new Coordinate(-100, -100);
	private FieldLayerData data;
	private LinkedList<LinkedList<TiledSprite>> tileLattice = new LinkedList<LinkedList<TiledSprite>>();
	private GameResource resource;
	private SpriteGroup spriteGroup;
	private Stack<TiledSprite> tilePool = new Stack<TiledSprite>();
	private int maxX;
	private int maxY;
	private int minX;
	private int minY;

	public FieldLayer(FieldLayerData data, GameResource resource) {
		super(data);
		this.data = data;
		this.resource = resource;

		int poolSize = computePoolSize();
		spriteGroup = resource.createFieldSpriteGroup(poolSize);
		attachChild(spriteGroup);
		for (int i = 0; i < poolSize; ++i) {
			tilePool.push(new TiledSprite(0, 0, resource
					.getFieldTextureRegion(), resource
					.getVertexBufferObjectManager()));
		}
	}

	@Override
	public void dispose() {
		for (LinkedList<TiledSprite> tileRow : tileLattice) {
			for (TiledSprite tile : tileRow) {
				if (tile != null)
				tile.dispose();
			}
		}
		while (!tilePool.isEmpty())
			tilePool.pop().dispose();
		super.dispose();
	}

	@Override
	public boolean isHidingSpot(Coordinate position) {
		TileTemplate tile = data.getTile(position.y(), position.x());
		if (tile == null)
			return false;
		return tile.isHidingSpot();
	}

	@Override
	public boolean isValidPosition(Vector2 position, Actor actor) {
		float radius = actor.getRadius();
		for (int x = (int) (position.x() - radius); x <= position.x() + radius; ++x) {
			for (int y = (int) (position.y() - radius); y <= position.y()
					+ radius; ++y) {
				TileTemplate tile = data.getTile(y, x);
				if (tile == null)
					continue;
				if (tile.isObstacle()) {
					// AABB collision detection
					Vector2 tileCenter = new Vector2(x + 0.5f, y + 0.5f);
					Vector2 dist = MathHelper.abs(position.minus(tileCenter));
					if (dist.x() < radius + 0.5f && dist.y() < radius + 0.5f)
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isWalkable(Coordinate position) {
		TileTemplate tile = data.getTile(position.y(), position.x());
		if (tile == null)
			return true;
		return !tile.isObstacle();
	}

	@Override
	public void onCameraPositionChanged(Vector2 position) {
		Coordinate newPosition = position.toCoordinate();
		Coordinate difference = newPosition.minus(coordinate);
		if (Math.abs(difference.x()) < 5 && Math.abs(difference.y()) < 5)
			render(newPosition);
		else
			renderAll(newPosition);

		coordinate = newPosition;
	}

	@Override
	public void onWorldDisposed() {
	}

	private TiledSprite allocate(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		TileTemplate template = data.getTile(y, x);
		if (template == null)
			return null;

		TiledSprite sprite = tilePool.pop();
		if (sprite == null)
			throw new RuntimeException("Insufficient tiles.");
		sprite.setCurrentTileIndex(template.getId());
		sprite.setPosition(x * resource.getFieldTileSize(),
				y * resource.getFieldTileSize());
		spriteGroup.attachChild(sprite);
		return sprite;
	}

	private int computePoolSize() {
		int maxPoolSize = 0;
		for (int r = 0; r < height; ++r) {
			for (int c = 0; c < width; ++c) {
				if (data.getTile(r, c) != null)
					maxPoolSize++;
			}
		}
		return Math.min(
				maxPoolSize,
				(resource.getScreenWidth() / resource.getFieldTileSize() + 2)
						* (resource.getScreenHeight()
								/ resource.getFieldTileSize() + 2));
	}

	private void recycle(TiledSprite tile) {
		if (tile == null)
			return;
		tile.detachSelf();
		tilePool.push(tile);
	}

	private void render(Coordinate newPosition) {
		Coordinate difference = newPosition.minus(coordinate);
		for (int x = difference.x(); x < 0; ++x) {
			minX--;
			maxX--;
			int y = minY;
			for (LinkedList<TiledSprite> tileRow : tileLattice) {
				recycle(tileRow.removeLast());
				tileRow.addFirst(allocate(minX, y++));
			}
		}
		for (int x = difference.x(); x > 0; --x) {
			minX++;
			maxX++;
			int y = minY;
			for (LinkedList<TiledSprite> tileRow : tileLattice) {
				recycle(tileRow.removeFirst());
				tileRow.addLast(allocate(maxX, y++));
			}
		}

		for (int y = difference.y(); y < 0; ++y) {
			minY--;
			maxY--;
			LinkedList<TiledSprite> tileRow = tileLattice.removeLast();
			tileLattice.addFirst(tileRow);
			int size = tileRow.size();
			for (int x = 0; x < size; ++x) {
				recycle(tileRow.removeFirst());
				tileRow.addLast(allocate(minX + x, minY));
			}
		}
		for (int y = difference.y(); y > 0; --y) {
			minY++;
			maxY++;
			LinkedList<TiledSprite> tileRow = tileLattice.removeFirst();
			tileLattice.addLast(tileRow);
			int size = tileRow.size();
			for (int x = 0; x < size; ++x) {
				recycle(tileRow.removeFirst());
				tileRow.addLast(allocate(minX + x, maxY));
			}
		}
	}

	private void renderAll(Coordinate newPosition) {
		for (LinkedList<TiledSprite> tileRow : tileLattice) {
			for (TiledSprite tile : tileRow)
				recycle(tile);
		}
		tileLattice.clear();

		int visibleWidth = resource.getScreenWidth()
				/ resource.getFieldTileSize();
		int halfWidth = (visibleWidth - 1) / 2;
		minX = newPosition.x() - halfWidth - 1;
		maxX = newPosition.x() + halfWidth + 1;

		int visibleHeight = resource.getScreenHeight()
				/ resource.getFieldTileSize();
		int halfHeight = (visibleHeight - 1) / 2;
		minY = newPosition.y() - halfHeight - 1;
		maxY = newPosition.y() + halfHeight + 1;

		for (int y = minY; y <= maxY; ++y) {
			LinkedList<TiledSprite> tileRow = new LinkedList<TiledSprite>();
			tileLattice.addLast(tileRow);
			for (int x = minX; x <= maxX; ++x)
				tileRow.addLast(allocate(x, y));
		}
	}

}
