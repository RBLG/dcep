package engine.physic.basic2DInteraction;

import engine.physic.basic2Dvectorial.MovingBox;

public class RelativeInteractionObserver extends InteractionObserver {

	protected MovingBox box;

	public RelativeInteractionObserver(MyConsumer ncons, int nx, int ny, int nwidth, int nheight, MovingBox nbox,
			String[] ninfos) {
		super(ncons, nx, ny, nwidth, nheight, ninfos);
		this.box = nbox;
	}

	@Override
	public int getX() {
		return this.box.getX() + x;

	}

	@Override
	public int getY() {
		return this.box.getY() + y;
	}

	public void setX(int nx) {
		this.x = nx;
	}

	public void setY(int ny) {
		this.y = ny;
	}

}
