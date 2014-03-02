package ca.nigelchan.silenttempest.controllers;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.controllers.FreeMove;
import ca.nigelchan.silenttempest.util.Vector2;

public class ActorController implements Joystick.IListener {
	
	private FreeMove move = null;

	@Override
	public void onAccept() {
		if (move == null)
			return;
		move.setEnabled(true);
	}

	@Override
	public void onMove(float rotation) {
		if (move == null)
			return;
        move.setRotation(rotation);
	}

	@Override
	public void onRelease() {
		if (move == null)
			return;
    	move.setEnabled(false);
	}

	@Override
	public void onTouch(Vector2 position) {
		// TODO Auto-generated method stub
		
	}
	
	public void setActor(Actor actor) {
		move = new FreeMove(actor);
	}

}
