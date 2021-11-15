package engine.game.defaultge.level.type1;

import java.awt.Point;
import engine.game.defaultge.DefaultGameEngine;
import engine.game.defaultge.GameContext;
import engine.game.defaultge.level.IStageEngine;
import engine.game.defaultge.level.StageEngine;
import engine.game.defaultge.level.type1.entity.PathfindingTester;
import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.misc.util2d.position.IMotionModifier;
import engine.misc.util2d.position.PrecisionModifier;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.Scene;
import engine.state.prototype2.State;
import engine.state.prototype2.StateCore;
import main.events.StepEvent;
import my.util.Cardinal;
import my.util.Keys;
import my.util.Log;

public class StageType1 extends StageEngine {

	protected Room[][] floor;
	protected Point current = new Point(StageGenerator.fcentx, StageGenerator.fcenty);
	protected PlayerEntityV3 player;
	public StageMap map = new StageMap();

	public StageContext scontext;

	public Scene topscene = new Scene();
	public Scene scene = new Scene();

	public StateCore guifsm = new StateCore();

	// public State e;

	// public State e;

	public StageType1(GameContext gcontext) {
		super();

		this.scontext = new StageContext(gcontext, this);
		this.floor = StageGenerator.genFloor(this);
		this.player = new PlayerEntityV3(scontext);

		guifsm.add(dungeoncrawling);
	}

	public State dungeoncrawling = new State((time) -> {
		this.getCurrent().update(time);
		scontext.getInputE().ifPressed(Keys.tab.value, () -> {
			this.openMap();
		});
		this.addTester();
	}, (r, g, time, scx, scy) -> {
		this.scene.render(r, g, time, scx, scy);
	});
	public State mapmenu = new State((time) -> {
		scontext.getInputE().ifPressed(Keys.tab.value, () -> {
			closeMap();
		});
	}, (r, g, time, scx, scy) -> {
		this.scene.render(r, g, time, scx, scy);
		this.map.img.render(r, g, time, scx, scy);
	});
	public State hackmenu;
	public State pausemenu;
	public State betweenroom = new State(null, (r, g, time, scx, scy) -> {
		this.scene.render(r, g, time, scx, scy);
	});

	// TODO trouver une alternative, ça polue la
	public void closeMap() {
		guifsm.removeFrom(mapmenu);
	}

	public void openMap() {
		this.guifsm.add(mapmenu);// TODO fix la map
		this.map.img.getPos().getPos().x = this.current.x * StageGenerator.cyclex + 30;
		this.map.img.getPos().getPos().y = this.current.y * StageGenerator.cycley + 30;
		this.map.img.setVisible(true);
	}

	@Override
	public IStageEngine getNext() {
		return this.scontext.gcontext.getTree().getNext();
	}

	@Override
	public void start(DefaultGameEngine ge) {
		ge.gcontext.getRenderE().setScene(topscene);
		topscene.add(this.guifsm, DrawLayer.Room_Entities);
		// ge.gcontext.getRenderE().setScene(this.scene);
		this.scene.setVisible(true);
		this.setCamOnRoom(this.current.x, this.current.y);

		this.getCurrent().playerEnter(Cardinal.north, this.player);
		ge.startDefaultLoop(this);
	}

	public void moveRoom(Cardinal dir) {
		/////////////////////////// RN////////////
		Room pre, post;
		pre = this.getCurrent();
		int mx = dir.toXMultiplier();
		int my = dir.toYMultiplier();
		if (this.floor[current.x + mx][current.y + my] == null) {
			Log.log(this, "mur mangé");
			return;
		}
		this.guifsm.add(betweenroom);
		this.current.x += mx;
		this.current.y += my;

		post = this.getCurrent();
		this.moveCamByOffset(mx, my, 600);

		//////////////// ENTRE L'ENTRE ET SORTIE/////////
		StepEvent.start().then(GameTick.fromMillis(300), (long time) -> { // était à 300ms
			pre.update(time);
			pre.playerLeave(this.player);
			post.playerEnter(dir, this.player);
			post.update(time);

		}).then(GameTick.fromMillis(350), (long time) -> { // était a 650ms
			/////////////// FIN////////////////////
			this.guifsm.removeFrom(betweenroom);
		}).endAndUse((ev) -> scontext.gcontext.EventE.chaotic.add(ev));
	}

	public Room getCurrent() {
		return this.floor[current.x][current.y];
	}

	public void setCamOnRoom(int x, int y) {
		Point pos = this.scene.getPos().getPos();
		pos.x = -x * StageGenerator.cyclex;
		pos.y = -y * StageGenerator.cycley;
	}

	public void moveCamByOffset(int x, int y, long time) {
		IMotionModifier omod = this.scene.getPos().getModifier();
		long end = omod.getMaxTime();
		Point pos = this.scene.getPos().getPos();
		pos.x += (int) omod.getModX(end);
		pos.y += (int) omod.getModY(end);
		long now = System.currentTimeMillis();
		int pmx = -x * StageGenerator.cyclex;
		int pmy = -y * StageGenerator.cycley;
		PrecisionModifier nmod = new PrecisionModifier(time, now, pmx, pmy);
		this.scene.getPos().setModifier(nmod);
	}

	@Override
	public void run2(long time) {
		guifsm.run(time);
	}

	public void addTester() {
		scontext.getInputE().ifPressed(Keys.space.value, () -> {
			PathfindingTester pft = new PathfindingTester(this);
			this.getCurrent().enter(Cardinal.north, pft);
			Log.log(this, "new tester");
		});
	}
}