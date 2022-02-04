package engine.game.defaultge.level.type1.states;

import engine.game.defaultge.level.type1.StageType1;
import engine.render.engine2d.I2DRenderer;
import engine.render.engine2d.SceneV2;
import engine.state.prototype2.IState;
import my.util.Keys;
import my.util.geometry.IPoint;

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
	public IPoint updateRefs(IPoint nori, I2DRenderer renderer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepare(SceneV2 scene, IPoint nori, I2DRenderer renderer) {
		stage.scene.prepare(scene, stage.scene.updateRefs(nori, renderer), renderer);
	}

}
