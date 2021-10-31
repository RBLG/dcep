package engine.save.room.type1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.physic.basic2DvectorialV2.ISegment;
import my.util.CardinalDirection;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;
import my.util.geometry.IVector;

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

	public static class Tile extends IRectangle.Rectangle implements Serializable {
		private static final long serialVersionUID = 7444722321657363476L;
		public ArrayList<Neighbor> neighbors = new ArrayList<>();
		public int index = 0;

		public Tile(int nx, int ny, int nx2, int ny2) {
			super(nx, ny, nx2, ny2);
			if (nx > nx2 || ny > ny2) {
				throw new RuntimeException("taile negative x" + nx + "!x2:" + nx2 + "!y:" + ny + "!y2:" + ny2);
			}
		}

		public static class Neighbor implements Serializable {
			private static final long serialVersionUID = 4186735999298001513L;
			public final Tile next;
			public final Tile last;
			public final Junction junction;
			public final int dirx, diry;

			public Neighbor(Tile nlast, Tile nnext, Junction njc) {
				last = nlast;
				next = nnext;
				junction = njc;
				IPoint jpt = njc.getCenter(), npt = next.getCenter();
				// * height/width pour nulifier si y a pas de hauteur/largeur
				dirx = Integer.signum((-jpt.getX() + npt.getX()) * njc.getHeight());
				diry = Integer.signum((-jpt.getY() + npt.getY()) * njc.getWidth());
			}
		}

		public static class Junction extends IRectangle.Rectangle implements Serializable {
			private static final long serialVersionUID = -8677821632484080852L;
			public final boolean flatx, flaty;
			public final my.util.geometry.ISegment door;

			public Junction(IRectangle njunc, IVector dir) {
				super(njunc.getX(), njunc.getY(), njunc.getX2(), njunc.getY2());
				flatx = x == x2;
				flaty = y == y2;
				door = doDoor(dir);
			}

			private my.util.geometry.ISegment doDoor(IVector dir) {
				// xor pour voir si dir est ++/-- ou +-/-+
				if (dir.getX() > 0 ^ dir.getY() > 0) { // +-/-+
					return new my.util.geometry.ISegment.Segment(getXY(), getX2Y2());
				} else { // ++/--
					return new my.util.geometry.ISegment.Segment(getX2Y(), getXY2());
				}
			}

			public IVector getCenteredApproachVector(IRectangle rec) {
				return rec.getCenter().getVectorTo(getCenter());
			}

			public int getPassageSize() {
				return this.getHypothenuseSize();
			}

			public boolean isFittingThrough(IRectangle rec) {
				return (getHeight() > rec.getHeight() || getWidth() > rec.getWidth());
			}

		}
	}

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
