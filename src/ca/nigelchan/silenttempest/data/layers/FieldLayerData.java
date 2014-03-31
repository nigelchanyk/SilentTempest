package ca.nigelchan.silenttempest.data.layers;

public class FieldLayerData extends LayerData {

	private TileTemplate[][] grid;
	private TileTemplate defaultTile;
	
	public FieldLayerData(int width, int height, TileTemplate defaultTile) {
		super(width, height);
		grid = new TileTemplate[width][height];
		this.defaultTile = defaultTile;
	}

	@Override
	public void accept(ILayerDataVisitor visitor) {
		visitor.visit(this);
	}

	// Getters
	public TileTemplate getDefaultTile() {
		return defaultTile;
	}

	public TileTemplate getTile(int row, int column) {
		if (column < 0 || column >= getWidth() || row < 0 || row >= getHeight())
			return defaultTile;
		return grid[column][row];
	}

	// Setters
	public void setTile(int row, int column, TileTemplate template) {
		grid[column][row] = template;
	}
	
}
