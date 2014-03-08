package ca.nigelchan.silenttempest.scenes.subscenes;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.userinterface.game.DialogBox;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class GameMenu extends Subscene {
	
	private CommonResource resource;
	private DialogBox dialog;
	
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
		// TODO Auto-generated method stub
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.CENTER)
			.setAnchorY(PositionHelper.AnchorY.BOTTOM)
			.setWidth(resource.getDPI() * 3)
			.setHeight(resource.getDPI());
		dialog = new DialogBox(resource, pos);
		getHUD().attachChild(dialog);
	}

}
