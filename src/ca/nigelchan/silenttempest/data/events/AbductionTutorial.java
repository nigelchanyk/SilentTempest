package ca.nigelchan.silenttempest.data.events;

import ca.nigelchan.silenttempest.events.ConcurrentEvent;
import ca.nigelchan.silenttempest.events.DestinationBeacon;
import ca.nigelchan.silenttempest.events.EnemyDragMonitor;
import ca.nigelchan.silenttempest.events.EnemyStatusMonitor;
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

public class AbductionTutorial implements IEventData {
	
	private Coordinate approachPosition;
	private Coordinate destination;
	private String enemyID;
	
	public AbductionTutorial(String enemyID, Coordinate approachPosition, Coordinate destination) {
		this.enemyID = enemyID;
		this.approachPosition = approachPosition;
		this.destination = destination;
	}

	public Event toEvent(World world, EventLayer eventLayer, GameResource gameResource, CommonResource commonResource) {
		return new SequentialEvent(world, false)
			.addEventComponent(new ModalSetter("Approach the enemy from behind.", eventLayer, commonResource))
			.addEventComponent(
				new ConcurrentEvent(world, false, CompletionRequirement.ANY)
					.addEventComponent(new PositionSenser(world.getPlayer(), approachPosition.toCenterVector2(), 1))
					.addEventComponent(new DestinationBeacon(approachPosition.toCenterVector2(), world, eventLayer, gameResource))
			)
			.addEventComponent(new ModalSetter("Face the enemy from behind and double tap.", eventLayer, commonResource))
			.addEventComponent(new EnemyStatusMonitor(world, enemyID))
			.addEventComponent(
				new SequentialEvent(world, true)
					.addEventComponent(new ModalSetter("Enemy is now temporary knocked out.", eventLayer, commonResource))
					.addEventComponent(new WaitEvent(3))
					.addEventComponent(new ModalRemover(eventLayer))
			)
			.addEventComponent(new ModalSetter("Double tap to start dragging the body.", eventLayer, commonResource))
			.addEventComponent(new EnemyDragMonitor(world, enemyID, EnemyDragMonitor.DragEvent.START))
			.addEventComponent(new ModalSetter("Move the body to the beacon.", eventLayer, commonResource))
			.addEventComponent(
				new ConcurrentEvent(world, false, CompletionRequirement.ANY)
					.addEventComponent(new PositionSenser(world.getPlayer(), destination.toCenterVector2(), 1))
					.addEventComponent(new DestinationBeacon(destination.toCenterVector2(), world, eventLayer, gameResource))
			)
			.addEventComponent(new ModalSetter("Double tap to release the body.", eventLayer, commonResource))
			.addEventComponent(new EnemyDragMonitor(world, enemyID, EnemyDragMonitor.DragEvent.END))
			.addEventComponent(
				new SequentialEvent(world, true)
					.addEventComponent(new ModalSetter("Well done!", eventLayer, commonResource))
					.addEventComponent(new WaitEvent(3))
					.addEventComponent(new ModalRemover(eventLayer))
			);
	}

}
