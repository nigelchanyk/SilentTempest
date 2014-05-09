package ca.nigelchan.silenttempest.events;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;

import ca.nigelchan.silenttempest.modals.Modal;
import ca.nigelchan.silenttempest.resources.GameResource;

public class EventLayer extends Entity {
	
	private Rectangle backgroundColor;
	private Modal modal;
	
	public EventLayer(GameResource resource) {
		backgroundColor = new Rectangle(0, 0, resource.getScreenWidth(), resource.getScreenHeight(), resource.getVertexBufferObjectManager());
		backgroundColor.setColor(0, 0, 0, 0);
		attachChild(backgroundColor);
	}

	@Override
	public void dispose() {
		backgroundColor.dispose();
		super.dispose();
	}
	
	public void setBackgroundColor(float red, float green, float blue, float alpha) {
		backgroundColor.setColor(red, green, blue, alpha);
	}

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
