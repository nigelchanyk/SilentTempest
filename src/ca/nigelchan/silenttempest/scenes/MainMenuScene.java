package ca.nigelchan.silenttempest.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.util.color.Color;

import ca.nigelchan.silenttempest.background.MainMenuBackground;
import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.MainMenuResource;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class MainMenuScene extends BaseScene {
	
	private CommonResource commonResource;
	private MainMenuResource resource;

	public MainMenuScene(SceneManager manager, CommonResource commonResource) {
		super(manager);
		this.commonResource = commonResource;
		resource = new MainMenuResource(activity);
		setResource(resource);
		// loadAsynchronous = true;
	}

	@Override
	public void onBackKeyPressed() {
	}

	@Override
	protected void createScene() {
		setBackground(new Background(new Color(0.2f, 0.2f, 0.2f)));
		attachChild(new MainMenuBackground(resource, commonResource));
		
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.LEFT)
			.setAnchorY(PositionHelper.AnchorY.TOP)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth(0.5f);

		attachChild(new Header("Silent Tempest", pos.getPosition(), pos.getDimension().x(), commonResource));
	}
}
