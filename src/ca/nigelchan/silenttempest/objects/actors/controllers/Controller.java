package ca.nigelchan.silenttempest.objects.actors.controllers;

import ca.nigelchan.silenttempest.objects.actors.Actor;

public abstract class Controller {

	protected Actor actor;
	
	private boolean enabled = false;
	private boolean permitted = true;
	
	public Controller(Actor actor) {
		this.actor = actor;
		actor.addController(this);
	}
	
	public void update(float elapsedTime) {
		if (isActive())
			onUpdate(elapsedTime);
	}

	protected void onStateChanged() {
	}
	
	protected abstract void onUpdate(float elapsedTime);

	// Getters
	public boolean isActive() {
		return actor != null && enabled && permitted;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		boolean previous = isActive();
		this.enabled = enabled;

		if (previous != isActive())
			onStateChanged();
	}
	
	public void setPermitted(boolean permitted) {
		boolean previous = isActive();
		this.permitted = permitted;
		
		if (previous != isActive())
			onStateChanged();
	}
}
