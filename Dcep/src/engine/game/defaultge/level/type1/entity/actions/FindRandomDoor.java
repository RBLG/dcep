package engine.game.defaultge.level.type1.entity.actions;

import java.util.Random;
import java.util.function.Supplier;

import engine.entityfwp2.ai.action.IAction;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.save.room.type1.RoomState.Door;
import engine.save.room.type1.Side;
import my.util.Cardinal;
import my.util.Field;

public class FindRandomDoor implements IAction {

	protected Supplier<Room> room;
	protected Field<Cardinal> dir;
	protected static Random rand = new Random();

	public FindRandomDoor(Supplier<Room> nroom, Field<Cardinal> ndir) {
		room = nroom;
		dir = ndir;
	}

	@Override
	public Status doOngoing() {
		Side rside = Side.values()[rand.nextInt(Side.values().length)];
		Door rdoor = room.get().doors.get(rside);
		if (rdoor != null && room.get().doors2[rside.ordinal()] != DoorType.wall) {
			dir.set(rdoor.side.toCardinal());
			return Status.completed;
		}
		return Status.running;
	}

}
