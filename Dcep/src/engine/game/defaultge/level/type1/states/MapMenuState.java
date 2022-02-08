package engine.game.defaultge.level.type1.states;

import engine.game.defaultge.level.type1.StageType1;
import engine.state.prototype2.IState;
import my.util.Keys;

public class MapMenuState implements IState {
	protected StageType1 stage;

	public MapMenuState(StageType1 nstage) {
		stage = nstage;
		//stage.map.img.setVisible(true);
	}

	@Override
	public void run2(long time) {
		stage.scontext.getInputE().ifPressed(Keys.tab.value, () -> {
			stage.guifsm.removeFrom(this);
		});
	}

	@Override
	public void prepare(IWaitlist wt, int res, long time, double px, double py, double vx, double vy) {
		stage.scene.prepare(wt, res, time, px, py, vx, vy);
		stage.map.img.prepare(wt, res, time, px, py, vx, vy);
	}

}
