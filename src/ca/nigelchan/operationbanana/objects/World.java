package ca.nigelchan.operationbanana.objects;


import java.util.ArrayList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;

import ca.nigelchan.operationbanana.data.WorldData;
import ca.nigelchan.operationbanana.data.layers.ActorLayerData;
import ca.nigelchan.operationbanana.data.layers.FieldLayerData;
import ca.nigelchan.operationbanana.data.layers.ILayerDataVisitor;
import ca.nigelchan.operationbanana.data.layers.LayerData;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.Enemy;
import ca.nigelchan.operationbanana.objects.actors.Player;
import ca.nigelchan.operationbanana.objects.layers.ActorLayer;
import ca.nigelchan.operationbanana.objects.layers.FieldLayer;
import ca.nigelchan.operationbanana.objects.layers.Layer;
import ca.nigelchan.operationbanana.resources.GameResource;
import ca.nigelchan.operationbanana.util.Coordinate;
import ca.nigelchan.operationbanana.util.Vector2;

public class World extends Entity {
	
	private ActorLayer actorLayer;
	private Entity alwaysVisibleLayer = new Entity();
	private Entity belowActorLayer = new Entity();
	private int height;
	private ArrayList<Layer> layers = new ArrayList<Layer>();
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
				addLayer(new FieldLayer(data, World.this.resource));
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
	}
	
	public void attachAlwaysVisibleChild(IEntity pEntity) {
		alwaysVisibleLayer.attachChild(pEntity);
	}
	
	public void attachBelowActorChild(IEntity pEntity) {
		belowActorLayer.attachChild(pEntity);
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
	
	public boolean isHidingSpot(Coordinate position) {
		for (Layer layer : layers) {
			if (layer.isHidingSpot(position))
				return true;
		}
		return false;
	}
	
	public boolean isValidPath(Vector2 src, Vector2 dest) {
		float x0 = src.x(), x1 = dest.x(), y0 = src.y(), y1 = dest.y();
        float dx = Math.abs(x1 - x0);
        float dy = Math.abs(y1 - y0);
        
        if (dx == 0 && dy == 0)
        	return true;
        
        int x = (int)Math.floor(src.x());
        int y = (int)Math.floor(src.y());
        int n = 1;
        int xInc = 0, yInc = 0;
        float error = 0;

        if (dx == 0)
            error = Float.POSITIVE_INFINITY;
        else if (x1 > x0) {
            xInc = 1;
            n += (int)Math.floor(x1) - x;
            error = (float)(Math.floor(x0) + 1 - x0) * dy;
        }
        else {
            xInc = -1;
            n += x - (int)Math.floor(x1);
            error = (float)(x0 - Math.floor(x0)) * dy;
        }

        if (dy == 0)
            error = Float.NEGATIVE_INFINITY;
        else if (y1 > y0) {
            yInc = 1;
            n += (int)Math.floor(y1) - y;
            error -= (float)(Math.floor(y0) + 1 - y0) * dx;
        }
        else {
            yInc = -1;
            n += y - (int)Math.floor(y1);
            error -= (float)(y0 - Math.floor(y0)) * dx;
        }

        for (; n > 0; --n) {
        	Coordinate coordinate = new Coordinate(x, y);
        	for (Layer layer : layers) {
        		if (!layer.isWalkable(coordinate))
        			return false;
        	}
            if (error > 0) {
                y += yInc;
                error -= dx;
            }
            else {
                x += xInc;
                error += dy;
            }
        }
        
        return true;
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
		if (position.x() < 0 || position.x() >= width)
			return false;
		if (position.y() < 0 || position.y() >= height)
			return false;
		for (Layer layer : layers) {
			if (!layer.isWalkable(position))
				return false;
		}
		return true;
	}
	
	public void subscribe(IListener subscriber) {
		if (subscribers.contains(subscriber))
			return;
		subscribers.add(subscriber);
	}
	
	public void unsubscribe(IListener subscriber) {
		subscribers.remove(subscriber);
	}

	// Getters
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
	
	
	public static interface IListener {
		
		public void onWorldDisposed();
		
	}
}
