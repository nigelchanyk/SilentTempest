package ca.nigelchan.silenttempest.events;

public abstract class EventComponent {
	
	protected boolean completed = false;
	
	public boolean isCompleted() {
		return completed;
	}
	
	public abstract void dispose();
	
	public abstract void onLoad();
	public abstract void onUpdate(float elapsedTime);

}
