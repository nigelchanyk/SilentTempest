package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.util.MathHelper;

public class FadeEvent extends EventComponent {
	
	private float current;
	private float delta;
	private EventLayer eventLayer;
	private float remainingTime = 0;
	
	public FadeEvent(EventLayer eventLayer, float start, float end, float duration) {
		this.eventLayer = eventLayer;
		// Alpha is reversed here because we are now referring to
		// the alpha of the filter, not the screen.
		this.current = end;
		this.delta = (start - end) / duration;
		this.remainingTime = duration;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
		updateColor();
	}

	@Override
	public void onUpdate(float elapsedTime) {
		current += delta * Math.min(elapsedTime, remainingTime);
		remainingTime -= elapsedTime;
		updateColor();
		if (remainingTime <= 0)
			completed = true;
	}
	
	private void updateColor() {
		eventLayer.setBackgroundColor(0, 0, 0, MathHelper.clamp(current, 0, 1));
	}

}
