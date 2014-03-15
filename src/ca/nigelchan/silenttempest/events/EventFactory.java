package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;
import ca.nigelchan.silenttempest.util.Vector2;

public class EventFactory {
	
	public static final int CAMERA_TRANSLATION_SPEED = 5;
	
	public static Event createForDestinationDisplay(
		Actor source,
		Vector2 destination,
		World world,
		GameInterface gameInterface,
		GameResource gameResource
	) {
		return new Event(world, true)
			.addEventComponent(new CameraTranslation(world, destination, CAMERA_TRANSLATION_SPEED))
			.addEventComponent(new DestinationBeacon(gameInterface, gameResource))
			.addEventComponent(new CameraTranslation(world, source, CAMERA_TRANSLATION_SPEED));
	}

}
