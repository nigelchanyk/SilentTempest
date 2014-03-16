package ca.nigelchan.silenttempest.modals;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.userinterface.game.UiShadow;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class Modal extends Entity {
	
	private UiShadow shadow;
	private Text text;
	
	public Modal(CommonResource resource, String label) {
		text = new Text(0, 0, resource.getRegularFont(), label, resource.getVertexBufferObjectManager());
		text.setColor(Color.WHITE);
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.LEFT)
			.setAnchorY(PositionHelper.AnchorY.BOTTOM)
			.setWidth((int)text.getWidth() + resource.getDPI() / 2)
			.setHeight((int)text.getHeight() + resource.getDPI() / 2);
		shadow = new UiShadow(resource, pos);
		attachChild(shadow);
		shadow.attachChild(text);
		text.setPosition(
			(pos.getDimension().x() - text.getWidth()) / 2,
			(pos.getDimension().y() - text.getHeight()) / 2
		);
	}

	@Override
	public void dispose() {
		for (int i = 0; i < getChildCount(); ++i)
			getChildByIndex(i).dispose();
		super.dispose();
	}
	

}
