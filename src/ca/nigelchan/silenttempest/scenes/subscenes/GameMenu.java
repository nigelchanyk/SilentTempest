package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.userinterface.game.Button;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.PositionHelper;

public abstract class GameMenu extends Subscene {
	
	private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0.9f);
	
	private Entity mainButtonsContainer;
	private CommonResource resource;
	
	public GameMenu(CommonResource resource) {
		this.resource = resource;
	}
	
	public abstract void onContinue();
	public abstract void onExit();
	public abstract void onRestart();

	@Override
	protected void onActivate() {
	}

	@Override
	protected void onDeactivate() {
	}

	@Override
	protected void onDispose() {
		for (int i = 0; i < mainButtonsContainer.getChildCount(); ++i)
			mainButtonsContainer.getChildByIndex(i).dispose();
	}

	@Override
	protected void onLoad() {
		createBackground();

		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.CENTER)
			.setAnchorY(PositionHelper.AnchorY.CENTER)
			.setWidth(0.8f)
			.setHeight(0.8f);

		Header header = new Header("Paused", pos.getPosition(), pos.getDimension().x(), resource);
		getHUD().attachChild(header);
		
		mainButtonsContainer = new Entity();
		mainButtonsContainer.setPosition(
			pos.getPosition().x(),
			Math.max(
				(resource.getScreenHeight() - resource.getButtons().getHeight()) / 2,
				header.getButtonY() + resource.getDPI() / 3
			)
		);
		
		getHUD().attachChild(mainButtonsContainer);
		createContinueButton(0);
		createRestartButton((pos.getDimension().x() - resource.getButtons().getWidth()) / 2);
		createExitButton(pos.getDimension().x() - resource.getButtons().getWidth());
	}

	@Override
	protected void onUpdate(float elapsedTime) {
	}

	private void createBackground() {
		Rectangle background = new Rectangle(
			0,
			0,
			resource.getScreenWidth(),
			resource.getScreenHeight(),
			resource.getVertexBufferObjectManager()
		);
		background.setColor(BACKGROUND_COLOR);
		getHUD().attachChild(background);
	}
	
	private void createContinueButton(float x) {
		Button button = new Button(
            resource.getButtons(),
            CommonResource.BUTTON_PLAY,
            CommonResource.BUTTON_PLAY_ACTIVE,
            resource.getButtonSound(),
            resource.getVertexBufferObjectManager()
        ) {
			@Override
			public void onClick() {
				onContinue();
			}
		};
		button.setPosition(x, 0);
		mainButtonsContainer.attachChild(button);
		registerTouch(button);
	}
	
	private void createExitButton(float x) {
		Button button = new Button(
            resource.getButtons(),
            CommonResource.BUTTON_EXIT,
            CommonResource.BUTTON_EXIT_ACTIVE,
            resource.getButtonSound(),
            resource.getVertexBufferObjectManager()
        ) {
			@Override
			public void onClick() {
			}
		};
		button.setPosition(x, 0);
		mainButtonsContainer.attachChild(button);
		registerTouch(button);
	}
	
	private void createRestartButton(float x) {
		Button button = new Button(
            resource.getButtons(),
            CommonResource.BUTTON_RESTART,
            CommonResource.BUTTON_RESTART_ACTIVE,
            resource.getButtonSound(),
            resource.getVertexBufferObjectManager()
        ) {
			@Override
			public void onClick() {
			}
		};
		button.setPosition(x, 0);
		mainButtonsContainer.attachChild(button);
		registerTouch(button);
	}

}
