package engine.save.room.type1;

import my.util.geometry.IPoint;

public class RoomEntities {
	public static class Entity {
		IPoint pos;
		Type type;

	}

	public static enum Type {
		console;
	}

}
