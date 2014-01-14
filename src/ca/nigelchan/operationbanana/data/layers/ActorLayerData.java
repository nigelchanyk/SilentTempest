package ca.nigelchan.operationbanana.data.layers;

import java.util.ArrayList;

import ca.nigelchan.operationbanana.data.actors.EnemyData;
import ca.nigelchan.operationbanana.data.actors.PlayerData;

public class ActorLayerData extends LayerData {
	
	private ArrayList<EnemyData> enemies = new ArrayList<EnemyData>();
	private PlayerData player;

	public ActorLayerData(int width, int height, PlayerData player) {
		super(width, height);
		this.player = player;
	}

	@Override
	public void accept(ILayerDataVisitor visitor) {
		visitor.visit(this);
	}

	// Getters
	public Iterable<EnemyData> getEnemies() {
		return enemies;
	}
	
	public PlayerData getPlayer() {
		return player;
	}
}
