package ca.nigelchan.silenttempest.userinterface.game;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class UiShadow extends Entity {
	
	private Entity window;
	
	public UiShadow(CommonResource resource, PositionHelper positionHelper) {
		ITiledTextureRegion texture = resource.getUiShadow();
		int corner = (int)texture.getWidth(0);
		positionHelper.setMinWidth(corner * 2 + 1);
		positionHelper.setMinHeight(corner * 2 + 1);
		
		Coordinate position = positionHelper.getPosition();
		setPosition(position.x(), position.y());

		window = new Entity();
		Coordinate dimension = positionHelper.getDimension();
		VertexBufferObjectManager vbom = resource.getVertexBufferObjectManager();
		
		int[] posX = {0, corner, dimension.x() - corner};
		int[] posY = {0, corner, dimension.y() - corner};
		int[] width = {corner, dimension.x() - corner * 2, corner};
		int[] height = {corner, dimension.y() - corner * 2, corner};
		
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				TiledSprite sprite = new TiledSprite(posX[x], posY[y], width[x], height[y], texture, vbom);
				sprite.setCurrentTileIndex(y * 3 + x);
				sprite.setColor(0, 0, 0, 0.8f);
				window.attachChild(sprite);
			}
		}
		attachChild(window);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < window.getChildCount(); ++i)
			window.getChildByIndex(i).dispose();
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();

		super.dispose();
	}

}
