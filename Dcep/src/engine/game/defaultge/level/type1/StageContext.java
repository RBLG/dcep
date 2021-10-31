package engine.game.defaultge.level.type1;

import engine.game.defaultge.GameContext;
import engine.input.IInputEngine;

public class StageContext {
	protected GameContext gcontext;
	protected StageType1 stageE;

	public StageContext(GameContext ngcontext, StageType1 nstageE) {
		this.gcontext = ngcontext;
		this.stageE = nstageE;
	}

	public GameContext getGcontext() {
		return gcontext;
	}

	public StageType1 getStageE() {
		return stageE;
	}

	public IInputEngine getInputE() {
		return gcontext.getInputE();
	}
}
