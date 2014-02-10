package ca.nigelchan.silenttempest.controllers;

import java.util.LinkedList;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import android.util.DisplayMetrics;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class Joystick implements IOnSceneTouchListener {
	
	private boolean active = false;
	private LinkedList<IListener> subscribers = new LinkedList<Joystick.IListener>();
	private Vector2 now;
	private Vector2 start;
	private int toleranceSq;
	
	public Joystick(DisplayMetrics metrics) {
		toleranceSq = MathHelper.sq(metrics.densityDpi / 8);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			start = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			now = start;
            for (IListener subscriber : subscribers)
                subscriber.onTouch(start);
			break;
		case TouchEvent.ACTION_MOVE:
			now = new Vector2(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			float rotation = MathHelper.getRotation(start, now);
			// snap to nearest pi/4
			rotation = (float)Math.floor((rotation + MathHelper.PI_OVER_8) / MathHelper.PI_OVER_4) * MathHelper.PI_OVER_4;
			if (!active) {
				active = start.distanceSquare(now) >= toleranceSq;
				if (active) {
	                for (IListener subscriber : subscribers)
	                    subscriber.onAccept();
				}
			}
			for (IListener subscriber : subscribers)
				subscriber.onMove(rotation);
			break;
        default:
        	active = false;
			for (IListener subscriber : subscribers)
				subscriber.onRelease();
		}
        return false;
	}
	
	public void subscribe(IListener subscriber) {
		subscribers.add(subscriber);
	}
	
	public void unsubscribe(IListener subscriber) {
		subscribers.remove(subscriber);
	}
	
	
	public static interface IListener {
		
		public void onAccept();
		public void onMove(float rotation);
		public void onRelease();
		public void onTouch(Vector2 position);

	}
}
