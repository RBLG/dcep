package engine.game.defaultge.level.type1;

import engine.game.defaultge.DefaultGameEngine;
import engine.game.defaultge.GameContext;
import engine.game.defaultge.level.IStageEngine;
import engine.game.defaultge.level.StageEngine;
import engine.game.defaultge.level.type1.entity.PathfindingTester;
import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.game.defaultge.level.type1.states.DungeonCrawlingState;
import engine.misc.util2d.position.IMotionModifier;
import engine.misc.util2d.position.NoModifier;
import engine.misc.util2d.position.PrecisionModifier;
import engine.render.engine2d.RenderableList;
import engine.render.engine2d.temp.ITreeNodeRenderable;
import engine.state.State;
import engine.state.StateCore;
import main.events.StepEvent;
import my.util.Cardinal;
import my.util.Keys;
import my.util.Log;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;

public class StageType1 extends StageEngine implements ITreeNodeRenderable {

	protected Room[][] floor;
	public Point current = new Point(StageGenerator.fcentx, StageGenerator.fcenty);
	protected PlayerEntityV3 player;
	public StageVisualMap map = new StageVisualMap();

	public StageContext scontext;

	// public Scene topscene = new Scene();
	public RenderableList scene = new RenderableList();

	public StateCore guifsm = new StateCore();

	// public State e;

	// public State e;

	public StageType1(GameContext gcontext) {
		super();

		this.scontext = new StageContext(gcontext, this);
		this.floor = StageGenerator.genFloor(this);
		this.player = new PlayerEntityV3(scontext);

		guifsm.add(new DungeonCrawlingState(this));
	}

	@Override
	public IStageEngine getNext() {
		return this.scontext.gcontext.getTree().getNext();
	}

	@Override
	public void start(DefaultGameEngine ge) {
		this.scene.setVisible(true);
		this.setCamOnRoom(this.current.x, this.current.y);

		this.getCurrent().playerEnter(Cardinal.north, this.player);
		ge.startDefaultLoop(this);
	}

	public void moveRoom(Cardinal dir) {

		/////////////////////////// RN////////////
		Room pre = this.getCurrent();
		int mx = dir.toXMultiplier();
		int my = dir.toYMultiplier();
		if (this.floor[current.x + mx][current.y + my] == null) {
			Log.log(this, "mur mangé");
			return;
		}
		this.current.x += mx;
		this.current.y += my;

		Room post = this.getCurrent();
		State betweenroom = new State(null, (ITreeNodeRenderable) (wt, res, time, px, py, vx, vy) -> {
			this.scene.prepare(wt, res, time, px, py, vx, vy);
		});
		this.guifsm.add(betweenroom);
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
			this.setCamOnRoom(current.x, current.y);
			this.scene.setModifier(new NoModifier());
		}).endAndUse((ev) -> scontext.gcontext.EventE.chaotic.add(ev));
	}

	public Room getCurrent() {
		return this.floor[current.x][current.y];
	}

	public void setCamOnRoom(int x, int y) {
		scene.setPos(-x * StageGenerator.cyclex, -y * StageGenerator.cycley);
	}

	public void moveCamByOffset(int x, int y, long time) {
		IMotionModifier omod = this.scene.getModifier();
		long end = (long) omod.getMaxTime();
		IPoint pos = this.scene.getPos();
		scene.setPos(pos.getX() + (int) omod.getModX(end), pos.getY() + (int) omod.getModY(end));
		long now = System.currentTimeMillis();
		int pmx = -x * StageGenerator.cyclex;
		int pmy = -y * StageGenerator.cycley;
		PrecisionModifier nmod = new PrecisionModifier(time, now, pmx, pmy);
		this.scene.setModifier(nmod);
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

	@Override

	public void prepare(IWaitlist wt, int res, long time, double px, double py, double vx, double vy) {
		// this.topscene.render(r, g, time, scx, scy);
		this.guifsm.prepare(wt, res, time, px, py, vx, vy);
	}
}