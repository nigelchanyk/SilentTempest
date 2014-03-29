package ca.nigelchan.silenttempest.objects;

import java.util.ArrayList;
import java.util.LinkedList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.data.layers.ActorLayerData;
import ca.nigelchan.silenttempest.data.layers.FieldLayerData;
import ca.nigelchan.silenttempest.data.layers.ILayerDataVisitor;
import ca.nigelchan.silenttempest.data.layers.LayerData;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.Enemy;
import ca.nigelchan.silenttempest.objects.actors.Player;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Move;
import ca.nigelchan.silenttempest.objects.layers.ActorLayer;
import ca.nigelchan.silenttempest.objects.layers.FieldLayer;
import ca.nigelchan.silenttempest.objects.layers.Layer;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class World extends Entity {
	
	private ActorLayer actorLayer;
	private Entity alwaysVisibleLayer = new Entity();
	private Entity belowActorLayer = new Entity();
	private Vector2 cameraRelativePosition = Vector2.ZERO;
	private int height;
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	private int locks = 0;
	private GameResource resource;
	private ArrayList<IListener> subscribers = new ArrayList<IListener>();
	private int width;

	public World(WorldData data, GameResource resource) {
		this.width = data.getWidth();
		this.height = data.getHeight();
		this.resource = resource;
		
		ILayerDataVisitor visitor = new ILayerDataVisitor() {
			
			public void addLayer(Layer layer) {
				layers.add(layer);
				attachChild(layer);
			}
			
			@Override
			public void visit(FieldLayerData data) {
				FieldLayer layer = new FieldLayer(data, World.this.resource);
				addLayer(layer);
				subscribe(layer);
			}
			
			@Override
			public void visit(ActorLayerData data) {
				attachChild(belowActorLayer);
				actorLayer = new ActorLayer(data, World.this, World.this.resource);
				addLayer(actorLayer);
			}
		};
		
		for (LayerData layerData : data.getLayers())
			layerData.accept(visitor);

		if (actorLayer == null)
			throw new IllegalArgumentException("Missing actor layer");
		
		// Must be last child
		attachChild(alwaysVisibleLayer);
		
		registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				chasePlayer();
			}
		});
		chasePlayer();
	}
	
	public void attachAlwaysVisibleChild(IEntity pEntity) {
		alwaysVisibleLayer.attachChild(pEntity);
	}
	
	public void attachBelowActorChild(IEntity pEntity) {
		belowActorLayer.attachChild(pEntity);
	}
	
	public void chasePlayer() {
		Player player = actorLayer.getPlayer();
		if (player == null)
			return;
		// Round to nearest integer to avoid gaps in between grid
		setCameraRelativePosition(player.getPosition());
	}
	
	@Override
	public void dispose() {
		for (IListener subscriber : subscribers)
			subscriber.onWorldDisposed();
		for (int i = 0; i < alwaysVisibleLayer.getChildCount(); ++i)
			alwaysVisibleLayer.getChildByIndex(i).dispose();
		for (int i = 0; i < belowActorLayer.getChildCount(); ++i)
			belowActorLayer.getChildByIndex(i).dispose();
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();
		super.dispose();
	}
	
	public Iterable<Move> findPath(Actor actor, Coordinate dest) {
		if (actor.getGridPosition().equals(dest)) {
			return new ArrayList<Move>();
		}
		Direction[][] directions = new Direction[height][width];
		LinkedList<Coordinate> queue = new LinkedList<Coordinate>();
		Coordinate start = actor.getGridPosition();
		queue.add(start);
        Coordinate next = null;
		while (!queue.isEmpty()) {
			Coordinate current = queue.poll();
			for (Direction direction : MathHelper.getNonDiagonalDirections()) {
				Coordinate translation = MathHelper.getTranslation(direction);
				next = current.add(translation);
				if (!isWalkable(next)) {
					continue;
				}
				if (directions[next.y()][next.x()] != null)
					continue;
				directions[next.y()][next.x()] = direction;
				if (next.equals(dest)) {
					current = next;
                    LinkedList<Move> path = new LinkedList<Move>();
					while (!current.equals(start)) {
						direction = directions[current.y()][current.x()];
						next = current.add(MathHelper.getTranslation(MathHelper.reverse(direction)));
						path.addFirst(new Move(actor, next.toCenterVector2(), direction));
						current = next;
					}
					return path;
				}
				queue.addLast(next);
			}
		}
		
		return null;
	}
	
	public Coordinate findBlockingTile(Vector2 src, float rotation) {
		Vector2 dest = MathHelper.getUnitVector(rotation).add(src);
		return getBlockingTile(src, dest, Integer.MAX_VALUE);
	}
	
	public boolean isHidingSpot(Coordinate position) {
		for (Layer layer : layers) {
			if (layer.isHidingSpot(position))
				return true;
		}
		return false;
	}
	
	public boolean isOutOfBound(Coordinate position) {
		if (position.x() < 0 || position.x() >= width)
			return true;
		if (position.y() < 0 || position.y() >= height)
			return true;
		return false;
	}
	
	public boolean isValidPath(Vector2 src, Vector2 dest) {
		return getBlockingTile(src, dest) == null;
	}
	
	public boolean isValidPosition(Vector2 position, Actor actor) {
		float radius = actor.getRadius();
		if (position.x() - radius < 0 || position.x() + radius >= width)
			return false;
		if (position.y() - radius < 0 || position.y() + radius >= height)
			return false;
		// TODO Check each layer
		for (Layer layer : layers) {
			if (!layer.isValidPosition(position, actor))
				return false;
		}
		return true;
	}
	
	public boolean isWalkable(Coordinate position) {
		if (isOutOfBound(position))
			return false;
		for (Layer layer : layers) {
			if (!layer.isWalkable(position))
				return false;
		}
		return true;
	}
	
	public void lock() {
		locks++;
		setIgnoreUpdate(true);
	}
	
	public void subscribe(IListener subscriber) {
		if (subscribers.contains(subscriber))
			return;
		subscribers.add(subscriber);
	}
	
	public void unlock() {
		if (locks == 0)
			return;
		locks--;
		if (locks == 0) {
			setIgnoreUpdate(false);
		}
	}
	
	public void unsubscribe(IListener subscriber) {
		subscribers.remove(subscriber);
	}
	
	private Coordinate getBlockingTile(Vector2 src, Vector2 dest) {
        int x = (int)Math.floor(src.x());
        int y = (int)Math.floor(src.y());
        int n = 1;
		float x0 = src.x(), x1 = dest.x(), y0 = src.y(), y1 = dest.y();
        if (x1 > x0) {
            n += (int)Math.floor(x1) - x;
        }
        else {
            n += x - (int)Math.floor(x1);
        }

        if (y1 > y0) {
            n += (int)Math.floor(y1) - y;
        }
        else {
            n += y - (int)Math.floor(y1);
        }
        
        return getBlockingTile(src, dest, n);
	}
	
	private Coordinate getBlockingTile(Vector2 src, Vector2 dest, int n) {
		float x0 = src.x(), x1 = dest.x(), y0 = src.y(), y1 = dest.y();
        float dx = Math.abs(x1 - x0);
        float dy = Math.abs(y1 - y0);
        
        if (dx == 0 && dy == 0)
        	return null;
        
        int x = (int)Math.floor(src.x());
        int y = (int)Math.floor(src.y());
        int xInc = 0, yInc = 0;
        float error = 0;

        if (dx == 0)
            error = Float.POSITIVE_INFINITY;
        else if (x1 > x0) {
            xInc = 1;
            error = (float)(Math.floor(x0) + 1 - x0) * dy;
        }
        else {
            xInc = -1;
            error = (float)(x0 - Math.floor(x0)) * dy;
        }

        if (dy == 0)
            error = Float.NEGATIVE_INFINITY;
        else if (y1 > y0) {
            yInc = 1;
            error -= (float)(Math.floor(y0) + 1 - y0) * dx;
        }
        else {
            yInc = -1;
            error -= (float)(y0 - Math.floor(y0)) * dx;
        }

        for (; n > 0; --n) {
        	Coordinate coordinate = new Coordinate(x, y);
        	if (!isWalkable(coordinate))
        		return coordinate;
            if (error > 0) {
                y += yInc;
                error -= dx;
            }
            else {
                x += xInc;
                error += dy;
            }
        }
        
        return null;
	}

	// Getters
	public Vector2 getCameraRelativePosition() {
		return cameraRelativePosition;
	}

	public Iterable<Enemy> getEnemies() {
		return actorLayer.getEnemies();
	}
	
	public int getHeight() {
		return height;
	}
	
	public Player getPlayer() {
		return actorLayer.getPlayer();
	}
	
	public int getUnitScale() {
		return resource.getFieldTileSize();
	}

	public int getWidth() {
		return width;
	}
	
	// Setters
	public void setCameraRelativePosition(Vector2 position) {
		cameraRelativePosition = position;
		setPosition(
			resource.getScreenWidth() / 2 - (int)(position.x() * resource.getFieldTileSize()),
			resource.getScreenHeight() / 2 - (int)(position.y() * resource.getFieldTileSize())
		);
		for (IListener subscriber : subscribers)
			subscriber.onCameraPositionChanged(position);
	}
	
	
	public static interface IListener {
		
		public void onCameraPositionChanged(Vector2 position);
		public void onWorldDisposed();
		
	}
}
