package engine.render.misc;

import engine.game.defaultge.level.type1.Room;
import engine.input.IInputEngine;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.motionprovider.IMotionProvider;
import my.util.Gauge;
import my.util.Keys;
import my.util.geometry.IVector.Vector;

public class BasicV2PlayerInput implements IMotionProvider {

	protected IInputEngine inputs;
	protected Gauge up;
	protected Gauge down;
	protected Gauge right;
	protected Gauge left;

	public BasicV2PlayerInput(IInputEngine ninputs) {
		this.inputs = ninputs;
		int max = 10;
		this.up = new Gauge(0, max, 0);
		this.down = new Gauge(0, max, 0);
		this.right = new Gauge(0, max, 0);
		this.left = new Gauge(0, max, 0);
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
	public Vector getNextMotionVector(MovingBox box) {
		Vector vec = new Vector(0, 0);
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

		vec.setY((vec.getY() - this.up.get() * Room.simscale));
		vec.setY((vec.getY() + this.down.get() * Room.simscale));
		vec.setX((vec.getX() + this.right.get() * Room.simscale));
		vec.setX((vec.getX() - this.left.get() * Room.simscale));

		return vec;
	}
}
