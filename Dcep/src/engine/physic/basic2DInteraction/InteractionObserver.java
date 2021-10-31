package engine.physic.basic2DInteraction;

public class InteractionObserver {

	protected MyConsumer cons;
	protected int x;

	protected int y;
	protected int width;
	protected int height;
	protected String[] infos;

	public InteractionObserver(MyConsumer ncons, int nx, int ny, int nwidth, int nheight, String[] ninfos) {
		this.cons = ncons;
		this.x = nx;
		this.y = ny;
		this.width = nwidth;
		this.height = nheight;
		this.infos = ninfos;

	}

	public static interface MyConsumer {
		public void accept(InteractionObserver e);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void checkOverlap(RelativeInteractionObserver obs) {

		int ptx, pty, rx1, ry1, rx2, ry2;
		ptx = obs.getX();
		pty = obs.getY();
		rx1 = this.getX() - obs.getWidth();
		ry1 = this.getY() - obs.getHeight();
		rx2 = this.getX() + this.getWidth();
		ry2 = this.getY() + this.getHeight();
		//Log.log(this, "pt:" + ptx + "/" + pty + " r1:" + rx1 + "/" + ry1 + " r2:" + rx2 + "/" + ry2);
		if (ptx > rx1 && ptx < rx2 && pty > ry1 && pty < ry2) {
			this.cons.accept(obs);
			obs.cons.accept(this);
		}
	}

	public String[] getInfos() {
		return this.infos;
	}

}
