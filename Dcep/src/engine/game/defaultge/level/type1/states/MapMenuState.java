package engine.game.defaultge.level.type1.states;

import java.awt.Graphics;

import engine.game.defaultge.level.type1.StageType1;
import engine.render.engine2d.renderable.I2DRenderer;
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
	public void render(I2DRenderer r, Graphics g, long time, double scx, double scy) {
		stage.scene.render(r, g, time, scx, scy);
		stage.map.img.render(r, g, time, scx, scy);
	}

}
