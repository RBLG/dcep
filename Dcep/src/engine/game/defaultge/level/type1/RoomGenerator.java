package engine.game.defaultge.level.type1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.game.defaultge.level.type1.interactions.DoorInteractable;
import engine.physic.basic2Dvectorial.ISegment;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.Rectangle;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import my.util.Cardinal;

public final class RoomGenerator extends Room {

	private RoomGenerator(int offsetx, int offsety, ArrayList<RoomState> pool, DoorType[] doors) {
		super(offsetx, offsety, pool, doors);
	}

	public static void genRoom(Room r, ArrayList<RoomState> pool, DoorType[] doors) {
		ArrayList<RoomState> rooms = pool;
		if (rooms.size() == 0) {
			throw new RuntimeException("pool vide");
		}
		Random rand = new Random(System.currentTimeMillis());
		RoomState roomstate = rooms.get(rand.nextInt(rooms.size()));

		r.doors = roomstate.getDoors();
		for (RoomState.Door door : roomstate.getDoors().values()) {
			int x = 0, y = 0, wi = 0, he = 0;
			boolean open = false;
			int drlen = 4; // épaisseur de la porte

			if (door.side.isHorizontal()) {
				x = (door.side == Side.east) ? Room.rosizex - drlen : 0;
				wi = drlen;
				y = door.pos;
				he = door.size;
			} else {
				x = door.pos;
				wi = door.size;
				y = (door.side == Side.north) ? 0 : Room.rosizey - drlen;
				he = drlen;
			}
			open = doors[door.side.ordinal()] != DoorType.wall;
			if (open) {
				Rectangle rec = new Rectangle(x, y, wi, he, Color.blue);
				r.scene.add(rec, DrawLayer.Room_Walls);
				r.interactables.getInteractables().add(new DoorInteractable(door, x, y, wi, he));
			} else {
				Rectangle rec = new Rectangle(x, y, wi, he, Color.cyan);
				r.scene.add(rec, DrawLayer.Room_Walls);
			}
		}
		r.state = roomstate;
		r.walls = roomstate.walls.get(69);
		for (Entry<Cardinal, ArrayList<ISegment>> entry : r.walls.entrySet()) {
			entry.getValue().addAll(roomstate.walls.get(68).get(entry.getKey()));
		}
	}
}
