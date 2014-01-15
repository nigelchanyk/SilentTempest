package ca.nigelchan.operationbanana.objects.layers;

import org.andengine.entity.Entity;

import ca.nigelchan.operationbanana.data.layers.LayerData;
import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.util.Vector2;

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
	
	public abstract boolean isValidPosition(Vector2 position, Actor actor);

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
