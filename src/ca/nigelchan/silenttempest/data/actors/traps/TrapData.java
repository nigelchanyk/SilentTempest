package ca.nigelchan.silenttempest.data.actors.traps;

import ca.nigelchan.silenttempest.data.actors.ActorData;
import ca.nigelchan.silenttempest.data.actors.sequences.SequenceDataList;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.traps.Trap;
import ca.nigelchan.silenttempest.resources.GameResource;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class TrapData extends ActorData {

	private SequenceDataList sequenceList = new SequenceDataList();

	public TrapData(Vector2 initPosition, float initRotation, float speed) {
		super(initPosition, initRotation, speed);
	}
	
	public abstract Trap createTrap(World world, GameResource resource);

	// Getters
	public SequenceDataList getSequenceList() {
		return sequenceList;
	}

}
