package ca.nigelchan.silenttempest.data.events;

import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.events.EventLayer;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;

public interface IEventData {
	
	public Event toEvent(World world, EventLayer eventLayer, GameResource gameResource, CommonResource commonResource);

}
