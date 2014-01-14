package ca.nigelchan.operationbanana.objects.layers;

import org.andengine.entity.Entity;

import ca.nigelchan.operationbanana.data.layers.LayerData;

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
