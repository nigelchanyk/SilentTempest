package ca.nigelchan.silenttempest.controllers;

import ca.nigelchan.silenttempest.objects.actors.Player;
import ca.nigelchan.silenttempest.objects.actors.controllers.PlayerController;
import ca.nigelchan.silenttempest.util.Vector2;

public class JoystickEventInterpreter implements Joystick.IListener {
	
	private PlayerController controller = null;

	@Override
	public void onAccept() {
		if (controller == null)
			return;
		controller.setEnabled(true);
	}

	@Override
	public void onMove(float rotation) {
		if (controller == null)
			return;
		controller.setRotation(rotation);
	}

	@Override
	public void onRelease() {
		if (controller == null)
			return;
		controller.setEnabled(false);
	}

	@Override
	public void onTouch(Vector2 position) {
	}
	
	public void setPlayer(Player player) {
		controller = new PlayerController(player);
	}

	@Override
	public void onDoubleTap() {
		if (controller == null)
			return;
		controller.interact();
	}

}
