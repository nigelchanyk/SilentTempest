package ca.nigelchan.silenttempest.data.actors;

import ca.nigelchan.silenttempest.util.Coordinate;

public class PlayerData extends ActorData {

	public PlayerData(Coordinate initPosition, float initRotation, float speed) {
		super(initPosition.toCenterVector2(), initRotation, speed);
	}

}
