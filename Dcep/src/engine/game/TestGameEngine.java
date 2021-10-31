package engine.game;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;

import engine.input.IInputEngine;
import engine.loop.BasicLooper;
import engine.loop.ILoopable;
import engine.misc.util2d.position.BasicSpeedModifier;
import engine.render.PanelAdapter;
import engine.render.engine2d.Basic2DEngine;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.Scene;
import engine.render.engine2d.renderable.BasicText;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.StillImage;
import engine.save.ISaveEngine;
import my.util.ImageCache;
import my.util.Keys;

public class TestGameEngine implements IGameEngine, ILoopable {

	public final int tick_per_second = 30;

	protected Scene scene;
	protected StillImage img;
	protected LoopingAnimation img2;
	protected boolean dir;
	protected IInputEngine inputs;
	protected BasicSpeedModifier mod;
	protected Basic2DEngine reng;
	protected PanelAdapter adapter;

	public TestGameEngine(PanelAdapter nadapter, IInputEngine ninputs, ISaveEngine saveng) {
		// this.reng = nreng;
		this.inputs = ninputs;
		this.reng = new Basic2DEngine(adapter);
		this.adapter = nadapter;

		this.scene = new Scene();
		this.img = new StillImage(ImageCache.getImage("test.png"), 100, 200);
		this.mod = new BasicSpeedModifier(0D, 0D, 0, 0);

		this.img.getPos().setModifier(mod);

		this.img2 = new LoopingAnimation(ImageCache.getImages("animtest").toArray(new Image[4]), new Point(100, 200),
				100);

		this.scene.addRenderable(img, DrawLayer.Room_Entities);

		BasicText txt = new BasicText(new Point(40, 100), "test");

		this.scene.addRenderable(txt, DrawLayer.Menu);

		// this.scene.addRenderable(img2, DrawLayer.Game_Entities);
	}

	@Override
	public void go(long time) {
		Point pos = this.img2.getPos().getPos();
		Double speed = 2D;
		int speed2 = 2;

		Point2D.Double modf = new Point2D.Double(0, 0);
		if (this.inputs.isActive(Keys.shift.value)) {
			speed = 4D;
			speed2 = 4;
		} else if (this.inputs.isActive(Keys.ctrl.value)) {
			speed = 1D;
			speed2 = 1;
		}

		speed = speed / (1000 / this.tick_per_second);

		if (this.inputs.isActive(Keys.down.value)) {
			modf.y += speed;
			pos.y += speed2;
		}
		if (this.inputs.isActive(Keys.up.value)) {
			modf.y -= speed;
			pos.y -= speed2;
		}

		if (this.inputs.isActive(Keys.right.value)) {
			modf.x += speed;
			pos.x += speed2;
		}
		if (this.inputs.isActive(Keys.left.value)) {
			modf.x -= speed;
			pos.x -= speed2;
		}

		this.img.getPos().apply();
		this.mod.update(modf.x, modf.y, time, 1000 / this.tick_per_second);
	}

	@Override
	public int getTickRate() {
		return this.tick_per_second;
	}

	@Override
	public void start() {

		this.reng.load(this.adapter);
		reng.setScene(this.scene);

		BasicLooper looper = new BasicLooper(this.getTickRate(), 60);
		looper.add(this);
		looper.add(reng);
		looper.add((ILoopable) inputs);
		looper.addRT((long time) -> {
			reng.updateScreen();
		});

		looper.start();

	}

}
