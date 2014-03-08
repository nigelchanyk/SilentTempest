package ca.nigelchan.silenttempest.util;

public class PositionHelper {
	
	public enum AnchorX {
		LEFT,
		CENTER,
		RIGHT
	}
	
	public enum AnchorY {
		TOP,
		CENTER,
		BOTTOM
	}
	
	private AnchorX anchorX = AnchorX.CENTER;
	private AnchorY anchorY = AnchorY.CENTER;
	private int height;
	private int marginX;
	private int marginY;
	private int maxHeight;
	private int maxWidth;
	private int minHeight;
	private int minWidth;
	private int width;
	
	public PositionHelper(int maxWidth, int maxHeight) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}

	public AnchorX getAnchorX() {
		return anchorX;
	}

	public AnchorY getAnchorY() {
		return anchorY;
	}
	
	public Coordinate getDimension() {
		return new Coordinate(width, height);
	}
	
	public Coordinate getMargin() {
		return new Coordinate(marginX, marginY);
	}
	
	public Coordinate getMinDimension() {
		return new Coordinate(minWidth, minHeight);
	}
	
	public Coordinate getPosition() {
		int x = 0;
		switch (anchorX) {
		case LEFT:
			x = marginX;
			break;
		case CENTER:
			x = (maxWidth - width) / 2;
			break;
		case RIGHT:
			x = maxWidth - width - marginX;
			break;
		}

		int y = 0;
		switch (anchorY) {
		case TOP:
			y = marginY;
			break;
		case CENTER:
			y = (maxHeight - height) / 2;
			break;
		case BOTTOM:
			y = maxHeight - height - marginY;
			break;
		}
		
		return new Coordinate(x, y);
	}

	public PositionHelper setAnchorX(AnchorX anchorX) {
		this.anchorX = anchorX;
		return this;
	}

	public PositionHelper setAnchorY(AnchorY anchorY) {
		this.anchorY = anchorY;
		return this;
	}
	
	public PositionHelper setHeight(float percent) {
		height = (int)(maxHeight * percent);
		limit();
		return this;
	}
	
	public PositionHelper setHeight(int value) {
		height = value;
		limit();
		return this;
	}
	
	public PositionHelper setMarginX(float xPercent) {
		marginX = (int)(maxWidth * xPercent);
		limit();
		return this;
	}
	
	public PositionHelper setMarginX(int value) {
		marginX = value;
		limit();
		return this;
	}
	
	public PositionHelper setMarginY(float yPercent) {
		marginY = (int)(maxHeight * yPercent);
		limit();
		return this;
	}
	
	public PositionHelper setMarginY(int value) {
		marginY = value;
		limit();
		return this;
	}
	
	public PositionHelper setMinHeight(int value) {
		minHeight = value;
		limit();
		return this;
	}
	
	public PositionHelper setMinWidth(int value) {
		minWidth = value;
		limit();
		return this;
	}
	
	public PositionHelper setWidth(float percent) {
		width = (int)(maxWidth * percent);
		limit();
		return this;
	}
	
	public PositionHelper setWidth(int value) {
		width = value;
		limit();
		return this;
	}
	
	private void limit() {
		width = Math.max(minWidth, Math.min(maxWidth, width));
		height = Math.max(minHeight, Math.min(maxHeight, height));
		marginX = Math.min(maxWidth - width, marginX);
		marginY = Math.min(maxHeight - height, marginY);
	}

}