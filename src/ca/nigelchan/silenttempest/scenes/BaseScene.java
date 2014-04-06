package ca.nigelchan.silenttempest.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
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
	protected SceneManager manager;
	protected boolean inactiveResourceAllowed = true;
	protected boolean resourceLoaded = false;
	
	private boolean loadAsynchronous = false;
	private PopOperation defualtPopOperation = PopOperation.NONE;
	private Resource resource;
	private Entity objectLayer = new Entity();
	private boolean sceneCreated = false;
	private Entity uiLayer = new Entity();
	
	public BaseScene(SceneManager manager) {
		this.manager = manager;
		this.activity = manager.getActivity();
		this.camera = manager.getCamera();
		this.engine = manager.getEngine();
		super.attachChild(objectLayer);
		super.attachChild(uiLayer);
	}

	@Override
	public void attachChild(final IEntity entity) {
		objectLayer.attachChild(entity);
	}
	
	public void attachUI(IEntity entity) {
		uiLayer.attachChild(entity);
	}
	
	public void detachUI(IEntity entity) {
		uiLayer.detachChild(entity);
	}

	public void disposeScene() {
		detachChildren();
		for (int i = 0; i < objectLayer.getChildCount(); ++i)
			objectLayer.getChildByIndex(i).dispose();
		for (int i = 0; i < uiLayer.getChildCount(); ++i)
			uiLayer.getChildByIndex(i).dispose();

		objectLayer.dispose();
		uiLayer.dispose();
		dispose();
		
		unloadResources();
	}
	
	public void prepareScene() {
		loadResources();
		if (!sceneCreated)
			createScene();
		sceneCreated = true;
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

	/**
	 * 
	 * @return true if resource can be deallocated when
	 *		 the scene is inactive.
	 */
	public boolean isInactiveResourceAllowed() {
		return inactiveResourceAllowed;
	}

	public boolean isLoadAsynchronous() {
		return loadAsynchronous;
	}

	public boolean isResourceLoaded() {
		return resourceLoaded;
	}

	// Setters
	protected void setDefualtPopOperation(PopOperation defualtPopOperation) {
		this.defualtPopOperation = defualtPopOperation;
	}

	protected void setResource(Resource resource) {
		this.resource = resource;
	}

	protected void setLoadAsynchronous(boolean loadAsynchronous) {
		this.loadAsynchronous = loadAsynchronous;
	}

}
