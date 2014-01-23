package ca.nigelchan.operationbanana.objects.actors.controllers;

import ca.nigelchan.operationbanana.objects.actors.Player;

public class PlayerStatus extends Controller {

	private boolean hidden;
	
	public PlayerStatus(Player player) {
		super(player);
		setEnabled(true);
		hidden = actor.getWorld().isHidingSpot(actor.getGridPosition());
	}

	// Getters
	public boolean isHidden() {
		return hidden;
	}

	@Override
	protected void onUpdate(float elapsedTime) {
		hidden = actor.getWorld().isHidingSpot(actor.getGridPosition());
	}
	
}
