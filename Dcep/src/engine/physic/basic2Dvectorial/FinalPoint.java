package engine.physic.basic2Dvectorial;

public class FinalPoint {

	protected int x;
	protected int y;

	public FinalPoint(int nx, int ny) {
		this.x = nx;
		this.y = ny;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public FinalPoint getAddition(FinalPoint vec) {
		return new FinalPoint(this.getX() + vec.getX(), this.getY() + vec.getY());
	}

	public FinalPoint getSubstraction(FinalPoint vec) {
		return new FinalPoint(this.getX() - vec.getX(), this.getY() - vec.getY());
	}

	public FinalPoint getMultiplication(FinalPoint vec) {
		return new FinalPoint(this.getX() * vec.getX(), this.getY() * vec.getY());
	}

	public FinalPoint getDivision(FinalPoint vec) {
		return new FinalPoint(this.getX() / vec.getX(), this.getY() / vec.getY());
	}
}
