package ca.nigelchan.silenttempest.scenes.subscenes;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.resources.LoadingResource;
import ca.nigelchan.silenttempest.userinterface.loading.LoadingScenePenguin;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;

public class LoadingSceneMinigame extends Subscene {
	
	private static final float DURATION = 0.5f;
	private static final float SPEED = 2;
	
	private static enum State {
		IDLE,
		COUNTDOWN,
		SHOW,
		STAY,
		HIDE
	}
	
	private ICallback callback = null;
	private Direction direction = null;
	private LoadingScenePenguin penguin;
	private float remainingTime = 0;
	private LoadingResource resource;
	private State state = State.IDLE;
	private Entity world = new Entity();
	
	public LoadingSceneMinigame(LoadingResource resource) {
		this.resource = resource;
		penguin = new LoadingScenePenguin(resource);
		world.attachChild(penguin);
		getHUD().attachChild(world);
		registerTouch(penguin);
	}
	
	public void prepare(ICallback callback) {
		this.callback = callback;
		state = State.COUNTDOWN;
		Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
		direction = dirs[MathHelper.getRandom().nextInt(dirs.length)];

		penguin.setX(
			MathHelper.getRandom().nextFloat() * (resource.getScreenHeight() - penguin.getWidth())
		);
		penguin.setY(resource.getScreenHeight() + penguin.getHeight());
		
		world.setRotationCenter(resource.getScreenHeight() / 2, resource.getScreenHeight() / 2);
		world.setRotation(MathHelper.toDegrees(MathHelper.getRotation(direction)));
		if (direction == Direction.WEST)
			world.setX(resource.getScreenWidth() - resource.getScreenHeight());
		else if (direction == Direction.EAST)
			world.setX(0);
		else
			world.setX((resource.getScreenWidth() - resource.getScreenHeight()) / 2);
		
		penguin.reset();
		remainingTime = MathHelper.getRandom().nextFloat();
	}
	
	@Override
	protected void onActivate() {
		
	}

	@Override
	protected void onDeactivate() {
	}

	@Override
	protected void onDispose() {
		penguin.dispose();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onUpdate(float elapsedTime) {
		switch (state) {
		case IDLE:
			return;
		case COUNTDOWN:
			remainingTime -= elapsedTime;
			if (remainingTime <= 0)
				state = State.SHOW;
			return;
		case SHOW:
			penguin.setY(penguin.getY() - resource.getDPI() * SPEED * elapsedTime);
			if (penguin.getY() <= resource.getScreenHeight() - penguin.getHeight()) {
				penguin.setY(resource.getScreenHeight() - penguin.getHeight());
				state = State.STAY;
			}
			remainingTime = DURATION;
			return;
		case STAY:
			remainingTime -= elapsedTime;
			if (remainingTime <= 0)
				state = State.HIDE;
			return;
		case HIDE:
			penguin.setY(penguin.getY() + resource.getDPI() * SPEED * elapsedTime);
			if (penguin.getY() >= resource.getScreenHeight()) {
				state = State.IDLE;
				callback.onCompleted();
			}
		}
	}
	
	public static interface ICallback {
		public void onCompleted();
	}

}
