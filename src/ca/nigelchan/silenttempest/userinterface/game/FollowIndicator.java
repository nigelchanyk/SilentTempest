package ca.nigelchan.silenttempest.userinterface.game;

import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.WorldObject;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class FollowIndicator<T extends Actor> extends WorldObject implements Actor.IListener {
	
	private T actor = null;
	
	public FollowIndicator(OffsetType offsetType, World world) {
		super(Vector2.ZERO, offsetType, world);
	}
	
	public void follow(T actor) {
		unfollow();
		setPosition(actor.getPosition().add(getFollowOffset()));
		actor.subscribe(this);
		onFollow(actor);
		this.actor = actor;
	}
	
	public void unfollow() {
		if (this.actor != null) {
			this.actor.unsubscribe(this);
			onUnfollow(this.actor);
		}
	}

	@Override
	public void onPositionChanged(Vector2 position) {
		setPosition(position.add(getFollowOffset()));
	}
	
	protected void onFollow(T actor) {
	}
	
	protected void onUnfollow(T actor) {
	}
	
	protected abstract Vector2 getFollowOffset();

}
