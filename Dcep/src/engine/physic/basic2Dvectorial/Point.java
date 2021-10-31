package engine.physic.basic2Dvectorial;

public class Point extends FinalPoint {

	public Point(int nx, int ny) {
		super(nx, ny);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void add(FinalPoint vec) {
		this.x += vec.getX();
		this.y += vec.getY();
	}

	public void substract(FinalPoint vec) {
		this.x -= vec.getX();
		this.y -= vec.getY();
	}

	public void multiply(FinalPoint vec) {
		this.x *= vec.getX();
		this.y *= vec.getY();
	}

	public void divide(FinalPoint vec) {
		this.x /= vec.getX();
		this.y /= vec.getY();
	}

}
