package engine.game.defaultge.level.type1;

import java.util.ArrayList;
import java.util.EnumMap;

import engine.save.Constants;
import engine.save.room.type1.ITypeCriterias;
import engine.save.room.type1.RoomLoader;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import engine.save.room.type1.RoomState.Door;
import engine.save.room.type1.RoomType;

public class RoomPool {
	public static enum DoorType { // TODO pourrait etre relatif au type de niveau
		wall, any, small_door, double_door
	}

	public static class RList extends ArrayList<RoomState> {
		private static final long serialVersionUID = -1966631402389500577L;
	}

	// le nom veut dire type size
	public static final int tpsize = DoorType.values().length;
	public static final RoomPool pool = new RoomPool(); // TODO pourrait ne pas etre un singleton

	protected RList[][][][] sortedpool;//TODO remplacer par une enum de enum de enum (pareil mais mieux)
	protected EnumMap<RoomType, RList> specialpool;

	private RoomPool() {
		this.sortedpool = new RList[tpsize][tpsize][tpsize][tpsize];
		this.specialpool = new EnumMap<>(RoomType.class);
		this.iniet();
	}

	public static int ord(DoorType dt) {
		return dt.ordinal();
	}

	public ArrayList<RoomState> get(DoorType n, DoorType s, DoorType e, DoorType w) {
		RList rtn = sortedpool[ord(n)][ord(s)][ord(e)][ord(w)];
		if (rtn == null) {
			rtn = new RList();
		}
		return rtn;
	}

	public void add1(DoorType n, DoorType s, DoorType e, DoorType w, RoomState room) {
		RList list = sortedpool[ord(n)][ord(s)][ord(e)][ord(w)];
		if (list == null) {
			list = new RList();
			sortedpool[ord(n)][ord(s)][ord(e)][ord(w)] = list;
		}
		list.add(room);
	}

	public void add(DoorType n, DoorType s, DoorType e, DoorType w, RoomState room) {
		DoorType[] t = DoorType.values();
		this.iterate4D((ni, si, ei, wi) -> { // TODO a opti mais j'ai la flemme
			if (isAll4Compatible(n, s, e, w, t[ni], t[si], t[ei], t[wi])) {
				this.add1(t[ni], t[si], t[ei], t[wi], room);
			}
		});
	}

	protected boolean isAll4Compatible(DoorType n, DoorType s, DoorType e, DoorType w, DoorType ni, DoorType si,
			DoorType ei, DoorType wi) {
		return isCompatible(n, ni) && isCompatible(s, si) && isCompatible(e, ei) && isCompatible(w, wi);
	}

	protected boolean isCompatible(DoorType raw, DoorType ref) {
		boolean rtn = raw == ref;
		// si ref= wall -> tested peut etre n'imp
		rtn |= ref == DoorType.wall;
		// si ref= any -> tested peut etre n'imp sauf wall
		rtn |= (ref == DoorType.any) && raw != DoorType.wall;
		// si raw= any -> ref peut etre n'imp
		rtn |= raw == DoorType.any;

		// TODO devellopper les compatibilitées entre portes

		return rtn;
	}

	/***
	 * devrait probablement etre dans le constructeur mais bon je suis un con
	 */
	protected void iniet() {
		ArrayList<RoomState> rooms = RoomLoader.importRooms(Constants.stagetype1_rooms);
		for (RoomState room : rooms) {
			EnumMap<Side, Door> ds = room.doors;
			// TODO implementer les criterias
			ITypeCriterias crit = (a, b) -> DoorType.any;
			DoorType n, s, e, w;
			// pourrait etre réduit je pense
			n = (ds.get(Side.north) != null) ? ds.get(Side.north).getType(crit) : DoorType.wall;
			s = (ds.get(Side.south) != null) ? ds.get(Side.south).getType(crit) : DoorType.wall;
			e = (ds.get(Side.east) != null) ? ds.get(Side.east).getType(crit) : DoorType.wall;
			w = (ds.get(Side.west) != null) ? ds.get(Side.west).getType(crit) : DoorType.wall;
			if (room.type == RoomType.room) {
				this.add(n, s, e, w, room);
			} else {
				specialpool.putIfAbsent(room.type, new RList());
				specialpool.get(room.type).add(room);
			}
		}
		//this.iterate4D((a, b, c, d) -> System.out.println(sortedpool[a][b][c][d]));
	}

	public ArrayList<RoomState> getSpecialPool(RoomType type) {
		return this.specialpool.getOrDefault(type, new RList());
	}

	public void iterate4D(QuadIntConsumer cons) {
		for (DoorType ni : DoorType.values()) {
			for (DoorType si : DoorType.values()) {
				for (DoorType ei : DoorType.values()) {
					for (DoorType wi : DoorType.values()) {
						cons.accept(ord(ni), ord(si), ord(ei), ord(wi));
					}
				}
			}
		}
	}

	public static interface QuadIntConsumer {
		public void accept(int a, int b, int c, int d);
	}

}
