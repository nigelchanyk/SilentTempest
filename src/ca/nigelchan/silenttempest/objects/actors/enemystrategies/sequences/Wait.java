package ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences;

import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Vector2;

public class Wait extends Sequence {
	
	private float remainingTime;

	public Wait(Actor actor, Vector2 initPosition, float time) {
		super(actor, initPosition);
		remainingTime = time;
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (completed)
			return;
		remainingTime -= elapsedTime;
		if (remainingTime <= 0)
			completed = true;
	}

}
