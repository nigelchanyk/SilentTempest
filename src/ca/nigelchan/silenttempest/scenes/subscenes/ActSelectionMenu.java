package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.MainMenuResource;
import ca.nigelchan.silenttempest.scenes.MainMenuScene;
import ca.nigelchan.silenttempest.userinterface.game.Button;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class ActSelectionMenu extends Subscene {
	
	private CommonResource commonResource;
	private Header header;
	private Entity mainButtonsContainer;
	private MainMenuResource mainMenuResource;
	private MainMenuScene scene;
	
	public ActSelectionMenu(MainMenuScene scene, MainMenuResource mainMenuResource, CommonResource commonResource) {
		this.scene = scene;
		this.mainMenuResource = mainMenuResource;
		this.commonResource = commonResource;
	}

	@Override
	protected void onActivate() {
	}

	@Override
	public void onBackKeyPressed() {
		switchSubscene(scene.getMainMenu());
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
		PositionHelper pos = new PositionHelper(commonResource.getScreenWidth(), commonResource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.LEFT)
			.setAnchorY(PositionHelper.AnchorY.TOP)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth(0.5f);

		header = new Header("Act Selection", pos.getPosition(), pos.getDimension().x(), commonResource);
		getHUD().attachChild(header);

		mainButtonsContainer = new Entity();
		mainButtonsContainer.setPosition(
			pos.getPosition().x(),
			Math.max(
				(commonResource.getScreenHeight() - mainMenuResource.getActButtons().getHeight(0)) / 2,
				header.getBottomY() + commonResource.getDPI() / 3
			)
		);
		getHUD().attachChild(mainButtonsContainer);
		
		float size = mainMenuResource.getActButtons().getWidth(0);
		float totalWidth = pos.getPosition().x() + commonResource.getScreenWidth() * 0.8f - size;
		for (int i = 0; i < 4; ++i) {
			Button button = new Button(
				mainMenuResource.getActButtons(),
				i * 2,
				i * 2 + 1,
				commonResource.getButtonSound(),
				commonResource.getVertexBufferObjectManager()
			) {
				
				@Override
				public void onClick() {
				}
			};
			button.setPosition(totalWidth * i / 4 + size / 2, 0);
			mainButtonsContainer.attachChild(button);
			registerTouch(button);
		}
	}

	@Override
	protected void onUpdate(float elapsedTime) {
	}

}
