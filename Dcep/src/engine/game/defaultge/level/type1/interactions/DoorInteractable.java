package engine.game.defaultge.level.type1.interactions;

import engine.physic.basic2DInteractionV3.IInteractable.Interactable;
import engine.save.room.type1.RoomState.Door;
import my.util.geometry.IRectangle;

public class DoorInteractable extends Interactable {
	protected Door door;

	public DoorInteractable(Door ndoor, int x, int y, int w, int h) {
		super(new IRectangle.Rectangle(x, y, x + w, y + h));
		door = ndoor;
	}

//	public DoorInteractable(Door ndoor) {
//		this(ndoor,0, 0, 0, 0);
//	}

	public Door getDoor() {
		return door;
	}

}
