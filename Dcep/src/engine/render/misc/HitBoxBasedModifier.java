package engine.render.misc;

import engine.misc.util2d.position.IMotionModifier;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.Point;

public class HitBoxBasedModifier implements IMotionModifier {

	protected MovingBox hitbox;
	protected Point offset;
	protected long beginning;
	protected MotionVector vec;
	protected Point pos;

	public HitBoxBasedModifier(MovingBox nhitbox, Point noffset, long nbeginning) {
		this.hitbox = nhitbox;
		this.vec = nhitbox.getVec().clone();
		this.offset = noffset;
		this.beginning = nbeginning;
		this.pos = new Point(nhitbox.getX(), nhitbox.getY());

	}

	@Override
	public long getMaxTime() {
		return vec.getDuration();
	}

	public void resetBeginning() {
		this.beginning = System.currentTimeMillis();
		this.vec.set(this.hitbox.getVec());
		this.pos.setX(this.hitbox.getX());
		this.pos.setY(this.hitbox.getY());
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
