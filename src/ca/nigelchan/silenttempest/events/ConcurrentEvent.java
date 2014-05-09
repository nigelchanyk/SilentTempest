package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;

public class ConcurrentEvent extends Event {

	public ConcurrentEvent(World world, boolean lock) {
		super(world, lock);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		for (EventComponent component : eventComponents)
			component.onLoad();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		boolean allCompleted = true;
		for (EventComponent component : eventComponents) {
			if (component.isCompleted())
				continue;
			component.onUpdate(elapsedTime);
			allCompleted &= component.isCompleted();
		}
		if (allCompleted)
			setCompleted();
	}

}
