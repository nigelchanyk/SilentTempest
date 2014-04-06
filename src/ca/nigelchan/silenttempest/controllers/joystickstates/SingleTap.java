package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.util.Vector2;
import android.util.DisplayMetrics;

public class SingleTap extends JoystickState {

	public SingleTap(DisplayMetrics metrics) {
		super(metrics);
	}

	@Override
	public JoystickState onTouchDown(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onTouch(position);
		return new SecondTouchDownState(position, getMetrics());
	}

}
