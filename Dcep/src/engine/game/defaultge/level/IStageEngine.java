package engine.game.defaultge.level;

import engine.game.defaultge.DefaultGameEngine;
import engine.render.IRenderEngine;

public interface IStageEngine {

	public void start(DefaultGameEngine ge);

	public IStageEngine getNext();

	public IRenderEngine getRenderEngine();
}
