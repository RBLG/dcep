package engine.save.room.type1;

import engine.game.defaultge.level.type1.RoomPool.DoorType;

public interface ITypeCriterias {
	DoorType judgeDoorSize(Side side, int size);
}
