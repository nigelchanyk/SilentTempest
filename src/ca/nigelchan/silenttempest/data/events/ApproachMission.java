package ca.nigelchan.silenttempest.data.events;

import ca.nigelchan.silenttempest.events.Event;
import ca.nigelchan.silenttempest.events.EventFactory;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;
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
	public Event toEvent(World world, GameInterface gameInterface, GameResource gameResource, CommonResource commonResource) {
		return EventFactory.createForDestinationDisplay(
			instruction,
			world.getPlayer(),
			destination,
			world,
			gameInterface,
			gameResource,
			commonResource
		);
	}

}
