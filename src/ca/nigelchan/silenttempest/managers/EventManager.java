package ca.nigelchan.silenttempest.managers;

import org.andengine.engine.handler.IUpdateHandler;

import ca.nigelchan.silenttempest.data.EventsData;
import ca.nigelchan.silenttempest.data.events.IEventData;
import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.events.EventLayer;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;

public class EventManager implements IUpdateHandler {
	
	private Event globalEvent;
	
	public EventManager(
		EventsData eventsData,
		World world,
		EventLayer eventLayer,
		GameResource gameResource,
		CommonResource commonResource
	) {
		globalEvent = new Event(world, false);
		for (IEventData eventData : eventsData.getAllEventData()) {
			globalEvent.addEventComponent(eventData.toEvent(world, eventLayer, gameResource, commonResource));
		}
		globalEvent.onLoad();
	}
	
	public void dispose() {
		globalEvent.dispose();
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		globalEvent.onUpdate(pSecondsElapsed);
	}

	@Override
	public void reset() {
	}
	
	public boolean isCompleted() {
		return globalEvent.isCompleted();
	}

}
