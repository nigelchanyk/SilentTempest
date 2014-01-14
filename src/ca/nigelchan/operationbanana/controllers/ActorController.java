package ca.nigelchan.operationbanana.controllers;

import ca.nigelchan.operationbanana.objects.actors.Actor;
import ca.nigelchan.operationbanana.objects.actors.action.Move;
import ca.nigelchan.operationbanana.util.Vector2;

public class ActorController implements Joystick.IListener {
	
	private Move move = new Move();
	
	public ActorController(Actor actor) {
		move.setActor(actor);
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
	
	// Setters
	public void setActor(Actor actor) {
		move.setActor(actor);
	}

}
