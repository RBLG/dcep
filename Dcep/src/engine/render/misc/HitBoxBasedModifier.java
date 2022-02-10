package engine.render.misc;

import engine.misc.util2d.position.IMotionModifier;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.render.engine2d.Basic2DSub;
import my.util.geometry.IPoint;
import my.util.geometry.floats.IFloatPoint;
import my.util.geometry.floats.IFloatPoint.FloatPoint;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class HitBoxBasedModifier implements IMotionModifier {

	protected MovingBox hitbox;
	protected IPoint offset;
	protected long beginning;
	protected FloatVector vec;
	protected IFloatPoint.FloatPoint pos;

	public HitBoxBasedModifier(MovingBox nhitbox, IPoint noffset, long nbeginning) {
		this.hitbox = nhitbox;
		this.vec = new FloatVector(nhitbox.getVec().getX(), nhitbox.getVec().getY());
		this.offset = noffset;
		this.beginning = nbeginning;
		this.pos = new FloatPoint(nhitbox.getXY());

	}

	@Override
	public double getMaxTime() {
		return (beginning + Basic2DSub.update_frequency);
	}

	public void resetBeginning() {
		// TODO separer pour que beginning soit plus aligné
		this.beginning = Basic2DSub.getRenderTime();
		FloatVector nvec = this.hitbox.getVec();
		//Log.e("nvec: " + nvec.getX() + "/" + nvec.getY());
		this.vec.set(nvec.getX(), nvec.getY());
		this.pos.set(this.hitbox.getX(), this.hitbox.getY());
	}

	@Override
	public double getModX(long time) {
		//Log.e("mvec: " + vec.getX() + "/" + vec.getY() + " t:" + time + " prog:" + getProgressMultiplier(time));
		//Log.e("result:" + (double) (vec.getX() * this.getProgressMultiplier(time)));
		return pos.getX() + this.vec.getX() * this.getProgressMultiplier(time);
	}

	@Override
	public double getModY(long time) {
		return pos.getY() + this.vec.getY() * this.getProgressMultiplier(time);
	}

	protected double getProgressMultiplier(long time) {
		double duration = Basic2DSub.update_frequency;
		double prog = (time - beginning);
		prog = Math.max(0, prog);
		prog = Math.min(prog, duration);
		return prog / duration;
	}

	public double getNextX(long time) {
		return pos.getX() + vec.getX();
	}

	public double getNextY(long time) {
		return pos.getY() + vec.getY();
	}
}
