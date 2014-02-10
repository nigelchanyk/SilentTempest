package ca.nigelchan.silenttempest.controllers;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.controllers.FreeMove;
import ca.nigelchan.silenttempest.util.Vector2;

public class ActorController implements Joystick.IListener {
	
	private FreeMove move;
	
	public ActorController(Actor actor) {
		move = new FreeMove(actor);
	}

	@Override
	public void onAccept() {
		move.setEnabled(true);
	}

	@Override
	public void onMove(float rotation) {
        move.setRotation(rotation);
	}

	@Override
	public void onRelease() {
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
