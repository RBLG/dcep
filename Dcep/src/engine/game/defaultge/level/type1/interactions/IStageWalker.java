package engine.game.defaultge.level.type1.interactions;

import engine.game.defaultge.level.type1.Room;

public interface IStageWalker extends IRoomTraverserEntity {

	public void outsideThink();

	public void returnToRoomExistence(Room room);

}
