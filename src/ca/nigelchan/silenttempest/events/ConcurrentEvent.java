package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;

public class ConcurrentEvent extends Event {
	
	public enum CompletionRequirement {
		ALL,
		ANY
	}
	
	private CompletionRequirement requirement;

	public ConcurrentEvent(World world, boolean lock, CompletionRequirement requirement) {
		super(world, lock);
		this.requirement = requirement;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		for (EventComponent component : eventComponents)
			component.onLoad();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		int completionCount = 0;
		for (EventComponent component : eventComponents) {
			if (component.isCompleted())
				continue;
			component.onUpdate(elapsedTime);
			if (component.isCompleted())
				completionCount++;
		}
		if (requirement == CompletionRequirement.ALL && completionCount == eventComponents.size())
			setCompleted();
		else if (requirement == CompletionRequirement.ANY && completionCount > 0)
			setCompleted();
	}

}
