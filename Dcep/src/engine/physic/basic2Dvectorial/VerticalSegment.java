package engine.physic.basic2Dvectorial;

import engine.physic.basic2DvectorialV2.ISegment;
import my.util.CardinalDirection;
import my.util.Geometry;

public class VerticalSegment implements ISegment {
	private static final long serialVersionUID = -6013441325763722423L;

	protected int x;
	protected int y1;
	protected int y2;
	protected int color;

	public VerticalSegment(int nx, int ny1, int ny2, int ncolor) {
		this.x = nx;
		this.y1 = ny1;
		this.y2 = ny2;
		this.color = ncolor;

	}

	/***
	 * vérifie s'il y a intersection
	 * 
	 * @param pt
	 * @param pt2
	 * @return
	 */
	public boolean isIntersecting(FinalPoint pt, FinalPoint pt2) {
		Point p1 = new Point(x, y1);
		Point p2 = new Point(x, y2);
		boolean e1 = Misc.crossProdBool(pt, pt2, p1) ^ Misc.crossProdBool(pt, pt2, p2);
		boolean e2 = (pt.getX() > x) ^ (pt2.getX() > x);

		return e1 && e2;
	}

	/***
	 * détermine le point d'impact et modifie le vecteur selon le résultat
	 * 
	 * @param pt
	 * @param vec
	 * @return si besoin de recheck
	 */
	@Deprecated
	public boolean impact(FinalPoint pt, MotionVector vec) {
		Point p1 = new Point(x, y1);
		Point p2 = new Point(x, y2);
		FinalPoint pt2 = new FinalPoint(pt.getX() + vec.getX(), pt.getY() + vec.getY());
		int prod1 = Misc.crossProduct(pt, pt2, p1);
		int prod2 = Misc.crossProduct(pt, pt2, p2);
		boolean crossing1;

		if ((prod1 == 0 || prod2 == 0) && vec.getY() != 0) {// teste si a un bord
			crossing1 = true;
		} else {
			// superieur ou egal afin qu'il y est impact si le point d'origine est sur la
			// ligne
			crossing1 = prod1 * prod2 < 0; // XOR de signe ( +*+=+, -*+=- -*-=+ )
		}

		int prod3 = Misc.flatCrossProduct(x, pt.getX());
		int prod4 = Misc.flatCrossProduct(x, pt2.getX());
		boolean crossing2 = (prod3 * prod4 < 0) || (prod3 == 0); // ^ -> XOR booléen
		// boolean crossing2 = (y <= pt.getY()) ^ (y <= pt2.getY());

		boolean intersect = crossing1 && crossing2;

		boolean rtn = intersect;
		if (intersect) { // produit en croix classique pour trouver le nouveau Y
			int total = prod4 - prod3;
			if (prod3 != 0) {
				vec.setY(((Geometry.abs(prod3) * vec.getY()) / Geometry.abs(total)));
			}
			vec.setX(((Geometry.abs(prod3) * vec.getX()) / Geometry.abs(total)));
		}
		return rtn;
	}

	@Override
	public boolean impact(FinalPoint pt, FinalPoint pt2, MotionVector vec, CardinalDirection dir) {

		// realx: si il faut enlever la taille de l'entité pour calc la collision
		// (en fonction du sens de vec)
		int realx = (dir == CardinalDirection.west) ? x + 1 : x - pt2.x;
		int realy1 = (vec.getY() < 0) ? y1 + 1 : y1;
		int realy2 = (vec.getY() < 0) ? y2 + 1 : y2;
		// sec = (wall.normal) ? 0 : -100; //TODO implementer si besoin
		Point p1 = new Point(realx, realy1 - pt2.y);
		Point p2 = new Point(realx, realy2);
		Point endpt = new Point(pt.getX() + vec.getX(), pt.getY() + vec.getY());
		int prod1 = Misc.crossProduct(pt, endpt, p1);
		int prod2 = Misc.crossProduct(pt, endpt, p2);
		boolean crossing1;

		if ((prod1 == 0 || prod2 == 0) && vec.getY() != 0) {// teste si a un bord
			crossing1 = true;
		} else {
			// superieur ou egal afin qu'il y est impact si le point d'origine est sur la
			// ligne
			crossing1 = prod1 * prod2 < 0; // XOR de signe ( +*+=+, -*+=- -*-=+ )
		}

		int prod3 = Misc.flatCrossProduct(realx, pt.getX());
		int prod4 = Misc.flatCrossProduct(realx, endpt.getX());
		boolean crossing2 = (prod3 * prod4 < 0) || (prod3 == 0); // ^ -> XOR booléen
		// boolean crossing2 = (y <= pt.getY()) ^ (y <= pt2.getY());

		boolean intersect = crossing1 && crossing2;

		if (intersect) { // produit en croix classique pour trouver le nouveau Y
			int total = prod4 - prod3;
			if (prod3 != 0) {
				vec.setY(((Geometry.abs(prod3) * vec.getY()) / Geometry.abs(total)));
			}
			vec.setX(((Geometry.abs(prod3) * vec.getX()) / Geometry.abs(total)));
			if (vec.getX() == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y1;
	}

	@Override
	public int getX2() {
		return x;
	}

	@Override
	public int getY2() {
		return y2;
	}

	@Override
	public int getColor() {
		return color;
	}

}
