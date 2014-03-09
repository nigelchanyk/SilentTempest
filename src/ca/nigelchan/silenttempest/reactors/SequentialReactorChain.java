package ca.nigelchan.silenttempest.reactors;

import java.util.LinkedList;

public class SequentialReactorChain extends Reactor {
	
	private LinkedList<Reactor> reactors = new LinkedList<Reactor>();
	
	public void addReactor(Reactor reactor) {
		reactor.addReaction(new IReaction() {
			
			@Override
			public void react() {
				reactors.poll();
			}
		});
		reactors.add(reactor);
	}

	public void removeReactor(Reactor reactor) {
		reactors.remove(reactor);
	}

	@Override
	public void update(float elapsedTime) {
		if (reactors.isEmpty()) {
			react();
			return;
		}
		reactors.peek().update(elapsedTime);
	}

}
