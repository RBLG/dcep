package engine.physic.basic2Dvectorial.pathfinding;

import java.util.ArrayList;

import engine.save.room.type1.RoomState.Tile;
import engine.save.room.type1.RoomState.Tile.Junction;
import engine.save.room.type1.RoomState.Tile.Neighbor;
import my.util.Log;
import my.util.geometry.IPoint;
import my.util.geometry.IRectangle;
import my.util.geometry.IVector;
import my.util.geometry.ISegment;

public class Path {
	protected int current;
	protected ArrayList<Neighbor> path;
	protected IPoint endpoint;
	// protected IPoint origin;
	protected Tile origin;
	protected boolean abort = false;
	protected IPoint antistucklastpos = null;

	public Path(ArrayList<Neighbor> npath, Tile nori, IPoint nend) {
		path = npath;
		origin = nori;
		endpoint = nend;
	}

	public IPoint getStepGoal(int step) {
		if (step < 0) {
			return origin.getCenter();
		} else if (step < path.size()) {
			return path.get(step).junction.getCenter();
		} else {
			return endpoint;
		}
	}

	public IVector getCurrentVector(IRectangle rec) {
		IPoint pos = rec.getCenter();
		if (current < 0) {
			return pos.getVectorTo(getStepGoal(current));
		} else if (current < path.size()) {
			Neighbor nb = path.get(current);
			Junction jc = nb.junction;

			IPoint nextgoal = getStepGoal(current + 1);
			if (pos.Equals(nextgoal)) {
				return new IVector.Vector(0, 0);
			}

			int ofx = 0, ofy = 0;
			if (jc.getXX().isContaining(rec.getXX()) && !jc.isAVerticalLine()) {
				ofy = +rec.getHeight() * nb.diry;
			} else {
				ofy = -rec.getHeight() / 2 * nb.diry;
			}
			if (jc.getYY().isContaining(rec.getYY()) && !jc.isAnHorizontalLine()) {
				ofx = +rec.getWidth() * nb.dirx;
			} else {
				ofx = -rec.getWidth() / 2 * nb.dirx;
			}

			nextgoal = nextgoal.getTranslated(ofx, ofy);
			ISegment vecseg = new ISegment.Segment(pos, nextgoal);
			ISegment realdoor = jc.door.getMargined(rec.getWidth(), rec.getHeight()).getDisplaced(ofx, ofy);

			return pos.getVectorTo(vecseg.getClosestPointFromIntersectionOnSegment(realdoor));
		} else {
			return pos.getVectorTo(endpoint);
		}
	}

	public IVector getShortTermVector(IRectangle rec, int speed) {
		IVector vec = getCurrentVector(rec);
		if (speed == 0 || ((vec.getX() == 0 && vec.getY() == 0))) {
			return new IVector.Vector(0, 0);
		} else if (Math.abs(vec.getX()) > speed || Math.abs(vec.getY()) > speed) {
			int signx, signy;
			signx = Integer.signum(vec.getX());
			signy = Integer.signum(vec.getY());

			// thalès en très réduit
			int x, y, max;
			x = Math.abs(vec.getX());
			y = Math.abs(vec.getY());
			max = Integer.max(x, y);
			return new IVector.Vector((speed * x / max) * signx, (speed * y / max) * signy);
		}
		return vec;
	}

	public boolean isStepDone(IRectangle rec) {
		if (current < 0) {
			return origin.isContaining(rec);
		} else if (current < path.size()) {
			Neighbor nb = path.get(current);
			Junction jc = nb.junction;
			if (!jc.flatx && !jc.flaty) {
				// Log.e("en nonflat: " + nb.dirx + "/" + nb.diry);
				return nb.next.isContaining(rec);
			} else if (jc.flatx) {
				// Log.e("en x: " + nb.dirx);
				if (nb.dirx > 0) {
					return rec.getXX().isBefore(jc.getXX());
				} else {
					return rec.getXX().isAfter(jc.getXX());
				}
			} else if (jc.flaty) {
				// Log.e("en y: " + nb.diry);
				if (nb.diry > 0) {
					return rec.getYY().isBefore(jc.getYY());
				} else {
					return rec.getYY().isAfter(jc.getYY());
				}
			}
			return true;
		} else {
			return getCurrentVector(rec).isZeroZero();
		}
	}

	public void moveOnToNextStep() {
		current++;
	}

	public boolean isDone(IRectangle rec) {
		// Log.e("done: " + (current > path.size()));
		return rec.isContaining(endpoint) || abort;
	}

	public void MoveToNextStepIfDone(IRectangle rec) {
		if (isStepDone(rec)) {
			moveOnToNextStep();
		}
	}

	public void checkStuckness(IPoint pt) {
		if (antistucklastpos != null && pt.Equals(antistucklastpos)) {
			abort = true;
			Log.log(this, "chemin coincé, annulation de l'objectif");
		} else {
			antistucklastpos = pt;
		}
	}

}
