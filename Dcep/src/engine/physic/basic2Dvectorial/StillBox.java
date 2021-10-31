package engine.physic.basic2Dvectorial;

public class StillBox {

	protected int x;
	protected int y;
	protected int x2;
	protected int y2;
	public final boolean normal;

	public StillBox(int nx, int ny, int nx2, int ny2, boolean nnormal) {
		this.normal = nnormal;
		if (Boolean.logicalXor(!normal, nx < nx2)) {
			this.x = nx;
			this.x2 = nx2;
		} else {
			this.x = nx2;
			this.x2 = nx;
		}

		if (Boolean.logicalXor(!normal, ny < ny2)) {
			this.y = ny;
			this.y2 = ny2;
		} else {
			this.y = ny2;
			this.y2 = ny;
		}

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}
}
