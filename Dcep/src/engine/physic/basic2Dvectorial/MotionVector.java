package engine.physic.basic2Dvectorial;

public class MotionVector extends FinalMotionVector {

	public MotionVector(int x, int y) {
		super(x, y);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void addX(int x) {
		this.x += x;
	}

	public void addY(int y) {
		this.y += y;
	}

	public void add(FinalMotionVector vec) {
		this.x += vec.getX();
		this.y += vec.getY();
	}

	public void substract(FinalMotionVector vec) {
		this.x -= vec.getX();
		this.y -= vec.getY();
	}

	public void multiply(FinalMotionVector vec) {
		this.x *= vec.getX();
		this.y *= vec.getY();
	}

	public void divide(FinalMotionVector vec) {
		this.x /= vec.getX();
		this.y /= vec.getY();
	}

	public void set(MotionVector vec) {
		this.x = vec.getX();
		this.y = vec.getY();
	}

}
