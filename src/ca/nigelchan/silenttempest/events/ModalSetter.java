package ca.nigelchan.silenttempest.events;

import ca.nigelchan.silenttempest.modals.Modal;
import ca.nigelchan.silenttempest.resources.CommonResource;

public class ModalSetter extends EventComponent {
	
	private EventLayer eventLayer;
	private CommonResource resource;
	private String text;
	
	public ModalSetter(String text, EventLayer eventLayer, CommonResource resource) {
		this.text = text;
		this.eventLayer = eventLayer;
		this.resource = resource;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad() {
		eventLayer.setModal(new Modal(resource, text));
		completed = true;
	}

	@Override
	public void onUpdate(float elapsedTime) {
	}

}
