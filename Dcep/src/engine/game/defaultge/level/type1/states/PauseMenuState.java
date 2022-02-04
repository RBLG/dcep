package engine.game.defaultge.level.type1.states;

import java.awt.Graphics;

import engine.game.defaultge.level.type1.StageType1;
import engine.render.engine2d.Basic2DSub;
import engine.render.engine2d.I2DRenderer;
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
		filter = new Rectangle(0, 0, Basic2DSub.LDxmax, Basic2DSub.LDymax, col);
	}

	@Override
	public void run2(long time) {
		stage.scontext.getInputE().ifPressed(Keys.escape.value, () -> {
			stage.guifsm.removeFrom(this);
		});
	}

	@Override
	public void render(I2DRenderer r, Graphics g, long time, double scx, double scy) {
		stage.scene.render(r, g, time);
		filter.render(r, g, time);
	}
}
