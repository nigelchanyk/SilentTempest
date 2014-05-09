package ca.nigelchan.silenttempest.events;

import java.util.LinkedList;

import ca.nigelchan.silenttempest.objects.World;

public abstract class Event extends EventComponent {
	
	protected LinkedList<EventComponent> eventComponents = new LinkedList<EventComponent>();
	protected World world;

	private boolean lock;
	
	public Event(World world, boolean lock) {
		this.world = world;
		this.lock = lock;
	}
	
	@Override
	public void dispose() {
		for (EventComponent component : eventComponents)
			component.dispose();
	}

	public Event addEventComponent(EventComponent component) {
		eventComponents.addLast(component);
		return this;
	}
	
	@Override
	public void onLoad() {
		if (lock)
			world.lock();
	}
	
	public abstract void onUpdate(float elapsedTime);
	
	protected void setCompleted() {
		completed = true;
		if (lock)
			world.unlock();
	}
	
}
