package ca.nigelchan.silenttempest.objects.layers;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.data.layers.LayerData;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Layer extends Entity {
	
	protected int height;
	protected int width;

	public Layer(LayerData data) {
		this.width = data.getWidth();
		this.height = data.getHeight();
	}
	
	@Override
	public void dispose() {
		for (int i = 0; i < getChildCount(); ++i) {
			getChildByIndex(i).dispose();
		}
		super.dispose();
	}
	
	public abstract boolean isHidingSpot(Coordinate position);
	public abstract boolean isValidPosition(Vector2 position, Actor actor);
	public abstract boolean isWalkable(Coordinate position);

	// Getters
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	// Setters
	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
