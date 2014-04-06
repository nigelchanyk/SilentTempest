package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.util.Vector2;

public class IdleState extends JoystickState {
	
	public IdleState(DisplayMetrics metrics) {
		super(metrics);
	}

	@Override
	public JoystickState onTouchDown(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onTouch(position);
		return new TouchDownState(position, getMetrics());
	}

}
