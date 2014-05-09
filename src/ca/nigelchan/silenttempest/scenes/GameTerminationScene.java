package ca.nigelchan.silenttempest.scenes;

import ca.nigelchan.silenttempest.managers.SceneManager;

public class GameTerminationScene extends ForegroundScene {
	
	public enum Mode {
		SUCCESS,
		FAILURE
	}
	
	private boolean ready = false;

	public GameTerminationScene(SceneManager manager, Mode mode, int score) {
		super(manager);
	}

	@Override
	public void onBackKeyPressed() {
	}

	@Override
	protected void createScene() {
	}

	@Override
	protected boolean isReady() {
		return ready;
	}

	@Override
	protected void resetScene() {
	}

}
