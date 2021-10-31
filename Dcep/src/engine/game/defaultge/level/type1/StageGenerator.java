package engine.game.defaultge.level.type1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.Rectangle;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;

/***
 * générateur d'étage pour StageTop2DType1, la méthode fait quasi 100 lignes
 * 
 * @author RobinL
 *
 */
public final class StageGenerator extends StageType1 {
	public final static int roomperstage = 10;
	public final static int fsizex = 10;
	public final static int fsizey = 10;
	public final static int fcentx = fsizex / 2;
	public final static int fcenty = fsizey / 2;
	public final static int borderx = Room.rosizex * 3;
	public final static int bordery = Room.rosizey * 3;
	public final static int cyclex = Room.rosizex + borderx;
	public final static int cycley = Room.rosizey + bordery;
	public final static Color wallcolor = Color.black;

	private StageGenerator() {
		super(null);
	}

	/**
	 * génère la carte des salles d'un étage
	 * 
	 * @return
	 */
	public static Room[][] genFloor(StageType1 stage) {
		boolean[][] prefloor = new boolean[fsizex][fsizey];

		// génération de l'emplacement des salles
		Random rand = new Random(System.currentTimeMillis());
		ArrayList<int[]> coords = new ArrayList<int[]>();
		coords.add(new int[] { fcentx, fcenty });
		int slotsleft = roomperstage;
		while (slotsleft > 0) {
			int[] co = coords.get(rand.nextInt(coords.size()));
			int cx = co[0];
			int cy = co[1];
			ArrayList<int[]> toadd = new ArrayList<int[]>();

			if (!isOccupied(prefloor, cx + 1, cy)) { // east
				toadd.add(new int[] { cx + 1, cy });
			}
			if (!isOccupied(prefloor, cx - 1, cy)) { // west
				toadd.add(new int[] { cx - 1, cy });
			}
			if (!isOccupied(prefloor, cx, cy + 1)) { // south
				toadd.add(new int[] { cx, cy + 1 });
			}
			if (!isOccupied(prefloor, cx, cy - 1)) { // north
				toadd.add(new int[] { cx, cy - 1 });
			}

			int d100 = rand.nextInt(100);
			int check = toadd.size() * 50 - 92;
			if (d100 < check) {
				prefloor[cx][cy] = true;
				coords.remove(co);
				coords.addAll(toadd);
				slotsleft--;
			}
		}
		// génération de la carte des salles finale
		Room[][] nfloor = new Room[fsizex][fsizey];
		int spawnx = fcentx, spawny = fcenty;

		for (int itx = 0; itx < fsizex; itx++) {
			// murs épais sur l'axe des Y
			Rectangle rec2 = new Rectangle(itx * cyclex - borderx, 0, borderx, cycley * fsizey, wallcolor);
			stage.scene.addRenderable(rec2, DrawLayer.Game_Ceiling);
			for (int ity = 0; ity < fsizey; ity++) {
				// si il y a salle
				if (prefloor[itx][ity]) {
					DoorType[] dors = new DoorType[4];
					for (Side side : Side.values()) {
						boolean cond = isOccupied(prefloor, itx + side.toXMultiplier(), ity + side.toYMultiplier());
						dors[side.ordinal()] = cond ? DoorType.small_door : DoorType.wall;
					}
					DoorType n, s, e, w;
					// n = isOccupied(prefloor, itx, ity - 1) ? DoorType.small_door : DoorType.wall;
					// s = isOccupied(prefloor, itx, ity + 1) ? DoorType.small_door : DoorType.wall;
					// e = isOccupied(prefloor, itx + 1, ity) ? DoorType.small_door : DoorType.wall;
					// w = isOccupied(prefloor, itx - 1, ity) ? DoorType.small_door : DoorType.wall;
					n = dors[Side.north.ordinal()];
					s = dors[Side.south.ordinal()];
					e = dors[Side.east.ordinal()];
					w = dors[Side.west.ordinal()];

					Room rom;
					ArrayList<RoomState> pool;
					if (itx == spawnx && ity == spawny) { // TODO si spawn
						pool = RoomPool.pool.get(n, DoorType.any, e, w);
						rom = new Room(itx * cyclex, ity * cycley, pool, dors);
					} else {
						pool = RoomPool.pool.get(n, s, e, w);
						rom = new Room(itx * cyclex, ity * cycley, pool, dors);
					}

					nfloor[itx][ity] = rom;
					stage.scene.addRenderable(nfloor[itx][ity].getScene(), DrawLayer.Room_Walls);

				} else {
					// si c'est un mur
					Rectangle rec = new Rectangle(itx * cyclex, ity * cycley, Room.rosizex, Room.rosizey, wallcolor);
					stage.scene.addRenderable(rec, DrawLayer.Game_Ceiling);
				}
			}
		}
		// murs épais sur l'axe des Y
		for (int ity = 0; ity < fsizey; ity++) {
			Rectangle rec2 = new Rectangle(0, ity * cycley - bordery, cyclex * fsizex, bordery, wallcolor);
			stage.scene.addRenderable(rec2, DrawLayer.Game_Ceiling);
		}
		// print de la map dans la console
		for (int ity = 0; ity < fsizey; ity++) {
			for (int itx = 0; itx < fsizex; itx++) {
				if (prefloor[itx][ity]) {
					if (itx == spawnx && ity == spawny) {
						System.out.print("()");
					} else {
						System.out.print("[]");
					}
				} else {
					System.out.print("__");
				}
			}
			System.out.println();
		}
		return nfloor;
	}

	protected static boolean isOccupied(boolean[][] floor, int x, int y) {
		if (isInBound(floor, x, y)) {
			return (floor[x][y]);
		}
		return true;
	}

	protected static boolean isInBound(boolean[][] floor, int x, int y) {
		return (x >= 0 && x < floor.length && y >= 0 && y < floor[0].length);
	}

}
