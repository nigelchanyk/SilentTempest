package ca.nigelchan.silenttempest.userinterface.game;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Button extends Entity implements ITouchableInterfaceObject {
	
	private int activeIndex;
	private int index;
	private TiledSprite sprite;

	public Button(ITiledTextureRegion texture, int index, int activeIndex, VertexBufferObjectManager vbom) {
		this.index = index;
		this.activeIndex = activeIndex;

		sprite = new TiledSprite(0, 0, texture, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					setCurrentTileIndex(Button.this.activeIndex);
				}
				else if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					setCurrentTileIndex(Button.this.index);
					onClick();
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		sprite.setCurrentTileIndex(index);
		attachChild(sprite);
	}

	@Override
	public void dispose() {
		sprite.dispose();
		super.dispose();
	}
	
	public abstract void onClick();
	
	// Getters
	public ITouchArea getTouchArea() {
		return sprite;
	}

	@Override
	public Vector2 getWorldPosition() {
		float[] coord = getSceneCenterCoordinates();
		return new Vector2(coord[0], coord[1]);
	}

	@Override
	public Vector2 getDimension() {
		// TODO Auto-generated method stub
		return new Vector2(sprite.getWidth(), sprite.getHeight());
	}

	@Override
	public boolean onTouchEvent(UserInterfaceTouchEvent event) {
        if (event == UserInterfaceTouchEvent.DOWN) {
            sprite.setCurrentTileIndex(Button.this.activeIndex);
        }
        else if (event == UserInterfaceTouchEvent.UP) {
            sprite.setCurrentTileIndex(Button.this.index);
            onClick();
        }
        else if (event == UserInterfaceTouchEvent.LEAVE) {
            sprite.setCurrentTileIndex(Button.this.index);
        }
		return false;
	}

}
