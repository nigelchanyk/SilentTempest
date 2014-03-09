package ca.nigelchan.silenttempest.reactors;

import java.util.ArrayList;

public abstract class Reactor {
	
	private ArrayList<IReaction> reactionList = new ArrayList<IReaction>();
	
	public void addReaction(IReaction reaction) {
		reactionList.add(reaction);
	}
	
	public void removeReaction(IReaction reaction) {
		reactionList.remove(reaction);
	}
	
	public abstract void update(float elapsedTime);
	
	protected void react() {
		for (IReaction reaction : reactionList)
			reaction.react();
	}

}
