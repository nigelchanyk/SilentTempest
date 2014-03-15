package ca.nigelchan.silenttempest.data.events;

import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;

public interface IEventData {
	
	public Event toEvent(World world, GameInterface gameInterface, GameResource gameResource);

}
