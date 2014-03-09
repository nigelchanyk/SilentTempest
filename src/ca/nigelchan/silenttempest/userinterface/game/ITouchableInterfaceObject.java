package ca.nigelchan.silenttempest.userinterface.game;

import ca.nigelchan.silenttempest.util.Vector2;

public interface ITouchableInterfaceObject {

	public Vector2 getWorldPosition();
	public Vector2 getDimension();
	
	public boolean onTouchEvent(UserInterfaceTouchEvent event);

}
