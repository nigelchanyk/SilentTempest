package ca.nigelchan.operationbanana.data.layers;

public abstract class LayerData {

	private int height;
	private int width;
	
	public LayerData(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract void accept(ILayerDataVisitor visitor);

	// Getters
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
