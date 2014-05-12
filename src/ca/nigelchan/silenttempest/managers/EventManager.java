package ca.nigelchan.silenttempest.managers;

import org.andengine.engine.handler.IUpdateHandler;

import ca.nigelchan.silenttempest.data.EventsData;
import ca.nigelchan.silenttempest.data.events.IEventData;
import ca.nigelchan.silenttempest.events.ConcurrentEvent;
import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.events.EventFactory;
import ca.nigelchan.silenttempest.events.EventLayer;
import ca.nigelchan.silenttempest.events.SequentialEvent;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.GameScene;

public class EventManager implements IUpdateHandler {
	
	private Event globalEvent;
	
	public EventManager(
		GameScene scene,
		EventsData eventsData,
		World world,
		EventLayer eventLayer,
		GameResource gameResource,
		CommonResource commonResource
	) {
		globalEvent = new ConcurrentEvent(world, false, ConcurrentEvent.CompletionRequirement.ANY);
		SequentialEvent mapDefinedEvent = new SequentialEvent(world, false);
		for (IEventData eventData : eventsData.getAllEventData())
			mapDefinedEvent.addEventComponent(eventData.toEvent(world, eventLayer, gameResource, commonResource));
		mapDefinedEvent.addEventComponent(EventFactory.createForGameCompleted(scene, world, eventLayer));

		globalEvent.addEventComponent(mapDefinedEvent);
		globalEvent.addEventComponent(EventFactory.createForGameOver(scene, world, eventLayer));
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
