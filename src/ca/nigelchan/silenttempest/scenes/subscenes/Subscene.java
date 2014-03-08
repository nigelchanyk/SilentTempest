package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public abstract class Subscene implements IOnSceneTouchListener {
	
	private Entity hud = new Entity();
	private boolean active = false;
	
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
		return false;
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
