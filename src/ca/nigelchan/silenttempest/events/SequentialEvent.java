package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;

public class SequentialEvent extends Event {

	public SequentialEvent(World world, boolean lock) {
		super(world, lock);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		prepareNext();
	}

	@Override
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
		if (eventComponents.isEmpty())
			setCompleted();
		else
			eventComponents.peek().onLoad();
	}

}
