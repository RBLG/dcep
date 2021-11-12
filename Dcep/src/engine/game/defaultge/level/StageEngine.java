package engine.game.defaultge.level;

import engine.game.defaultge.DefaultGameEngine;
import main.events.IPermanentEvent;

public abstract class StageEngine implements IStageEngine, IPermanentEvent {

	public abstract void start(DefaultGameEngine ge);

	public abstract IStageEngine getNext();

}
