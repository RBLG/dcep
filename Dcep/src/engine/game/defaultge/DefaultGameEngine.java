package engine.game.defaultge;

import engine.game.IGameEngine;
import engine.game.defaultge.level.IStageEngine;
import engine.input.IInputEngine;
import engine.loop.BasicLooper;
import engine.loop.ILoopable;
import engine.render.IRenderEngine;
import engine.render.PanelAdapter;
import engine.save.Constants;
import engine.save.ISaveEngine;
import engine.save.Settings;
import my.util.Log;

public class DefaultGameEngine implements IGameEngine {

	/***
	 * nombre de tick par seconde
	 */
	public static final long tickrate = 30;
	/***
	 * durée d'un tick
	 */
	public static final long tick = tickrate / 1000;

	protected GameContext gcontext;

	public DefaultGameEngine(PanelAdapter nadapter, IInputEngine ninputs, ISaveEngine nsave) {
		this.gcontext = new GameContext(nadapter, ninputs, nsave, this);

	}

	@Override
	public int getTickRate() {
		return 30; // 30 par 1000 millis
	}

	@Override
	public void start() {

		gcontext.curstage = gcontext.tree.getFirst();
		while (this.gcontext.curstage != null) {
			gcontext.curstage.getRenderEngine().load(gcontext.adapter);
			gcontext.curstage.start(this);
			gcontext.curstage = gcontext.curstage.getNext();
			Log.log(this, "next stage");
		}

	}

	public IInputEngine getInputs() {
		return gcontext.inputE;
	}

	public PanelAdapter getAdapter() {
		return gcontext.adapter;
	}

	public void startDefaultLoop(IStageEngine stage) {
		BasicLooper looper = new BasicLooper(Constants.game_tick_per_second, Settings.settings.frame_per_second);
		IRenderEngine ren = stage.getRenderEngine();
		looper.add((ILoopable) stage);
		looper.add(ren);
		looper.add((ILoopable) gcontext.inputE);
		looper.addRT((long time) -> {
			ren.updateScreen();
		});
		looper.start();
	}

}
