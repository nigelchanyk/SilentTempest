package ca.nigelchan.silenttempest.objects.actors.traps;

import org.andengine.engine.handler.IUpdateHandler;

import ca.nigelchan.silenttempest.data.actors.sequences.SequenceData;
import ca.nigelchan.silenttempest.data.actors.traps.TrapData;
import ca.nigelchan.silenttempest.objects.World;
import ca.nigelchan.silenttempest.objects.actors.Actor;
import ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences.Sequence;
import ca.nigelchan.silenttempest.util.Vector2;

public abstract class Trap extends Actor {

	private int current = 0;
	private Sequence[] sequence;

	public Trap(TrapData data, World world, Vector2 dimension) {
		super(data, dimension, world);
		sequence = new Sequence[data.getSequenceList().size()];
		int i = 0;
		Vector2 pos = data.getInitialPosition();
		for (SequenceData sequenceItem : data.getSequenceList().getSequence()) {
			sequence[i] = sequenceItem.create(this, pos);
			pos = pos.add(sequenceItem.getTranslation().toVector2());
			i++;
		}
		
		if (sequence.length > 0) {
			sequence[current].onSpawn();
			sequence[current].onStart();
		}
		
		registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {
			}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				update(pSecondsElapsed);
			}
		});
	}
	
	protected abstract boolean collided();
	protected abstract void handleUpdate(float elapsedTime);
	
	private int next() {
		return (current + 1) % sequence.length;
	}
	
	private void update(float elapsedTime) {
		if (sequence.length > 0) {
			sequence[current].onUpdate(elapsedTime);
			if (sequence[current].isCompleted()) {
				current = next();
				sequence[current].onStart();
			}
		}
		handleUpdate(elapsedTime);
		if (collided())
			world.getPlayer().setAlive(false);
	}

}
