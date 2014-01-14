package ca.nigelchan.operationbanana.objects.actors.action;

import ca.nigelchan.operationbanana.objects.actors.Actor;

public abstract class Action {

	protected Actor actor;
	
	private boolean enabled = false;
	private boolean permitted = true;
	
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
	
	// Setters
	public void setActor(Actor actor) {
		if (this.actor != null) {
			this.actor.removeAction(this);
		}
		this.actor = actor;
		actor.addAction(this);
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
