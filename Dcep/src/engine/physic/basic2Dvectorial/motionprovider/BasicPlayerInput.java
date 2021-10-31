package engine.physic.basic2Dvectorial.motionprovider;

import engine.input.IInputEngine;
import engine.physic.basic2Dvectorial.FinalMotionVector;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import my.util.Keys;

public class BasicPlayerInput implements IMotionProvider {

	protected IInputEngine inputs;

	public BasicPlayerInput(IInputEngine ninputs) {
		this.inputs = ninputs;
	}

	@Override
	public FinalMotionVector getNextMove() {
		MotionVector vec = new MotionVector(0, 0);
		if (inputs.isActive(Keys.up.value)) {
			vec.addY(-25);
		}
		if (inputs.isActive(Keys.down.value)) {
			vec.addY(25);
		}
		if (inputs.isActive(Keys.left.value)) {
			vec.addX(-25);
		}
		if (inputs.isActive(Keys.right.value)) {
			vec.addX(25);
		}

		// Log.log(this, "player prepare son next move:"+vec.getX()+"/"+vec.getY());
		return vec;
	}

	@Override
	public void onMotionComputed(MovingBox movingBox) {
		
	}

}
