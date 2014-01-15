package ca.nigelchan.operationbanana.data.layers;

public class TileTemplate {
	
	private int id;
	private boolean obstacle;
	
	public TileTemplate(int id, boolean obstacle) {
		this.id = id;
		this.obstacle = obstacle;
	}

	public int getId() {
		return id;
	}

	public boolean isObstacle() {
		return obstacle;
	}

}
