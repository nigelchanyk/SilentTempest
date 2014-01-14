package ca.nigelchan.operationbanana.objects;


import java.util.ArrayList;

import org.andengine.entity.Entity;

import android.graphics.Point;
import ca.nigelchan.operationbanana.data.WorldData;
import ca.nigelchan.operationbanana.data.layers.ActorLayerData;
import ca.nigelchan.operationbanana.data.layers.FieldLayerData;
import ca.nigelchan.operationbanana.data.layers.ILayerDataVisitor;
import ca.nigelchan.operationbanana.data.layers.LayerData;
import ca.nigelchan.operationbanana.objects.actors.Player;
import ca.nigelchan.operationbanana.objects.layers.ActorLayer;
import ca.nigelchan.operationbanana.objects.layers.FieldLayer;
import ca.nigelchan.operationbanana.objects.layers.Layer;
import ca.nigelchan.operationbanana.resources.GameResource;
import ca.nigelchan.operationbanana.util.Vector2;

public class World extends Entity {
	
	private ActorLayer actorLayer;
	private int height;
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	private GameResource resource;
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
				actorLayer = new ActorLayer(data, World.this, World.this.resource);
				addLayer(actorLayer);
			}
		};
		
		for (LayerData layerData : data.getLayers())
			layerData.accept(visitor);

		if (actorLayer == null)
			throw new IllegalArgumentException("Missing actor layer");
	}
	
	public Vector2 convertPointToVector2(Point point) {
		float half = getUnitScale() * 0.5f;
		return new Vector2(point.x * getUnitScale() + half, point.y * getUnitScale() + half);
	}
	
	@Override
	public void dispose() {
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();
		super.dispose();
	}
	
	public boolean isValidPosition(Point position) {
		if (position.x < 0 || position.x >= width)
			return false;
		if (position.y < 0 || position.y >= height)
			return false;
		// TODO Check each layer
		return true;
	}

	// Getters
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
	
}
