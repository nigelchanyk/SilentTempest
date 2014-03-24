package ca.nigelchan.silenttempest.scenes.subscenes;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.userinterface.game.Button;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.PositionHelper;

public abstract class MainMenu extends Subscene {

	private CommonResource resource;
	private Header header;
	
	public MainMenu(CommonResource resource) {
		this.resource = resource;
	}
	
	public abstract void onPlay();

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
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.LEFT)
			.setAnchorY(PositionHelper.AnchorY.TOP)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth(0.5f);

		header = new Header("Silent Tempest", pos.getPosition(), pos.getDimension().x(), resource);
		getHUD().attachChild(header);
		createPlayButton();
	}

	@Override
	protected void onUpdate(float elapsedTime) {
	}
	
	private void createPlayButton() {
		Button button = new Button(
            resource.getButtons(),
            CommonResource.BUTTON_PLAY,
            CommonResource.BUTTON_PLAY_ACTIVE,
            resource.getButtonSound(),
            resource.getVertexBufferObjectManager()
        ) {
			@Override
			public void onClick() {
				onPlay();
			}
		};
		
		button.setPosition(header.getX(), header.getBottomY() + resource.getDPI() / 3);
		getHUD().attachChild(button);
		registerTouch(button);
	}

}
