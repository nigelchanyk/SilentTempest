package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import ca.nigelchan.silenttempest.controllers.ActorController;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.userinterface.game.JoystickDisplay;

public class GameInterface extends Subscene {

	private ActorController controller;
	private GameResource resource;
	private Joystick joystick;
	private JoystickDisplay joystickDisplay;
	
	public GameInterface(GameResource resource, ActorController controller) {
		this.resource = resource;
		this.controller = controller;
		this.joystick = new Joystick(resource.getDisplayMetrics());
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return joystick.onSceneTouchEvent(pScene, pSceneTouchEvent);
	}

	@Override
	protected void onActivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDeactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onLoad() {
		joystickDisplay = new JoystickDisplay(resource);
		joystick.subscribe(controller);
		joystick.subscribe(joystickDisplay);
		getHUD().attachChild(joystickDisplay);
	}

	@Override
	protected void onDispose() {
		// TODO Auto-generated method stub
	}

}
