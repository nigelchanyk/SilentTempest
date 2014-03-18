package ca.nigelchan.silenttempest.userinterface.game;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Button extends Entity implements ITouchableInterfaceObject {
	
	private int activeIndex;
	private int index;
	private Sound sound;
	private TiledSprite sprite;

	public Button(ITiledTextureRegion texture, int index, int activeIndex, final Sound sound, VertexBufferObjectManager vbom) {
		this.index = index;
		this.activeIndex = activeIndex;
		this.sound = sound;

		sprite = new TiledSprite(0, 0, texture, vbom);
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
			sound.play();
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
