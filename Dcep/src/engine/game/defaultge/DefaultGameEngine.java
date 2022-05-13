package engine.game.defaultge;

import engine.game.defaultge.level.IStageEngine;
import engine.input.IInputEngine;
import engine.render.engine2d.Basic2DEngine;
import engine.render.engine2d.RenderableList;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.save.Constants;
import engine.save.ISaveEngine;
import main.EventCore;
import main.events.IEvent;

public class DefaultGameEngine implements IGameEngine {

	/***
	 * nombre de tick par seconde
	 */
	public static final long tickrate = 30;
	/***
	 * durée d'un tick
	 */
	public static final long tick = tickrate / 1000;

	public GameContext gcontext;

	public DefaultGameEngine(Basic2DEngine nren, IInputEngine ninputs, ISaveEngine nsave) {
		this.gcontext = new GameContext(nren, ninputs, nsave, this);

	}

	@Override
	public int getTickRate() {
		return 30; // 30 par 1000 millis
	}

	@Override
	public void start() {

		gcontext.curstage = gcontext.tree.getFirst();
		gcontext.curstage.start(this);
		// TODO pour l'implementation du next stage -> utiliser ça
		// gcontext.curstage.startNext();

	}

	public IInputEngine getInputs() {
		return gcontext.inputE;
	}

	public void startDefaultLoop(IStageEngine stage) {
		RenderableList scene = new RenderableList();
		gcontext.renderE.setScene(scene);
		scene.add((I2DRenderable) stage);

		EventCore core = gcontext.EventE;
		core.regular.add((IEvent) stage);
		core.regular.add((IEvent) gcontext.inputE);
		core.regular.add(gcontext.renderE);
		gcontext.renderE.panel.setUpdated(true);
		core.start(Constants.millis_between_game_ticks);
	}

}
