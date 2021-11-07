package engine.game.defaultge.level.type1.entity;

import engine.entityfw.IEntityV3;
import engine.game.defaultge.level.type1.Room;
import my.util.Cardinal;

public interface IRoomTraverserEntity extends IEntityV3 {

	public void enter(Room room, Cardinal dir);

	public void leave(Room room);

	
}
