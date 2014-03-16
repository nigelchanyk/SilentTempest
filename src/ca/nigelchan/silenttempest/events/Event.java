package ca.nigelchan.silenttempest.events;

import java.util.LinkedList;

import ca.nigelchan.silenttempest.objects.World;

public class Event extends EventComponent {
	
	private boolean lock;
	private LinkedList<EventComponent> eventComponents = new LinkedList<EventComponent>();
	private World world;
	
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
	
	public void onLoad() {
		if (lock)
			world.lock();
		prepareNext();
	}
	
	public void onUpdate(float elapsedTime) {
		if (completed)
			return;
		eventComponents.peek().onUpdate(elapsedTime);
		if (eventComponents.peek().isCompleted()) {
			eventComponents.poll().dispose();
			prepareNext();
		}
	}
	
	private void prepareNext() {
		if (eventComponents.isEmpty()) {
			completed = true;
			if (lock)
				world.unlock();
		}
		else
			eventComponents.peek().onLoad();
		
	}
	
}
