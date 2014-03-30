package ca.nigelchan.silenttempest.managers;

import java.util.HashSet;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

import ca.nigelchan.silenttempest.scenes.subscenes.Subscene;

public class SubsceneManager implements IOnSceneTouchListener, IUpdateHandler {
	
	private HashSet<Subscene> subscenes = new HashSet<Subscene>();
	private Subscene activeSubscene = null;
	private Entity uiLayer;
	
	public SubsceneManager(Entity uiLayer) {
		this.uiLayer = uiLayer;
	}
	
	public void add(Subscene ... subscenes) {
		for (int i = 0; i < subscenes.length; ++i) {
			this.subscenes.add(subscenes[i]);
			subscenes[i].setManager(this);
		}
	}
	
	public void activate(Subscene subscene) {
		for (Subscene each : subscenes) {
			each.deactivate();
		}
		subscene.activate();
		activeSubscene = subscene;
		uiLayer.detachChildren();
		uiLayer.attachChild(subscene.getHUD());
	}
	
	public void dispose() {
		for (Subscene subscene : subscenes) {
			subscene.dispose();
		}
	}
	
	public void load() {
		for (Subscene subscene :subscenes) {
			subscene.load();
		}
	}

	public void onBackKeyPressed() {
		if (activeSubscene == null)
			return;
		activeSubscene.onBackKeyPressed();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (activeSubscene == null)
			return false;
		return activeSubscene.onSceneTouchEvent(pScene, pSceneTouchEvent);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (activeSubscene != null)
			activeSubscene.update(pSecondsElapsed);
	}

	@Override
	public void reset() {
	}

}
