package engine.game.defaultge.StageTree;

import engine.game.defaultge.level.IStageEngine;

public interface IStageTree {
	
	public IStageEngine getFirst();

	public IStageEngine getNext();
}
