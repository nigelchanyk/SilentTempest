package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.util.Vector2;
import android.util.DisplayMetrics;

public class SingleTap extends IdleState {
	
	public static final float DOUBLE_TAP_THRESHOLD = 0.5f;
	
	private float totalElapsedTime = 0;

	public SingleTap(DisplayMetrics metrics) {
		super(metrics);
	}

	@Override
	public JoystickState onTouchDown(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		if (totalElapsedTime <= DOUBLE_TAP_THRESHOLD) {
			for (Joystick.IListener listener : listeners)
				listener.onTouch(position);
			return new SecondTouchDownState(position, getMetrics());
		}
		
		return super.onTouchDown(position, listeners);
	}

	@Override
	public void onUpdate(float elapsedTime) {
		totalElapsedTime += elapsedTime;
	}

}
