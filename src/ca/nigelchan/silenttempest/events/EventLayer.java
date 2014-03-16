package ca.nigelchan.silenttempest.events;

import org.andengine.entity.Entity;

import ca.nigelchan.silenttempest.modals.Modal;

public class EventLayer extends Entity {
	
	private Modal modal;

	public void setModal(Modal modal) {
		if (this.modal != null) {
			this.modal.detachSelf();
			this.modal.dispose();
		}
		this.modal = modal;
		if (modal != null)
			attachChild(modal);
	}

}
