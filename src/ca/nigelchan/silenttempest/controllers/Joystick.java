package ca.nigelchan.silenttempest.controllers;

import java.util.LinkedList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.controllers.joystickstates.IdleState;
import ca.nigelchan.silenttempest.controllers.joystickstates.JoystickState;
import ca.nigelchan.silenttempest.util.Vector2;

public class Joystick implements IOnSceneTouchListener, IUpdateHandler {
	
	private LinkedList<IListener> subscribers = new LinkedList<Joystick.IListener>();
	private JoystickState state;
	
	public Joystick(DisplayMetrics metrics) {
		state = new IdleState(metrics);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		Vector2 position = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			state = state.onTouchDown(position, subscribers);
			break;
		case TouchEvent.ACTION_MOVE:
			state = state.onTouchMove(position, subscribers);
			break;
		case TouchEvent.ACTION_UP:
			state = state.onTouchUp(position, subscribers);
			break;
		default:
			state = state.onTouchExit(position, subscribers);
			break;
		}
		return false;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		state.onUpdate(pSecondsElapsed);
	}

	@Override
	public void reset() {
	}
	
	public void subscribe(IListener subscriber) {
		subscribers.add(subscriber);
	}
	
	public void unsubscribe(IListener subscriber) {
		subscribers.remove(subscriber);
	}
	
	public static interface IListener {
		
		public void onAccept();
		public void onDoubleTap();
		public void onMove(float rotation);
		public void onRelease();
		public void onTouch(Vector2 position);

	}

}
