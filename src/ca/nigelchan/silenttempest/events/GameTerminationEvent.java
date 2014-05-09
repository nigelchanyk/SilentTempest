package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.scenes.GameScene;
import ca.nigelchan.silenttempest.scenes.GameTerminationScene;

public class GameTerminationEvent extends EventComponent {
	
	private GameScene scene;
	private GameTerminationScene.Mode mode;
	
	public GameTerminationEvent(GameScene scene, GameTerminationScene.Mode mode) {
		this.scene = scene;
		this.mode = mode;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
		scene.terminateGame(mode);
		completed = true;
	}

	@Override
	public void onUpdate(float elapsedTime) {
	}

}
