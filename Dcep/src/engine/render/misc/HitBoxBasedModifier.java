package engine.render.misc;

import engine.game.defaultge.level.type1.Room;
import engine.misc.util2d.position.IMotionModifier;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import my.util.geometry.IPoint;
import my.util.geometry.IVector.Vector;

public class HitBoxBasedModifier implements IMotionModifier {

	protected MovingBox hitbox;
	protected IPoint offset;
	protected long beginning;
	protected MotionVector vec;
	protected IPoint.Point pos;

	public HitBoxBasedModifier(MovingBox nhitbox, IPoint noffset, long nbeginning) {
		this.hitbox = nhitbox;
		this.vec = new MotionVector(nhitbox.getVec().getX() / Room.simscale, nhitbox.getVec().getY() / Room.simscale);
		this.offset = noffset;
		this.beginning = nbeginning;
		this.pos = new IPoint.Point(nhitbox.getX() / Room.simscale, nhitbox.getY() / Room.simscale);

	}

	@Override
	public long getMaxTime() {
		return vec.getDuration();
	}

	public void resetBeginning() {
		this.beginning = System.currentTimeMillis();
		Vector bigvec = this.hitbox.getVec();
		Vector nvec = new Vector(bigvec.getX() / Room.simscale, bigvec.getY() / Room.simscale);
		this.vec.set(nvec);
		this.pos.setX(this.hitbox.getX() / Room.simscale);
		this.pos.setY(this.hitbox.getY() / Room.simscale);
	}

	@Override
	public double getModX(long time) {
		return pos.getX() + this.vec.getX() * this.getProgressMultiplier(time);
	}

	@Override
	public double getModY(long time) {
		return pos.getY() + this.vec.getY() * this.getProgressMultiplier(time);
	}

	protected double getProgressMultiplier(long time) {
		double duration = this.vec.getDuration();
		double prog = (time - beginning);
		if (prog > duration) {
			prog = duration;
		}
		return prog / duration;
	}

}
