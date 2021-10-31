package engine.game.defaultge.level;

import java.awt.geom.Point2D;

import engine.game.defaultge.DefaultGameEngine;
import engine.input.IInputEngine;
import engine.loop.BasicLooper;
import engine.loop.ILoopable;
import engine.misc.util2d.position.BasicSpeedModifier;
import engine.render.engine2d.Basic2DEngine;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.Scene;
import engine.render.engine2d.renderable.StillImage;
import my.util.ImageCache;
import my.util.Keys;

public class TestStageEngine extends StageEngine<Basic2DEngine> {

	protected Scene scene;
	protected DefaultGameEngine ge;
	protected BasicLooper looper;
	/////////////////////
	protected StillImage img;

	protected BasicSpeedModifier mod;

	public TestStageEngine() {
		super(new Basic2DEngine(null));
		this.scene = new Scene();
		this.img = new StillImage(ImageCache.getImage("test.png"), 100, 200);
		this.mod = new BasicSpeedModifier(0D, 0D, 0, 0);
		this.img.getPos().setModifier(mod);

		this.looper = new BasicLooper(30, 60);
	}

	@Override
	public void go(long time) {
		IInputEngine inputs = this.ge.getInputs();

		Double speed = 2D;

		Point2D.Double modf = new Point2D.Double(0, 0);
		if (inputs.isActive(Keys.shift.value)) {
			speed = 4D;
		} else if (inputs.isActive(Keys.ctrl.value)) {
			speed = 1D;
		}

		speed = speed / (1000 / 30);

		if (inputs.isActive(Keys.down.value)) {
			modf.y += speed;
		}
		if (inputs.isActive(Keys.up.value)) {
			modf.y -= speed;
		}

		if (inputs.isActive(Keys.right.value)) {
			modf.x += speed;
		}
		if (inputs.isActive(Keys.left.value)) {
			modf.x -= speed;
		}

		if (inputs.isActive(Keys.enter.value)) {
			this.looper.stop();
			// this.scene.clean();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		this.img.getPos().apply();
		this.mod.update(modf.x, modf.y, time, 1000 / 30);
	}

	@Override
	public void start(DefaultGameEngine nge) {
		this.ge = nge;

		this.ren.setScene(this.scene);
		this.ren.load(nge.getAdapter());
		this.scene.addRenderable(img, DrawLayer.Room_Entities);

		this.startLoop();
	}

	protected void startLoop() {
		looper.add(this);
		looper.add(this.ren);
		looper.add((ILoopable) ge.getInputs());
		looper.addRT((long time) -> {
			ren.updateScreen();
		});

		looper.start();
	}

	@Override
	public IStageEngine getNext() {
		return new TestStageEngine();
	}

}
