package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.scenes.GameScene;
import ca.nigelchan.silenttempest.scenes.GameTerminationScene;
import ca.nigelchan.silenttempest.util.Vector2;

public class EventFactory {
	
	public static final int CAMERA_TRANSLATION_SPEED = 5;
	public static final int FADE_DURATION = 3;
	
	public static Event createForDestinationDisplay(
		String instruction,
		Actor source,
		Vector2 destination,
		World world,
		EventLayer layer,
		GameResource gameResource,
		CommonResource commonResource
	) {
		return new SequentialEvent(world, true)
			.addEventComponent(new ModalSetter(instruction, layer, commonResource))
			.addEventComponent(new CameraTranslation(world, destination, CAMERA_TRANSLATION_SPEED))
			.addEventComponent(new WaitEvent(4))
			.addEventComponent(new CameraTranslation(world, source, CAMERA_TRANSLATION_SPEED))
			.addEventComponent(new ModalRemover(layer));
	}
	
	public static Event createForGameCompleted(GameScene game, World world, EventLayer layer) {
		return new SequentialEvent(world, true)
			.addEventComponent(new FadeEvent(layer, 1, 0, FADE_DURATION))
			.addEventComponent(new GameTerminationEvent(game, GameTerminationScene.Mode.SUCCESS));
	}
	
	public static Event createForGameOver(GameScene game, World world, EventLayer layer) {
		return new SequentialEvent(world, false)
			.addEventComponent(new PlayerHealthMonitor(world.getPlayer()))
			.addEventComponent(
				new SequentialEvent(world, true)
					.addEventComponent(new FadeEvent(layer, 1, 0, FADE_DURATION))
					.addEventComponent(new GameTerminationEvent(game, GameTerminationScene.Mode.FAILURE))
			);
	}

}
