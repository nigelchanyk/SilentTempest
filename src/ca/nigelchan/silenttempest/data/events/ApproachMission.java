package ca.nigelchan.silenttempest.data.events;

import ca.nigelchan.silenttempest.events.ConcurrentEvent;
import ca.nigelchan.silenttempest.events.DestinationBeacon;
import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.events.EventFactory;
import ca.nigelchan.silenttempest.events.EventLayer;
import ca.nigelchan.silenttempest.events.PositionSenser;
import ca.nigelchan.silenttempest.events.SequentialEvent;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Vector2;

public class ApproachMission implements IEventData {
	
	private Vector2 destination;
	private String instruction;
	private float radius;
	
	public ApproachMission(Vector2 destination, float radius, String instruction) {
		this.destination = destination;
		this.radius = radius;
		this.instruction = instruction;
	}

	@Override
	public Event toEvent(World world, EventLayer layer, GameResource gameResource, CommonResource commonResource) {
		return new ConcurrentEvent(world, false, ConcurrentEvent.CompletionRequirement.ANY)
			.addEventComponent(new DestinationBeacon(destination, world, layer, gameResource))
			.addEventComponent(
				new SequentialEvent(world, false)
					.addEventComponent(EventFactory.createForDestinationDisplay(
						instruction,
						world.getPlayer(),
						destination,
						world,
						layer,
						gameResource,
						commonResource
					))
					.addEventComponent(new PositionSenser(world.getPlayer(), destination, radius))
			);
	}

}
