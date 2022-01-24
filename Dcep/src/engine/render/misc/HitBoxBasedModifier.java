package engine.render.misc;

import engine.misc.util2d.position.IMotionModifier;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import my.util.geometry.IPoint;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class HitBoxBasedModifier implements IMotionModifier {

	protected MovingBox hitbox;
	protected IPoint offset;
	protected long beginning;
	protected MotionVector vec;
	protected IPoint.Point pos;

	public HitBoxBasedModifier(MovingBox nhitbox, IPoint noffset, long nbeginning) {
		this.hitbox = nhitbox;
		this.vec = new MotionVector((int) nhitbox.getVec().getX(), (int) nhitbox.getVec().getY());
		this.offset = noffset;
		this.beginning = nbeginning;
		this.pos = nhitbox.getX2Y().toInt();

	}

	@Override
	public long getMaxTime() {
		return vec.getDuration();
	}

	public void resetBeginning() {
		this.beginning = System.currentTimeMillis();
		FloatVector nvec = this.hitbox.getVec();
		this.vec.set(new MotionVector((int) nvec.getX(), (int) nvec.getY()));
		this.pos.setX((int) this.hitbox.getX() /// Room.simscale
		);
		this.pos.setY((int) this.hitbox.getY() /// Room.simscale
		);
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
