package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.MainMenuResource;
import ca.nigelchan.silenttempest.scenes.MainMenuScene;
import ca.nigelchan.silenttempest.userinterface.game.Button;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class SceneSelectionMenu extends Subscene {
	
	private CommonResource commonResource;
	private Header header;
	private Entity mainButtonsContainer;
	private MainMenuResource mainMenuResource;
	private MainMenuScene scene;
	private int selection = 0;
	
	public SceneSelectionMenu(MainMenuScene scene, MainMenuResource mainMenuResource, CommonResource commonResource) {
		this.scene = scene;
		this.mainMenuResource = mainMenuResource;
		this.commonResource = commonResource;
	}

	@Override
	public void onBackKeyPressed() {
		switchSubscene(scene.getActSelectionMenu());
	}

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
		PositionHelper pos = new PositionHelper(commonResource.getScreenWidth(), commonResource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.LEFT)
			.setAnchorY(PositionHelper.AnchorY.TOP)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth(0.5f);

		header = new Header("Scene Selection", pos.getPosition(), pos.getDimension().x(), commonResource);
		getHUD().attachChild(header);

		mainButtonsContainer = new Entity();
		mainButtonsContainer.setPosition(
			pos.getPosition().x(),
			header.getBottomY() + commonResource.getDPI() / 3
		);
		getHUD().attachChild(mainButtonsContainer);
		
		float size = mainMenuResource.getSceneButtons().getWidth(0);
		float totalWidth = commonResource.getScreenWidth() * 0.9f - mainButtonsContainer.getX() - size;
		float totalHeight = commonResource.getScreenHeight() * 0.9f - mainButtonsContainer.getY() - size;
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				final int referenceSelection = y * 3 + x;
				Button button = new Button(
					mainMenuResource.getSceneButtons(),
					(y * 3 + x) * 2,
					(y * 3 + x) * 2 + 1,
					commonResource.getButtonSound(),
					commonResource.getVertexBufferObjectManager()
				) {
					
					@Override
					public void onClick() {
						selection = referenceSelection;
						scene.startGame();
					}
				};
				button.setPosition(totalWidth * x / 2, totalHeight * y / 2);
				mainButtonsContainer.attachChild(button);
				registerTouch(button);
			}
		}
	}

	@Override
	protected void onUpdate(float elapsedTime) {
	}

	// Getters
	public int getSelection() {
		return selection;
	}

}
