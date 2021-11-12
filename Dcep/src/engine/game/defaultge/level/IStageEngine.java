package engine.game.defaultge.level;

import engine.game.defaultge.DefaultGameEngine;
import my.util.Log;

public interface IStageEngine {

	public void start(DefaultGameEngine ge);

	public IStageEngine getNext();

	// public IRenderEngine getRenderEngine();

	default public void startNext(DefaultGameEngine ge) {
		Log.log(this, "next stage");
		IStageEngine nse = this.getNext();
		ge.gcontext.curstage = nse;
		nse.start(ge);

	}
}
