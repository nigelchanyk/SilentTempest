package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class JoystickState {
	
	private DisplayMetrics metrics;
	
	public JoystickState(DisplayMetrics metrics) {
		this.metrics = metrics;
	}

	public JoystickState onTouchDown(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onRelease();
		return new IdleState(metrics);
	}
	
	public JoystickState onTouchExit(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onRelease();
		return new IdleState(metrics);
	}
	
	public JoystickState onTouchMove(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onRelease();
		return new IdleState(metrics);
	}
	
	public JoystickState onTouchUp(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		for (Joystick.IListener listener : listeners)
			listener.onRelease();
		return new IdleState(metrics);
	}
	
	public void onUpdate(float elapsedTime) {
	}

	// Getters
	public DisplayMetrics getMetrics() {
		return metrics;
	}

}
