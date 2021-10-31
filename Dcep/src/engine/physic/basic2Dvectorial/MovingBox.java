package engine.physic.basic2Dvectorial;

import java.util.ArrayList;
import java.util.function.Consumer;

import engine.physic.basic2Dvectorial.motionprovider.IMotionProvider;
import engine.physic.basic2DvectorialV2.ISegment;
import my.util.CardinalDirection;
import my.util.Timing;
import my.util.geometry.IRectangle;

public class MovingBox implements IRectangle {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Point xy = new Point(0, 0);
	protected Point wh = new Point(0, 0);
	protected MotionVector vec;
	@Deprecated
	protected Consumer<Timing> obs;
	protected IMotionProvider motprovider;
	protected boolean chaotic = true;

	public MovingBox(int nx, int ny, int nw, int nh, MotionVector nvec, IMotionProvider nprov) {
		xy.x = this.x = nx;
		xy.y = this.y = ny;
		wh.x = width = nw;
		wh.y = height = nh;

		vec = nvec;
		motprovider = nprov;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int nx) {
		xy.x = x = nx;
	}

	public void setY(int ny) {
		xy.y = y = ny;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public MotionVector getVec() {
		return this.vec;
	}

	/***
	 * vérifie si il y a collision puis appelle
	 * {@link engine.physic.basic2Dvectorial.HorizontalSegment#impact} (ou celui de
	 * VerticalSegment selon la face touchée)
	 * 
	 * @param wall
	 */
	@Deprecated
	public boolean collideWith(StillBox wall) {
		MovingBox mob = this;
		int x1, y1, x2, y2;
		x1 = wall.getX() - mob.getWidth();
		y1 = wall.getY() - mob.getHeight();
		x2 = wall.getX2();
		y2 = wall.getY2();
		boolean rtn = false;

		// teste si la cible peut entrer en contact la droite ou la gauche
		if (vec.getX() == 0) {
			// peut pas
		} else {
			VerticalSegment seg;
			if (vec.getX() > 0) { // gauche
				seg = new VerticalSegment(x1, y1, y2, 0);
			} else {// droite
				seg = new VerticalSegment(x2, y1, y2, 0);
			}
			rtn |= seg.impact(mob.xy, vec);
		}
		// teste si la cible peut entrer en contact le haut ou le bas
		if (vec.getY() == 0) {
			// peut pas
		} else {
			HorizontalSegment seg;
			if (vec.getY() > 0) { // haut
				seg = new HorizontalSegment(x1, x2, y1, 0);
			} else { // bas
				seg = new HorizontalSegment(x1, x2, y2, 0);
			}
			rtn |= seg.impact(mob.xy, vec);
		}
		return rtn;
	}

	public boolean collideWith(MovingBox ent) {
		return false; // TODO faire la collision
	}

	/***
	 * renvoie si l'objet a une chance de rentrer en collision avec un mur <br/>
	 * (par exemple si le déplacement a est lié au joueur, ou si j'ai eu la flemme
	 * d'ajouter un check dans l'algo de pathfinding de l'entité, (ou qu'elle est en
	 * version test mdr))
	 * 
	 * @return
	 */
	public boolean isBoundToChaos() {
		return this.chaotic;
	}

	public void setChaotic(boolean nchaos) {
		this.chaotic = nchaos;
	}

	/***
	 * applique le déplacement du vecteur aux coordonnées
	 */
	public void applyMotion() {
		xy.x = x += this.vec.x;
		xy.y = y += this.vec.y;
		this.vec.x = 0;
		this.vec.y = 0;
	}

	public void onMotionComputed() {
		if(this.obs!=null) {
			this.obs.accept(Timing.after);
		}
		this.motprovider.onMotionComputed(this);
	}

	public void nextMove() {
		this.applyMotion();
		this.vec.add(this.motprovider.getNextMove());
		// Log.log(this, "player pos:" + this.x + "/" + this.y);
		// Log.log(this, "vec:" + this.vec.getX() + "/" + this.vec.getY());

	}

	public void setMotionProvider(IMotionProvider nmotprov) {
		this.motprovider = nmotprov;
	}

	/***
	 * collisionneur pour le basic engine V2
	 * 
	 * @param walls
	 */
	public void checkCollisions(ArrayList<ISegment> walls, CardinalDirection dir) {
		for (ISegment seg : walls) {
			if (seg.impact(xy, wh, vec, dir)) {
				break;
			}
		}
	}

	@Deprecated
	public void setOBs(Consumer<Timing> c) {
		obs = c;
	}

	@Override
	public int getX2() {
		return x + width;
	}

	@Override
	public int getY2() {
		return y + height;
	}

}
