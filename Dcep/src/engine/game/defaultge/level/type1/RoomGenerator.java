package engine.game.defaultge.level.type1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import engine.physic.basic2DInteraction.InteractionObserver;
import engine.physic.basic2DvectorialV2.ISegment;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.physic.basic2DInteraction.InteractionEngine.InteractionLayer;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.Rectangle;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import my.util.CardinalDirection;

public final class RoomGenerator extends Room {

	private RoomGenerator(int offsetx, int offsety, ArrayList<RoomState> pool, DoorType[] doors) {
		super(offsetx, offsety, pool, doors);
	}

	public static void genRoom(Room r, ArrayList<RoomState> pool, DoorType[] doors) {
		//ArrayList<StillBox> nroom = new ArrayList<>();
		ArrayList<RoomState> rooms = pool;// RoomPool.pool.get(doors[0], doors[1], doors[2], doors[3]);
		if (rooms.size() == 0) {
			throw new RuntimeException("pool vide");
		}
		Random rand = new Random(System.currentTimeMillis());
		RoomState roomstate = rooms.get(rand.nextInt(rooms.size()));
//		for (int[] prebox : roomstate.getBoxes()) {
//			StillBox box = new StillBox(prebox[0], prebox[1], prebox[2], prebox[3], true);
//			nroom.add(box);
//			// int width, height;
//			// width = box.getX2() - box.getX();
//			// height = box.getY2() - box.getY();
//			// Rectangle rec = new Rectangle(box.getX(), box.getY(), width, height,
//			// Color.black);
//			// r.scene.addRenderable(rec, DrawLayer.Room_Walls);
//		}

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
				r.scene.addRenderable(rec, DrawLayer.Room_Walls);
				// ndoors.add();
				String[] infos = new String[] { "door", door.side.toString() };
				InteractionObserver obs = new InteractionObserver(r::doorObsTrigger, x, y, wi, he, infos);
				r.inter.add(InteractionLayer.action, obs);
			} else {
				Rectangle rec = new Rectangle(x, y, wi, he, Color.cyan);
				r.scene.addRenderable(rec, DrawLayer.Room_Walls);
			}
		}
		r.state = roomstate;
		r.physic.setWalls(roomstate.walls.get(69));
		for(Entry<CardinalDirection,ArrayList<ISegment>> entry:r.physic.getWalls().entrySet()) {
			entry.getValue().addAll(roomstate.walls.get(68).get(entry.getKey()));
		}
		//r.physic.getBoxes().addAll(nroom);
		// r.physic.getWalls().addAll(nroom);

	}
}
