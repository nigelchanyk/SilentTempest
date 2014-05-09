package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.objects.actors.Player;

public class PlayerHealthMonitor extends EventComponent {
	
	private Player player;
	
	public PlayerHealthMonitor(Player player) {
		this.player = player;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onUpdate(float elapsedTime) {
		if (!player.isAlive())
			completed = true;
	}

}
