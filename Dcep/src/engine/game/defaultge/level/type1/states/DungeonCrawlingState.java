package engine.game.defaultge.level.type1.states;

import engine.game.defaultge.level.type1.StageType1;
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
	public void prepare(IWaitlist wt, int res, long time, double px, double py, double vx, double vy) {
		stage.scene.prepare(wt, res, time, px, py, vx, vy);
	}

}
