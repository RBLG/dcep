package engine.game.defaultge.level.type1.states;

import engine.game.defaultge.level.type1.StageType1;
import engine.render.engine2d.Basic2DSub;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.Rectangle;
import engine.state.prototype2.IState;
import my.util.Keys;

import java.awt.Color;

public class PauseMenuState implements IState {
	protected StageType1 stage;

	protected Rectangle filter;

	public PauseMenuState(StageType1 nstage) {
		stage = nstage;
		Color col = new Color(0, 0, 0, 100);
		filter = new Rectangle(0, 0, Basic2DSub.LDxmax, Basic2DSub.LDymax, col, DrawLayer.Menu);
	}

	@Override
	public void run2(long time) {
		stage.scontext.getInputE().ifPressed(Keys.escape.value, () -> {
			stage.guifsm.removeFrom(this);
		});
	}

	@Override
	public void prepare(IWaitlist wt, int res, long time, double px, double py, double vx, double vy) {
		stage.scene.prepare(wt, res, time, px, py, vx, vy);;
		filter.prepare(wt, res, time, px, py, vx, vy);;
	}
}
