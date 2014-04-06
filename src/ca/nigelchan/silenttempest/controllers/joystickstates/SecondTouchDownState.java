package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.util.Vector2;

public class SecondTouchDownState extends TouchDownState {

	public SecondTouchDownState(Vector2 position, DisplayMetrics metrics) {
		super(position, metrics);
	}

	@Override
	public JoystickState onTouchUp(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onRelease();
		if (isTap()) {
			for (Joystick.IListener listener : listeners)
				listener.onDoubleTap();
		}
		return new IdleState(getMetrics());
	}

}
