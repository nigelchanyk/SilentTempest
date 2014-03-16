package ca.nigelchan.silenttempest.events;

public class WaitEvent extends EventComponent {
	
	private float remainingTime;
	
	public WaitEvent(float duration) {
		remainingTime = duration;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onUpdate(float elapsedTime) {
		remainingTime -= elapsedTime;
		if (remainingTime <= 0)
			completed = true;
	}

}
