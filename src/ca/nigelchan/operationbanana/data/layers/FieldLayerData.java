package ca.nigelchan.operationbanana.data.layers;

public class FieldLayerData extends LayerData {

	private TileTemplate[][] grid;
	
	public FieldLayerData(int width, int height) {
		super(width, height);
		grid = new TileTemplate[width][height];
	}

	@Override
	public void accept(ILayerDataVisitor visitor) {
		visitor.visit(this);
	}

	// Getters
	public TileTemplate getTile(int row, int column) {
		return grid[row][column];
	}

	// Setters
	public void setTile(int row, int column, TileTemplate template) {
		grid[row][column] = template;
	}
	
}
