package engine.physic.basic2Dvectorial;

import engine.save.Constants;

public class FinalMotionVector {

	protected int x;
	protected int y;

	public FinalMotionVector(int nx, int ny) {
		this.x = nx;
		this.y = ny;
	}

	public long getDuration() {
		return Constants.millis_between_game_ticks;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public MotionVector getAddition(FinalMotionVector vec) {
		return new MotionVector(this.getX() + vec.getX(), this.getY() + vec.getY());
	}

	public MotionVector getSubstraction(FinalMotionVector vec) {
		return new MotionVector(this.getX() - vec.getX(), this.getY() - vec.getY());
	}

	public MotionVector getMultiplication(FinalMotionVector vec) {
		return new MotionVector(this.getX() * vec.getX(), this.getY() * vec.getY());
	}

	public FinalMotionVector getDivision(FinalMotionVector vec) {
		return new MotionVector(this.getX() / vec.getX(), this.getY() / vec.getY());
	}

	public MotionVector clone() {
		return new MotionVector(this.x, this.y);
	}

}
