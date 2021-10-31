package engine.game.defaultge.StageTree;

import engine.game.defaultge.GameContext;
import engine.game.defaultge.level.IStageEngine;
import engine.game.defaultge.level.type1.StageType1;

public class StaticStageTree implements IStageTree {

	public GameContext gcontext;

	// protected PlayerSoul stat;

	public StaticStageTree(GameContext ngcontext) {
		this.gcontext = ngcontext;
	}

	@Override
	public IStageEngine getFirst() {
		// TODO faire le random de monde
		return new StageType1(gcontext);
	}

	@Override
	public IStageEngine getNext() {
		return new StageType1(gcontext);
	}

}
