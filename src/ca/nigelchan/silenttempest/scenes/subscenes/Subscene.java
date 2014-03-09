package ca.nigelchan.silenttempest.scenes.subscenes;

import java.util.HashSet;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import ca.nigelchan.silenttempest.userinterface.game.ITouchableInterfaceObject;
import ca.nigelchan.silenttempest.userinterface.game.UserInterfaceTouchEvent;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Subscene implements IOnSceneTouchListener {
	
	private boolean active = false;
	private Entity hud = new Entity();
	private HashSet<ITouchableInterfaceObject> touchables = new HashSet<ITouchableInterfaceObject>();
	private ITouchableInterfaceObject activeTouchable = null;
	
	public void activate() {
		if (active)
			return;
		active = true;
		onActivate();
	}
	
	public void deactivate() {
		if (!active)
			return;
		active = false;
		onDeactivate();
	}
	
	public void dispose() {
		for (int i = 0; i < hud.getChildCount(); ++i) {
			hud.getChildByIndex(i).dispose();
		}
		hud.dispose();
		onDispose();
	}
	
	public void load() {
		onLoad();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		boolean returnVal = false;
		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
            for (ITouchableInterfaceObject touchable : touchables) {
            	Vector2 position = touchable.getWorldPosition();
            	Vector2 dimension = touchable.getDimension();
            	if (pSceneTouchEvent.getX() >= position.x()
            		&& pSceneTouchEvent.getX() <= position.x() + dimension.x()
            		&& pSceneTouchEvent.getY() >= position.y()
            		&& pSceneTouchEvent.getY() <= position.y() + dimension.y()) {
            		activeTouchable = touchable;
            		return activeTouchable.onTouchEvent(UserInterfaceTouchEvent.DOWN);
            	}
            }
            return false;
		case TouchEvent.ACTION_UP:
			if (activeTouchable == null)
				return false;
			returnVal = activeTouchable.onTouchEvent(UserInterfaceTouchEvent.UP);
			activeTouchable = null;
			return returnVal;
		case TouchEvent.ACTION_MOVE:
			if (activeTouchable == null)
				return false;
            Vector2 position = activeTouchable.getWorldPosition();
            Vector2 dimension = activeTouchable.getDimension();
            if (pSceneTouchEvent.getX() < position.x()
                || pSceneTouchEvent.getX() > position.x() + dimension.x()
                || pSceneTouchEvent.getY() < position.y()
                || pSceneTouchEvent.getY() > position.y() + dimension.y()) {
                returnVal = activeTouchable.onTouchEvent(UserInterfaceTouchEvent.LEAVE);
                activeTouchable = null;
                return returnVal;
            }

		}
		return false;
	}
	
	protected void registerTouch(ITouchableInterfaceObject touchable) {
		touchables.add(touchable);
	}
	
	protected void unregisterTouch(ITouchableInterfaceObject touchable) {
		touchables.remove(touchable);
	}
	
	protected abstract void onActivate();
	protected abstract void onDeactivate();
	protected abstract void onDispose();
	protected abstract void onLoad();
	
	// Getters
	public Entity getHUD() {
		return hud;
	}

	public boolean isActive() {
		return active;
	}

}
