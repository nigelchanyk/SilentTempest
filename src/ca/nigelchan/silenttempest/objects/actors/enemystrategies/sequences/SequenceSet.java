package ca.nigelchan.silenttempest.objects.actors.enemystrategies.sequences;

import android.util.SparseIntArray;
import ca.nigelchan.silenttempest.util.Coordinate;

public class SequenceSet {
		
	private SparseIntArray map = new SparseIntArray();
	
	public void add(Coordinate coordinate, int sequenceIndex) {
		map.put(coordinateHash(coordinate), sequenceIndex);
	}
	
	public boolean contains(Coordinate coordinate) {
		return map.get(coordinateHash(coordinate), -1) >= 0;
	}
	
	public int getIndex(Coordinate coordinate) {
		return map.get(coordinateHash(coordinate), -1);
	}
	
	private int coordinateHash(Coordinate coordinate) {
		return (coordinate.x() << 16) + coordinate.y();
	}

}
