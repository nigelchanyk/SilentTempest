package ca.nigelchan.operationbanana.data;

import java.util.ArrayList;

import ca.nigelchan.operationbanana.data.layers.LayerData;

public class WorldData {
	
	private int height;
	private ArrayList<LayerData> layers = new ArrayList<LayerData>();
	private int width;

	public WorldData(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void addLayer(LayerData layer) {
		if (layer.getWidth() != width || layer.getHeight() != height)
			throw new IllegalArgumentException("Width or height of layer does not match the world.");
		layers.add(layer);
	}

	public int getHeight() {
		return height;
	}
	
	public Iterable<LayerData> getLayers() {
		return layers;
	}

	public int getWidth() {
		return width;
	}
}
