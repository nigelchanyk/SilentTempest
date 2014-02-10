package ca.nigelchan.silenttempest.scenes;

import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import ca.nigelchan.silenttempest.managers.SceneManager;
import ca.nigelchan.silenttempest.resources.Resource;

public abstract class BaseScene extends Scene {
	
	public enum PopOperation {
		NONE,
		UNLOAD,
		DISPOSE
	}
	
	protected BaseGameActivity activity;
	protected Camera camera;
	protected Engine engine;
	protected boolean loadAsynchronous = false;
	protected SceneManager manager;
	protected boolean inactiveResourceAllowed = true;
	protected boolean resourceLoaded = false;
	
	private LinkedList<IEntity> autoReleaseEntities = new LinkedList<IEntity>();
	private PopOperation defualtPopOperation = PopOperation.NONE;
	private HUD hud = null; // Nullable
	private Resource resource;
	private boolean sceneCreated = false;
	
	public BaseScene(SceneManager manager) {
		this.manager = manager;
		this.activity = manager.getActivity();
		this.camera = manager.getCamera();
		this.engine = manager.getEngine();
	}
	
	@Override
	public void attachChild(final IEntity entity) {
		attachChild(entity, true);
	}
	
	public void attachChild(final IEntity entity, boolean autoRelease) {
		super.attachChild(entity);
		if (autoRelease)
			autoReleaseEntities.add(entity);
	}

	public void disposeScene() {
		detachChildren();
		for (IEntity entity : autoReleaseEntities)
			entity.dispose();
		autoReleaseEntities.clear();
		dispose();
		
		if (hud != null) {
			if (hud == camera.getHUD())
				camera.setHUD(null);

			for (int i = 0; i < hud.getChildCount(); ++i)
				hud.getChildByIndex(i).dispose();
			hud.dispose();
		}
		
		unloadResources();
	}
	
	public void prepareScene() {
		loadResources();
		if (!sceneCreated)
			createScene();
		sceneCreated = true;
		camera.setHUD(hud);
	}
	
	public void unloadResources() {
		if (resource != null && resourceLoaded)
			resource.unload();
        resourceLoaded = false;
	}
	
	public abstract void onBackKeyPressed();

	protected abstract void createScene();
	
	private void loadResources() {
		if (resource != null && !resourceLoaded)
			resource.load();
        resourceLoaded = true;
	}
	
	// Getters
	public PopOperation getDefualtPopOperation() {
		return defualtPopOperation;
	}

	public Resource getResource() {
		return resource;
	}

	public boolean isInactiveResourceAllowed() {
		return inactiveResourceAllowed;
	}

	public boolean isLoadAsynchronous() {
		return loadAsynchronous;
	}

	public boolean isResourceLoaded() {
		return resourceLoaded;
	}

	protected HUD getHud() {
		return hud;
	}

	// Setters
	public void setDefualtPopOperation(PopOperation defualtPopOperation) {
		this.defualtPopOperation = defualtPopOperation;
	}

	protected void setHud(HUD hud) {
		this.hud = hud;
	}

	protected void setResource(Resource resource) {
		this.resource = resource;
	}

}
