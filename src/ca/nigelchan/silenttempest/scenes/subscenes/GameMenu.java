package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class GameMenu extends Subscene {
	
	private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0.9f);
	
	private Rectangle background;
	private Header header;
	private CommonResource resource;
	
	public GameMenu(CommonResource resource) {
		this.resource = resource;
	}

	@Override
	protected void onActivate() {
	}

	@Override
	protected void onDeactivate() {
	}

	@Override
	protected void onDispose() {
	}

	@Override
	protected void onLoad() {
		background = new Rectangle(0, 0, resource.getScreenWidth(), resource.getScreenHeight(), resource.getVertexBufferObjectManager());
		background.setColor(BACKGROUND_COLOR);
		getHUD().attachChild(background);

		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.CENTER)
			.setAnchorY(PositionHelper.AnchorY.CENTER)
			.setWidth(0.8f)
			.setHeight(0.8f);

		header = new Header("Paused", pos.getPosition(), pos.getDimension().x(), resource);
		getHUD().attachChild(header);
	}

}
