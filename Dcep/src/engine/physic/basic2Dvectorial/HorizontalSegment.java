package engine.physic.basic2Dvectorial;

import engine.physic.basic2DvectorialV2.ISegment;
import my.util.CardinalDirection;
import my.util.Geometry;
import my.util.Log;

public class HorizontalSegment implements ISegment {
	private static final long serialVersionUID = -191337718650331987L;

	protected int x1;
	protected int x2;
	protected int y;
	protected int color;

	public HorizontalSegment(int nx1, int nx2, int ny, int ncolor) {
		this.x1 = nx1;
		this.x2 = nx2;
		this.y = ny;
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
		Point p1 = new Point(x1, y);
		Point p2 = new Point(x2, y);
		boolean e1 = Misc.crossProdBool(pt, pt2, p1) ^ Misc.crossProdBool(pt, pt2, p2);
		boolean e2 = (pt.getY() > y) ^ (pt2.getY() > y);

		return e1 && e2;
	}

	/***
	 * détermine le point d'impact et modifie le vecteur selon le résultat
	 * 
	 * @param pt
	 * @param vec le vecteur a MODIFIER /!\ <br/>
	 */
	@Deprecated
	public boolean impact(FinalPoint pt, MotionVector vec) { // TODO regler le duplicate de
		// vertical
		Point p1 = new Point(x1, y);
		Point p2 = new Point(x2, y);
		FinalPoint pt2 = new FinalPoint(pt.getX() + vec.getX(), pt.getY() + vec.getY());
		int prod1 = Misc.crossProduct(pt, pt2, p1);
		int prod2 = Misc.crossProduct(pt, pt2, p2);
		boolean crossing1;

		if ((prod1 == 0 || prod2 == 0) && vec.getX() != 0) {// teste si a un bord
			crossing1 = true;
		} else {
			// superieur ou egal afin que y est impact si le point d'origine est sur la
			// ligne
			crossing1 = prod1 * prod2 < 0; // XOR de signe ( +*+=+, -*+=- -*-=+ )
		}

		int prod3 = Misc.flatCrossProduct(y, pt.getY());
		int prod4 = Misc.flatCrossProduct(y, pt2.getY());
		boolean crossing2 = (prod3 * prod4 < 0) || (prod3 == 0); // ^ -> XOR booléen
		// boolean crossing2 = (y <= pt.getY()) ^ (y <= pt2.getY());

		boolean intersect = crossing1 && crossing2;
		boolean rtn = intersect;

		if (intersect) { // produit en croix classique pour trouver le nouveau Y
			int total = prod4 - prod3;
			if (prod3 != 0) {
				vec.setX(((Geometry.abs(prod3) * vec.getX()) / Geometry.abs(total)));
			}
			vec.setY(((Geometry.abs(prod3) * vec.getY()) / Geometry.abs(total)));
		}
		return rtn;
	}

	// TODO regler le duplicate de vertical
	@Override
	public boolean impact(FinalPoint pt, FinalPoint pt2, MotionVector vec, CardinalDirection dir) {

		int realy = (dir == CardinalDirection.north) ? y + 1 : y - pt2.y;
		int realx1 = (vec.getX() < 0) ? x1 + 1 : x1;
		int realx2 = (vec.getX() < 0) ? x2 + 1 : x2;

		Point p1 = new Point(realx1 - pt2.x, realy);
		Point p2 = new Point(realx2, realy);
		// pt
		// ..\
		// ...\
		// p1=======p2
		// .....\
		// .....pt2
		FinalPoint endpt = new FinalPoint(pt.getX() + vec.getX(), pt.getY() + vec.getY());
		int prod1 = Misc.crossProduct(pt, endpt, p1);
		int prod2 = Misc.crossProduct(pt, endpt, p2);
		boolean crossing1;

		if ((prod1 == 0 || prod2 == 0) && vec.getX() != 0) {// teste si a un bord
			crossing1 = true;
		} else {
			// superieur ou egal afin que y est impact si le point d'origine est sur la
			// ligne
			crossing1 = prod1 * prod2 < 0; // XOR de signe ( +*+=+, -*+=- -*-=+ )
		}

		int prod3 = Misc.flatCrossProduct(realy, pt.getY());
		int prod4 = Misc.flatCrossProduct(realy, endpt.getY());
		boolean crossing2 = (prod3 * prod4 < 0) || (prod3 == 0); // ^ -> XOR booléen
		// boolean crossing2 = (y <= pt.getY()) ^ (y <= pt2.getY());

		boolean intersect = crossing1 && crossing2;
		if (intersect) { // produit en croix classique pour trouver le nouveau Y
			int total = prod4 - prod3;
			if (total == 0) {
				Log.log(this, "total=0");
			}
			if (prod3 != 0) {
				vec.setX(((Geometry.abs(prod3) * vec.getX()) / Geometry.abs(total)));
			}
			vec.setY(((Geometry.abs(prod3) * vec.getY()) / Geometry.abs(total)));
			if (vec.getY() == 0) {
				return true;
			}
		}
		return false;// parce que Y n'était pas 0
	}

	@Override
	public int getX() {
		return x1;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getX2() {
		return x2;
	}

	@Override
	public int getY2() {
		return y;
	}

	@Override
	public int getColor() {
		return color;
	}
}