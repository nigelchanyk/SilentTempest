package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.modals.Modal;
import ca.nigelchan.silenttempest.resources.CommonResource;
import ca.nigelchan.silenttempest.scenes.subscenes.GameInterface;

public class ModalSetter extends EventComponent {
	
	private GameInterface gameInterface;
	private CommonResource resource;
	private String text;
	
	public ModalSetter(String text, GameInterface gameInterface, CommonResource resource) {
		this.text = text;
		this.gameInterface = gameInterface;
		this.resource = resource;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
		gameInterface.setModal(new Modal(resource, text));
		completed = true;
	}

	@Override
	public void onUpdate(float elapsedTime) {
	}

}
