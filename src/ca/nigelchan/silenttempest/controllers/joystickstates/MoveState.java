package ca.nigelchan.silenttempest.controllers.joystickstates;

import java.util.LinkedList;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.controllers.Joystick;
import ca.nigelchan.silenttempest.controllers.Joystick.IListener;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class MoveState extends JoystickState {
	
	private Vector2 origin;

	public MoveState(Vector2 origin, DisplayMetrics metrics) {
		super(metrics);
		this.origin = origin;
	}

	@Override
	public JoystickState onTouchMove(Vector2 position, LinkedList<IListener> listeners) {
		float rotation = MathHelper.getRotation(origin, position);
		// snap to nearest pi/4
		rotation = (float)Math.floor((rotation + MathHelper.PI_OVER_8) / MathHelper.PI_OVER_4) * MathHelper.PI_OVER_4;
		for (Joystick.IListener listener : listeners)
			listener.onMove(rotation);
		return this;
	}

}
