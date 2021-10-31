package engine.physic.basic2Dvectorial.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import engine.save.room.type1.RoomState.Tile;
import engine.save.room.type1.RoomState.Tile.Neighbor;
import my.util.Log;
import my.util.geometry.IPoint;
import my.util.geometry.IRectangle;

public class PathFinder {
	protected ArrayList<Tile> navmesh;

	public PathFinder(ArrayList<Tile> nnavmesh) {
		navmesh = nnavmesh;
	}

	public Tile getTile(IPoint pt) {
		for (Tile tile : navmesh) {
			if (tile.isContaining(pt)) {
				return tile;
			}
		}
		return null;
	}

	public Path getPathFromTo(IPoint from, IPoint to, IRectangle rec) {
		Tile start = getTile(from);
		Tile end = getTile(to);
		if (end == null || start == null) {
			// Log.log("pfder", "destination " + end + " ou depart" + start + "
			// inacessible");
			return null;
		}
		if (start == end) {
			return new Path(new ArrayList<Neighbor>(), start, to);
		}

		int[] sonar = new int[navmesh.size()];
		Arrays.fill(sonar, 9999);
		ArrayList<Tile> edges = new ArrayList<>();
		edges.add(end);
		sonar[end.index] = 0;
		boolean done = false;
		while (!done) {
			ArrayList<Tile> nexts = new ArrayList<>();
			for (Tile edge : edges) {
				for (Neighbor nb : edge.neighbors) {
					if (sonar[nb.next.index] > sonar[edge.index] + 1 && nb.junction.isFittingThrough(rec)) {
						sonar[nb.next.index] = sonar[edge.index] + 1;
						nexts.add(nb.next);
						if (nb.next.equals(start)) {
							done = true;
							// Log.log("pfder", "step1 finie");
						}
					}
				}
			}
			edges = nexts;
			if (nexts.size() == 0) {
				// si le sonar n'a pas reussit a atteindre start
				// aucun path n'est possible;
				Log.log("pfder", "pas de chemin dispo");
				return null;
			}
		}

		ArrayList<Neighbor> nbs = new ArrayList<>();
		Tile last = start;
		done = false;
		int max = 0;
		while (!done) {
			Neighbor next = null;
			for (Neighbor nb : last.neighbors) {
				if (next == null || sonar[last.index] > sonar[nb.next.index]) {
					next = nb;
				}
			}
			nbs.add(next);
			last = next.next;
			// teste si atteind la destination
			done = sonar[next.next.index] == 0;
			// TODO en vrai, faut empecher une boucle infinie
			max++;
			if (max > 100) {
				done = true;
				Log.log("pfder", "probleme avec le remontage du chemin");
				return null;
			}
		}
		Path rtn = new Path(nbs, start, to);
		if (!start.isContaining(rec) && start.canContain(rec)) {
			rtn.current = -1;
		}
		return rtn;
	}

	public boolean isAccessible(IPoint pt) {
		return getTile(pt) != null;
	}

	public IPoint getRandomPoint(IRectangle rec) {
		Random rand = new Random();
		int r1 = rand.nextInt(navmesh.size());
		Tile nt = navmesh.get(r1);
		int px = rand.nextInt(nt.getWidth()) + nt.getX();
		int py = rand.nextInt(nt.getHeight()) + nt.getY();
		return new IPoint.Point(px, py);
	}

	public IPoint getRandomAccessiblePoint(IRectangle rec) {
		Random rand = new Random();
		int r1 = rand.nextInt(navmesh.size());
		Tile nt = navmesh.get(r1);
		int bx, by, nnx, nny;
		bx = nt.getWidth() - rec.getWidth() - 1;
		by = nt.getHeight() - rec.getHeight() - 1;
		if (bx <= 0) {
			return getRandomAccessiblePoint(rec);
		} else {
			nnx = rand.nextInt(bx) + nt.getX() + rec.getWidth() / 2;
		}
		if (by <= 0) {
			return getRandomAccessiblePoint(rec);
		} else {
			nny = rand.nextInt(by) + nt.getY() + rec.getHeight() / 2;
		}
		return new IPoint.Point(nnx, nny);
	}
}
