package ca.nigelchan.silenttempest.data.actors.sequences;

import java.util.ArrayList;

import ca.nigelchan.silenttempest.util.Coordinate;

public class SequenceDataList {

	private Coordinate sequenceOffset = Coordinate.ZERO;
	private ArrayList<SequenceData> sequence = new ArrayList<SequenceData>();

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
	
	public int size() {
		return sequence.size();
	}

}
