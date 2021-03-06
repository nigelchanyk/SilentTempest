package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import ca.nigelchan.silenttempest.controllers.JoystickEventInterpreter;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.GameScene;
import ca.nigelchan.silenttempest.userinterface.game.JoystickDisplay;

public class GameInterface extends Subscene {

	private JoystickEventInterpreter interpreter;
	private GameResource resource;
	private Joystick joystick;
	private JoystickDisplay joystickDisplay;
	private GameScene scene;
	
	public GameInterface(GameScene scene, GameResource resource, JoystickEventInterpreter interpreter) {
		this.scene = scene;
		this.resource = resource;
		this.interpreter = interpreter;
		this.joystick = new Joystick(resource.getDisplayMetrics());
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return joystick.onSceneTouchEvent(pScene, pSceneTouchEvent);
	}

	@Override
	protected void onActivate() {
		scene.setGamePaused(false);
	}

	@Override
	public void onBackKeyPressed() {
		switchSubscene(scene.getGameMenu());
	}

	@Override
	protected void onDeactivate() {
	}

	@Override
	protected void onLoad() {
		joystickDisplay = new JoystickDisplay(resource);
		joystick.subscribe(interpreter);
		joystick.subscribe(joystickDisplay);
		getHUD().attachChild(joystickDisplay);
	}

	@Override
	protected void onDispose() {
	}

	@Override
	protected void onUpdate(float elapsedTime) {
		joystick.onUpdate(elapsedTime);
	}
	
}
