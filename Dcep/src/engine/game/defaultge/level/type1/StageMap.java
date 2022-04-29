package engine.game.defaultge.level.type1;

import java.util.ArrayList;
import java.util.Arrays;

import my.util.Cardinal;
import my.util.MapHelper;
import my.util.Sign;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;

public class StageMap {

	protected Room[][] floor;
	public Point current = new Point(StageGenerator.fcentx, StageGenerator.fcenty);
	public ArrayList<IPoint> rooms = new ArrayList<>();

	public StageMap(Room[][] nfloor, int rmamount) {
		floor = nfloor;
		int ity = 0;
		for (Room[] row : floor) {
			int itx = 0;
			for (Room room : row) {
				if (room != null) {
					rooms.add(new Point(itx, ity));
				}
				itx++;
			}
			ity++;
		}

	}

	public Room getCurrent() {
		return floor[current.x][current.y];
	}

	public void setCurrent(int nx, int ny) {
		current.set(nx, ny);
	}

	public void move(Sign nx, Sign ny) {
		current.x += nx.val;
		current.y += ny.val;
	}

	public int getRoomAmount() {
		return rooms.size();
	}

	/***
	 * fait le sonar entier, a verifier si la salle de départ est reliée
	 * (normalement oui après vu la methode de generation)
	 * 
	 * @return
	 */
	protected int[][] getSonar(IPoint end) {
		if (!MapHelper.isIn(floor, end)) {
			return null;
		}
		int[][] sonar = getIntFormat();
		for (int[] sona : sonar) {
			Arrays.fill(sona, Integer.MAX_VALUE);
		}
		sonar[end.getX()][end.getY()] = 0;
		ArrayList<IPoint> edges = new ArrayList<>();
		edges.add(end);
		while (edges.size() != 0) {
			ArrayList<IPoint> nexts = new ArrayList<>();
			for (IPoint edge : edges) {
				int distance = sonar[edge.getX()][edge.getY()] + 1;
				getSonarSub(sonar, nexts, edge.getX() + 1, edge.getY(), distance);
				getSonarSub(sonar, nexts, edge.getX(), edge.getY() + 1, distance);
				getSonarSub(sonar, nexts, edge.getX() - 1, edge.getY(), distance);
				getSonarSub(sonar, nexts, edge.getX(), edge.getY() - 1, distance);
			}
			edges = nexts;
		}
		return sonar;
	}

	private void getSonarSub(int[][] sonar, ArrayList<IPoint> nexts, int tx, int ty, int distance) {
		if (getIfIn(sonar, tx, ty) > distance && MapHelper.getIfIn(floor, tx, ty) != null) {
			sonar[tx][ty] = distance;
			nexts.add(new IPoint.Point(tx, ty));
		}
	}

	private static int getIfIn(int[][] array, IPoint pos) {
		return getIfIn(array, pos.getX(), pos.getY());
	}

	private static int getIfIn(int[][] array, int x, int y) {
		// pas besoin de test si len=0 parce que soit xy<0 soit 0<=xy
		if (x < 0 || y < 0 || array.length <= x || array[0].length <= y) {
			return Integer.MAX_VALUE;
		}
		return array[x][y];
	}

	public StagePath getPath(IPoint start, IPoint end) {
		ArrayList<Cardinal> path = new ArrayList<>();
		if (start.Equals(end)) {
			return new StagePath(path);
		}
		int[][] sonar = getSonar(end);
		if (!(getIfIn(sonar, start) != Integer.MAX_VALUE)) {
			return null;
		}
		IPoint last = start;
		while (!last.Equals(end)) {
			int min = Integer.MAX_VALUE;
			IPoint next = null;
			Cardinal ndir = null;
			for (Cardinal dir : Cardinal.values()) {
				IPoint pt = dir.getAdjacent(last);
				int val = getIfIn(sonar, pt);
				if (val < min) {
					min = val;
					next = pt;
					ndir = dir;
				}
			}
			last = next;
			path.add(ndir);
		}
		return new StagePath(path);
	}

	public int[][] getIntFormat() {
		return new int[floor.length][floor[0].length];
	}

	public Room getRoom(IPoint pt) {
		return floor[pt.getX()][pt.getY()];
	}

	public Room get(IPoint pt) {
		return floor[pt.getX()][pt.getY()];
	}

	public Point getPos(Room nroom) {
		int ity = 0;
		for (Room[] row : floor) {
			int itx = 0;
			for (Room room : row) {
				if (room != null) {
					if (room.equals(nroom)) {
						return new Point(itx, ity);
					}
				}
				itx++;
			}
			ity++;
		}
		return null;// arrive jamais
	}
}
