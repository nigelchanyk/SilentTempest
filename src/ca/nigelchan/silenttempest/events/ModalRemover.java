package ca.nigelchan.silenttempest.events;

public class ModalRemover extends EventComponent {
	
	private EventLayer eventLayer;
	
	public ModalRemover(EventLayer eventLayer) {
		this.eventLayer = eventLayer;
	}

	@Override
	public void dispose() {
		eventLayer.setModal(null);
	}

	@Override
	public void onLoad() {
		completed = true;
	}

	@Override
	public void onUpdate(float elapsedTime) {
	}

}
