package ca.nigelchan.silenttempest.data.actors;

import ca.nigelchan.silenttempest.data.actors.sequences.SequenceDataList;
import ca.nigelchan.silenttempest.util.Coordinate;

public class EnemyData extends ActorData {
	
	private String id;
	private SequenceDataList sequenceList = new SequenceDataList();
	private int visionRange;

	public EnemyData(Coordinate initPosition, float initRotation, float speed, int visionRange, String id) {
		super(initPosition.toCenterVector2(), initRotation, speed);
		this.visionRange = visionRange;
		this.id = id;
	}
	
	// Getters
	public String getID() {
		return id;
	}

	public SequenceDataList getSequenceList() {
		return sequenceList;
	}

	public int getVisionRange() {
		return visionRange;
	}

}
