package ca.nigelchan.silenttempest.data.layers;

public class TileTemplate {
	
	public enum Attribute {
		NORMAL,
		OBSTACLE,
		HIDING_SPOT
	}
	
	private Attribute attribute;
	private int id;
	
	public TileTemplate(int id, Attribute attribute) {
		this.id = id;
		this.attribute = attribute;
	}

	public int getId() {
		return id;
	}
	
	public boolean isHidingSpot() {
		return attribute == Attribute.HIDING_SPOT;
	}

	public boolean isObstacle() {
		return attribute == Attribute.OBSTACLE;
	}

}
