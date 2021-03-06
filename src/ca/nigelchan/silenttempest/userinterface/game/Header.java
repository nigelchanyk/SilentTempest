package ca.nigelchan.silenttempest.userinterface.game;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.util.Coordinate;

public class Header extends Entity {
	
	private Sprite arrow;
	private Rectangle line;
	private Text text;

	public Header(String label, Coordinate pos, int width, CommonResource resource) {
		this.setPosition(pos.x(), pos.y());

		text = new Text(
			resource.getDPI() / 8,
			0,
			resource.getHeaderFont(),
			label,
			resource.getVertexBufferObjectManager()
		);
		float lineWidth = text.getX() + Math.max(width, text.getWidth()) + resource.getDPI() / 8;

		ITextureRegion arrowTexture = resource.getHeaderArrow();
		int lineHeight = resource.getDPI() / 25;
		// + 1 to overlap with arrow in case of floating point error
		line = new Rectangle(
			0,
			text.getHeight(),
			lineWidth + 1,
			lineHeight,
			resource.getVertexBufferObjectManager()
		);
		line.setColor(Color.WHITE);
		
		arrow = new Sprite(
			lineWidth,
			line.getY() + line.getHeight() - arrowTexture.getHeight(),
			arrowTexture,
			resource.getVertexBufferObjectManager()
		);
		
		attachChild(line);
		attachChild(arrow);
		attachChild(text);
	}

	@Override
	public void dispose() {
		line.dispose();
		arrow.dispose();
		text.dispose();
		super.dispose();
	}
	
	public float getBottomY() {
		return getY() + line.getY() + line.getHeight();
	}

}
