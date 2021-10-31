package engine.game.defaultge.level;

import engine.game.defaultge.DefaultGameEngine;
import engine.loop.ILoopable;
import engine.render.IRenderEngine;

public abstract class StageEngine<REN extends IRenderEngine> implements ILoopable, IStageEngine {

	protected REN ren;

	public StageEngine(REN nren) {
		this.ren = nren;
	}

	public abstract void start(DefaultGameEngine ge);

	public abstract IStageEngine getNext();

	public REN getRenderEngine() {
		return ren;
	}
}
