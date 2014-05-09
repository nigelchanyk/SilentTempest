package ca.nigelchan.silenttempest.scenes;

import ca.nigelchan.silenttempest.managers.SceneManager;

/**
 * 
 * ForegroundScene allows background runners to execute
 * while the foreground scene is displayed.
 *
 */
public abstract class ForegroundScene extends BaseScene {

	private volatile boolean abortSignal = false;

	public ForegroundScene(SceneManager manager) {
		super(manager);
	}

	public void abortWhenReady() {
		abortSignal = true;
		popIfReady();
	}
	
	protected synchronized void popIfReady() {
		if (!abortSignal || !isReady())
			return;
		abortSignal = false;
		resetScene();
		manager.popScene();
	}
	
	protected abstract void resetScene();
	protected abstract boolean isReady();

}
