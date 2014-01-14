package ca.nigelchan.operationbanana.data.layers;

public class FieldLayerData extends LayerData {

	private int[][] grid;
	
	public FieldLayerData(int width, int height) {
		super(width, height);
		grid = new int[width][height];
	}

	@Override
	public void accept(ILayerDataVisitor visitor) {
		visitor.visit(this);
	}

	// Getters
	public int getID(int row, int column) {
		return grid[row][column];
	}

	// Setters
	public void setID(int row, int column, int id) {
		grid[row][column] = id;
	}
}
