package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;

public class ModalRemover extends EventComponent {
	
	private GameInterface gameInterface;
	
	public ModalRemover(GameInterface gameInterface) {
		this.gameInterface = gameInterface;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void onLoad() {
		gameInterface.setModal(null);
		completed = true;
	}

	@Override
	public void onUpdate(float elapsedTime) {
	}

}
