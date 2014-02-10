package ca.nigelchan.silenttempest.data.actors;

import java.util.ArrayList;

import ca.nigelchan.silenttempest.data.actors.sequences.SequenceData;
import ca.nigelchan.silenttempest.util.Coordinate;

public class EnemyData extends ActorData {
	
	private Coordinate sequenceOffset = Coordinate.ZERO;
	private ArrayList<SequenceData> sequence = new ArrayList<SequenceData>();
	private int visionRange;

	public EnemyData(Coordinate initPosition, float initRotation, float speed, int visionRange) {
		super(initPosition, initRotation, speed);
		this.visionRange = visionRange;
	}

	public void addSequenceItem(SequenceData sequenceItem) {
		sequence.add(sequenceItem);
		sequenceOffset = sequenceOffset.add(sequenceItem.getTranslation());
	}
	
	// Getters
	public Iterable<SequenceData> getSequence() {
		if (sequenceOffset.x() != 0 || sequenceOffset.y() != 0)
			throw new IllegalStateException("Enemy sequence is not repeatable.");
		return sequence;
	}
	
	public int getSequenceSize() {
		return sequence.size();
	}

	public int getVisionRange() {
		return visionRange;
	}

}
