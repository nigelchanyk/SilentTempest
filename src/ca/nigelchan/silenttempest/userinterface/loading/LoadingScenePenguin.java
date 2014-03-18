package ca.nigelchan.silenttempest.userinterface.loading;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.TiledSprite;

import ca.nigelchan.silenttempest.resources.LoadingResource;
import ca.nigelchan.silenttempest.userinterface.game.ITouchableInterfaceObject;
import ca.nigelchan.silenttempest.userinterface.game.UserInterfaceTouchEvent;
import ca.nigelchan.silenttempest.util.Vector2;

public class LoadingScenePenguin extends Entity implements ITouchableInterfaceObject {
	
	private TiledSprite sprite;
	
	public LoadingScenePenguin(LoadingResource resource) {
		sprite = new TiledSprite(0, 0, resource.getPenguin(), resource.getVertexBufferObjectManager());
		attachChild(sprite);
	}

	@Override
	public void dispose() {
		sprite.dispose();
		super.dispose();
	}

	@Override
	public boolean onTouchEvent(UserInterfaceTouchEvent event) {
		sprite.setCurrentTileIndex(1);
		return true;
	}
	
	public void reset() {
		sprite.setCurrentTileIndex(0);
	}
	
	// Getters
	@Override
	public Vector2 getDimension() {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		
		for (Vector2 corner : getCorners()) {
			minX = Math.min(corner.x(), minX);
			maxX = Math.max(corner.x(), maxX);
			minY = Math.min(corner.y(), minY);
			maxY = Math.max(corner.y(), maxY);
		}
		return new Vector2(maxX - minX, maxY - minY);
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	@Override
	public Vector2 getWorldPosition() {
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		for (Vector2 corner : getCorners()) {
			minX = Math.min(corner.x(), minX);
			minY = Math.min(corner.y(), minY);
		}
		return new Vector2(minX, minY);
	}
	
	private Vector2[] getCorners() {
		return new Vector2[] {
			Vector2.fromFloats(convertLocalToSceneCoordinates(0, 0)),
			Vector2.fromFloats(convertLocalToSceneCoordinates(getWidth(), 0)),
			Vector2.fromFloats(convertLocalToSceneCoordinates(0, getHeight())),
			Vector2.fromFloats(convertLocalToSceneCoordinates(getWidth(), getHeight()))
		};
		
	}

}
