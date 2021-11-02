package engine.save.room.type1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.physic.basic2Dvectorial.ISegment;
import engine.physic.basic2Dvectorial.pathfinding.Tile;
import my.util.CardinalDirection;
import my.util.geometry.IPoint.Point;

public class RoomState implements Serializable {

	private static final long serialVersionUID = 3636697824805539964L;

	// public final int[][] boxes;
	public final HashMap<Integer, EnumMap<CardinalDirection, ArrayList<ISegment>>> walls;
	// public final Door[] doors;
	public final EnumMap<Side, Door> doors;
	public final ArrayList<Tile> navmesh; // chemins que les entités peuvent suivre
	public final RoomType type;
	public final ArrayList<WallSlice> wallslices;
	public final String visconfpath;

	public RoomState(//
			HashMap<Integer, EnumMap<CardinalDirection, ArrayList<ISegment>>> nwalls, EnumMap<Side, Door> ndoors, //
			ArrayList<Tile> nnavmesh, //
			RoomType ntype, //
			ArrayList<WallSlice> nwallslices, //
			String nvisconfpath //
	) {
		// boxes = nboxes;
		walls = nwalls;
		doors = ndoors;
		navmesh = nnavmesh;
		type = ntype;
		wallslices = nwallslices;
		visconfpath = nvisconfpath;
	}

	public EnumMap<Side, Door> getDoors() {
		return doors;
	}

	public Point getDoorFront(CardinalDirection dir, int ewidth, int eheight) {
		RoomState.Door entrydoor = null;
		for (RoomState.Door door : doors.values()) {
			if (door.side.toOposite().toCardinal() == dir) {
				entrydoor = door;
			}
		}
		if (entrydoor == null) {// ne devrait jamais arriver
			entrydoor = new RoomState.Door(Side.north, 10, 50);
		}

		int nx = 0, ny = 0;
		if (dir.isHorizontal()) {
			nx = (dir == CardinalDirection.east) ? 1 : Room.rosizex - eheight - 1;
			ny = entrydoor.pos + (entrydoor.size - eheight) / 2;
		} else {
			nx = entrydoor.pos + (entrydoor.size - ewidth) / 2;
			ny = (dir == CardinalDirection.north) ? Room.rosizey - ewidth - 1 : 1;
		}
		return new Point(nx, ny);
	}

	/////////////////////////////////

	public static class Door implements Serializable {

		private static final long serialVersionUID = -4542603376754860735L;

		public Side side;
		public int pos;
		public int size;
		public int index;
		// public DoorType type;

		public Door(Side nside, int npos, int nsize) {
			this.side = nside;
			this.pos = npos;
			this.size = nsize;
		}

		public DoorType getType(ITypeCriterias crit) {
			return crit.judgeDoorSize(side, size);
		}
	}

}
