package engine.game.defaultge.level.type1.states;

import java.awt.Graphics;

import engine.game.defaultge.level.type1.StageType1;
import engine.render.engine2d.renderable.I2DRenderer;
import engine.state.prototype2.IState;
import my.util.Keys;

public class DungeonCrawlingState implements IState {
	protected StageType1 stage;

	public DungeonCrawlingState(StageType1 nstage) {
		stage = nstage;
	}

	@Override
	public void run2(long time) {
		stage.getCurrent().update(time);
		stage.scontext.getInputE().ifPressed(Keys.tab.value, () -> {
			stage.guifsm.add(new MapMenuState(stage));
		});
		stage.scontext.getInputE().ifPressed(Keys.escape.value, () -> {
			stage.guifsm.add(new PauseMenuState(stage));
		});
		stage.addTester();
	}

	@Override
	public void render(I2DRenderer r, Graphics g, long time, double scx, double scy) {
		stage.scene.render(r, g, time, scx, scy);
	}

}
