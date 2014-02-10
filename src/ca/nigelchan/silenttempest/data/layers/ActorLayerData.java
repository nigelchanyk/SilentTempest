package ca.nigelchan.silenttempest.data.layers;

import java.util.ArrayList;

import ca.nigelchan.silenttempest.data.actors.EnemyData;
import ca.nigelchan.silenttempest.data.actors.PlayerData;

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
	
	public void addEnemy(EnemyData enemy) {
		enemies.add(enemy);
	}

	// Getters
	public Iterable<EnemyData> getEnemies() {
		return enemies;
	}
	
	public PlayerData getPlayer() {
		return player;
	}
}
