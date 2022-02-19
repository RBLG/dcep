package engine.save.room.type1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.physic.basic2Dvectorial.ISegment;
import engine.physic.basic2Dvectorial.pathfinding.format.Junction;
import engine.physic.basic2Dvectorial.pathfinding.format.Tile;
import my.util.Cardinal;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle.Rectangle;

public class RoomState implements Serializable {

	private static final long serialVersionUID = 3636697824805539964L;

	// public final int[][] boxes;
	public final HashMap<Integer, EnumMap<Cardinal, ArrayList<ISegment>>> walls;
	// public final Door[] doors;
	public final EnumMap<Side, Door> doors;
	public final ArrayList<Tile> navmesh; // chemins que les entités peuvent suivre
	public final ArrayList<Junction> navmjunctions;
	public final RoomType type;
	public final ArrayList<WallSlice> wallslices;
	public final String visconfpath;

	public RoomState(//
			HashMap<Integer, EnumMap<Cardinal, ArrayList<ISegment>>> nwalls, EnumMap<Side, Door> ndoors, //
			ArrayList<Tile> nnavmesh, //
			ArrayList<Junction> njunctions, //
			RoomType ntype, //
			ArrayList<WallSlice> nwallslices, //
			String nvisconfpath //
	) {
		// boxes = nboxes;
		walls = nwalls;
		doors = ndoors;
		navmesh = nnavmesh;
		navmjunctions = njunctions;
		type = ntype;
		wallslices = nwallslices;
		visconfpath = nvisconfpath;
	}

	public EnumMap<Side, Door> getDoors() {
		return doors;
	}

	public Point getDoorFront(Cardinal dir, IPoint wh) {
		RoomState.Door entrydoor = null;
		for (RoomState.Door door : doors.values()) {
			if (door.side.toOposite().toCardinal() == dir) {
				entrydoor = door;
			}
		}
		if (entrydoor == null) {// ne devrait jamais arriver
			entrydoor = new RoomState.Door(Side.north, 10, 50);
		}
		// TODO passer de side a cardinal pour simplifier la recup de porte
		// entrydoor = doors.get(dir.toOposite());

		return entrydoor.getFront(wh);
	}

	/////////////////////////////////

	public static class Door implements Serializable {

		private static final long serialVersionUID = -4542603376754860735L;

		public Side side;
		public int pos;
		public int size;

		public Door(Side nside, int npos, int nsize) {
			this.side = nside;
			this.pos = npos;
			this.size = nsize;
		}

		public Rectangle getZone() {
			Cardinal dir = side.toCardinal();
			int sided;
			if (dir.isHorizontal()) {
				sided = !dir.isPositiveOnItAxis() ? 0 : Room.rosizex;
				return new Rectangle(sided, pos, sided, pos + size);
			} else {
				sided = !dir.isPositiveOnItAxis() ? 0 : Room.rosizey;
				return new Rectangle(pos, sided, pos + size, sided);
			}
		}

		public Point getFront(IPoint wh) {
			int nx = 0, ny = 0;
			Cardinal dir = side.toCardinal();
			if (dir.isHorizontal()) {
				nx = !dir.isPositiveOnItAxis() ? 1 : Room.rosizex - wh.getX() - 1;
				ny = pos + (size - wh.getY()) / 2;
			} else {
				nx = pos + (size - wh.getX()) / 2;
				ny = !dir.isPositiveOnItAxis() ? 1 : Room.rosizey - wh.getY() - 1;
			}
			return new Point(nx, ny);
		}

		// TODO generaliser (<ENUM> a la place de DoorType + <ENUM> après typecrit)
		public DoorType getType(ITypeCriterias crit) {
			return crit.judgeDoorSize(side, size);
		}
	}

}
