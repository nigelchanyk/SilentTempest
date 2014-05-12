package ca.nigelchan.silenttempest.data.events;

import ca.nigelchan.silenttempest.events.ConcurrentEvent;
import ca.nigelchan.silenttempest.events.DestinationBeacon;
import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.events.EventLayer;
import ca.nigelchan.silenttempest.events.ModalRemover;
import ca.nigelchan.silenttempest.events.ModalSetter;
import ca.nigelchan.silenttempest.events.PositionSenser;
import ca.nigelchan.silenttempest.events.SequentialEvent;
import ca.nigelchan.silenttempest.events.WaitEvent;
import ca.nigelchan.silenttempest.events.ConcurrentEvent.CompletionRequirement;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Coordinate;

public class MoveTutorial implements IEventData {
	
	private Coordinate destination;
	
	public MoveTutorial(Coordinate destination) {
		this.destination = destination;
	}

	public Event toEvent(World world, EventLayer eventLayer, GameResource gameResource, CommonResource commonResource) {
		return new SequentialEvent(world, false)
			.addEventComponent(new ModalSetter("Swipe and hold to move to the beacon.", eventLayer, commonResource))
			.addEventComponent(
				new ConcurrentEvent(world, false, CompletionRequirement.ANY)
					.addEventComponent(new PositionSenser(world.getPlayer(), destination.toCenterVector2(), 1))
					.addEventComponent(new DestinationBeacon(destination.toCenterVector2(), world, eventLayer, gameResource))
			)
			.addEventComponent(
				new SequentialEvent(world, true)
					.addEventComponent(new ModalSetter("Well done!", eventLayer, commonResource))
					.addEventComponent(new WaitEvent(3))
					.addEventComponent(new ModalRemover(eventLayer))
			);
	}

}
