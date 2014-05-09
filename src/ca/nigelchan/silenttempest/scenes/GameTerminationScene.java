package ca.nigelchan.silenttempest.scenes;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameTerminationResource;
import ca.nigelchan.silenttempest.userinterface.game.Header;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.PositionHelper;

public class GameTerminationScene extends ForegroundScene {
	
	public enum Mode {
		SUCCESS,
		FAILURE
	}
	
	private CommonResource commonResource;
	private boolean ready = false;
	private GameTerminationResource resource;
	private Mode mode;

	public GameTerminationScene(SceneManager manager, CommonResource commonResource, Mode mode) {
		super(manager);
		resource = new GameTerminationResource(activity, mode);
		setResource(resource);
		this.commonResource = commonResource;
		this.mode = mode;
	}

	@Override
	public void onBackKeyPressed() {
		ready = true;
		popIfReady();
	}

	@Override
	protected void createScene() {
		PositionHelper pos = new PositionHelper(resource.getScreenWidth(), resource.getScreenHeight())
			.setAnchorX(PositionHelper.AnchorX.RIGHT)
			.setAnchorY(PositionHelper.AnchorY.BOTTOM)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth((int)resource.getPenguin().getWidth())
			.setHeight((int)resource.getPenguin().getHeight());
		Coordinate position = pos.getPosition();
		attachChild(new Sprite(position.x(), position.y(), resource.getPenguin(), resource.getVertexBufferObjectManager()));
		
		pos.setAnchorX(PositionHelper.AnchorX.LEFT)
			.setAnchorY(PositionHelper.AnchorY.TOP)
			.setMarginX(0.1f)
			.setMarginY(0.1f)
			.setWidth(0.6f);

		attachChild(
			new Header(
				mode == Mode.SUCCESS ? "Mission Success" : "Mission Failed",
				pos.getPosition(),
				pos.getDimension().x(),
				commonResource
			)
		);
		
		setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				ready = true;
				popIfReady();
				return true;
			}
		});
	}

	@Override
	protected boolean isReady() {
		return ready;
	}

	@Override
	protected void resetScene() {
	}

}
