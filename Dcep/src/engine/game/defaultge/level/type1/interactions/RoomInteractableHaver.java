package engine.game.defaultge.level.type1.interactions;

import java.util.ArrayList;
import java.util.function.Consumer;

import engine.entityfw.IEntityV3;
import engine.entityfw.components.IHasInteractables;
import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2DInteractionV3.IInteractable;

public class RoomInteractableHaver implements IEntityV3, IHasInteractables {
	protected ArrayList<IInteractable> interactables = new ArrayList<>();

	public RoomInteractableHaver(Room room) {
		//room.doors.values().forEach((e) -> interactables.add(new DoorInteractable(e)));
	}
	
	public ArrayList<IInteractable> getInteractables(){
		return interactables;
	}

	@Override
	public void forEachInteractables(Consumer<IInteractable> task) {
		interactables.forEach(task);
	}
}
