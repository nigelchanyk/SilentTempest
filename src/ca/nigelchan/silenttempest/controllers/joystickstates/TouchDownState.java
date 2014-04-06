package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class TouchDownState extends JoystickState {
	
	public static final float TAP_TOLERANCE = 0.5f;
	
	private Vector2 start;
	private float totalElapsedTime = 0;
	private int toleranceSq;
	
	public TouchDownState(Vector2 position, DisplayMetrics metrics) {
		super(metrics);
		start = position;
		toleranceSq = MathHelper.sq(metrics.densityDpi / 8);
	}

	@Override
	public JoystickState onTouchMove(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		float rotation = MathHelper.getRotation(start, position);
		// snap to nearest pi/4
		rotation = (float)Math.floor((rotation + MathHelper.PI_OVER_8) / MathHelper.PI_OVER_4) * MathHelper.PI_OVER_4;
		for (Joystick.IListener listener : listeners)
			listener.onMove(rotation);

		if (start.distanceSquare(position) < toleranceSq)
			return this;

		for (Joystick.IListener listener : listeners)
			listener.onAccept();
		return new MoveState(start, getMetrics());
	}

	@Override
	public JoystickState onTouchUp(Vector2 position, LinkedList<Joystick.IListener> listeners) {
		if (isTap()) {
			for (Joystick.IListener listener : listeners)
				listener.onRelease();
			return new SingleTap(getMetrics());
		}
		return super.onTouchUp(position, listeners);
	}

	@Override
	public void onUpdate(float elapsedTime) {
		totalElapsedTime += elapsedTime;
	}

	public boolean isTap() {
		return totalElapsedTime < TAP_TOLERANCE;
	}
}
