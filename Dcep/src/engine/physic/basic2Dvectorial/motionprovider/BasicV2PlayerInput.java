package engine.physic.basic2Dvectorial.motionprovider;

import engine.input.IInputEngine;
import engine.physic.basic2Dvectorial.FinalMotionVector;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import my.util.Gauge;
import my.util.Keys;

public class BasicV2PlayerInput implements IMotionProvider {

	protected IInputEngine inputs;
	protected Gauge up;
	protected Gauge down;
	protected Gauge right;
	protected Gauge left;
	protected PlayerInputV2Feedbackable fb;

	public BasicV2PlayerInput(IInputEngine ninputs, PlayerInputV2Feedbackable nfb) {
		this.inputs = ninputs;
		int max = 10;
		this.up = new Gauge(0, max, 0);
		this.down = new Gauge(0, max, 0);
		this.right = new Gauge(0, max, 0);
		this.left = new Gauge(0, max, 0);
		this.fb = nfb;
	}

	protected void accelerate(Gauge gauge) {
		int inv = gauge.getMax() - gauge.get();
		inv = (int) ((inv * 0.8) - 0.1);
		gauge.set(gauge.getMax() - inv);

	}

	protected void deccelerate(Gauge gauge) {
		gauge.set((int) (gauge.get() * 0.8));
		gauge.substract(3);
	}

	@Override
	public FinalMotionVector getNextMove() {
		MotionVector vec = new MotionVector(0, 0);
		if (inputs.isActive(Keys.up.value)) {
			accelerate(this.up);
			// vec.addY(-20);
		} else {
			deccelerate(this.up);
		}

		if (inputs.isActive(Keys.down.value)) {
			accelerate(this.down);
			// vec.addY(20);
		} else {
			deccelerate(this.down);
		}

		if (inputs.isActive(Keys.left.value)) {
			accelerate(this.left);
			// vec.addX(-20);
		} else {
			deccelerate(this.left);
		}

		if (inputs.isActive(Keys.right.value)) {
			accelerate(this.right);
			// vec.addX(20);
		} else {
			deccelerate(this.right);
		}

		vec.addY(-this.up.get());
		vec.addY(this.down.get());
		vec.addX(this.right.get());
		vec.addX(-this.left.get());

		return vec;
	}

	@Override
	public void onMotionComputed(MovingBox box) {
		this.fb.onMotionComputed();
	}

	public static interface PlayerInputV2Feedbackable {
		void onMotionComputed();
	}

}
