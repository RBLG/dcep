package engine.game.defaultge.level.type1;

import java.awt.Point;
import java.util.ArrayList;
import engine.game.defaultge.DefaultGameEngine;
import engine.game.defaultge.GameContext;
import engine.game.defaultge.level.IStageEngine;
import engine.game.defaultge.level.StageEngine;
import engine.game.defaultge.level.type1.entity.PathfindingTester;
import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.loop.ILoopable;
import engine.loop.TimedAction;
import engine.misc.util2d.position.IMotionModifier;
import engine.misc.util2d.position.PrecisionModifier;
import engine.render.engine2d.Basic2DEngine;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.Scene;
import engine.render.misc.DcepJFrame;
import my.util.Cardinal;
import my.util.EnumMapList;
import my.util.Keys;
import my.util.Log;

public class StageType1 extends StageEngine<Basic2DEngine> {

	protected Room[][] floor;
	protected Point current = new Point(StageGenerator.fcentx, StageGenerator.fcenty);
	protected PlayerEntityV3 player;
	// protected IStageTree tree;
	// protected PlayerSoul plrstats;
	protected StageMap map = new StageMap();

	protected StageContext scontext;

	protected Scene scene = new Scene();

	/***
	 * le type de gameplay en cours
	 */
	protected Doing doing = Doing.inroom;
	/***
	 * les actions a faire en fonction du type de gameplay
	 */
	protected EnumMapList<Doing, ILoopable> todos = new EnumMapList<>(Doing.class);

	/***
	 * types de "gameplay" possibles
	 * 
	 * @author RobinL
	 *
	 */
	public static enum Doing {
		inroom, betweenroom, indialogue, incinematic, inmap, inhack;
	}

	public StageType1(GameContext gcontext) {
		super(new Basic2DEngine(DcepJFrame.staticadapter));
		
		this.scontext = new StageContext(gcontext,this);
		this.floor = StageGenerator.genFloor(this);
		this.player = new PlayerEntityV3(scontext);
		this.todos.add(Doing.inroom, this::goInRoom);

		this.map.img.setVisible(false);
		this.scene.add(this.map.img, DrawLayer.Map);
	}

	@Override
	public IStageEngine getNext() {
		return this.scontext.gcontext.getTree().getNext();
	}

	@Override
	public void start(DefaultGameEngine ge) {
		this.ren.setScene(this.scene);
		this.scene.setVisible(true);
		this.setCamOnRoom(this.current.x, this.current.y);

		this.getCurrent().playerEnter(Cardinal.north, this.player);
		ge.startDefaultLoop(this);
	}

	public void moveRoom(Cardinal dir) {
		ArrayList<TimedAction> actions = new ArrayList<>();
		this.todos.get(Doing.betweenroom).clear();

		/////////////////////////// RN////////////
		Room pre, post;
		pre = this.getCurrent();
		this.doing = Doing.betweenroom;
		int mx = 0, my = 0;
		mx = dir.toXMultiplier();
		my = dir.toYMultiplier();

		this.current.x += mx;
		this.current.y += my;
		post = this.getCurrent();
		// Log.log(this, "tiles:"+post.state.navmesh.size());
		// int npx = mx * borderx, npy = my * bordery;
		// this.player.setGuided(true);
		// this.player.doGuidedMove(200, npx, npy);
		this.moveCamByOffset(mx, my, 600);
		if (this.getCurrent() == null) {
			Log.log(this, "a mangé un mur en:" + this.current.x + "/" + this.current.y + "\n");
			throw new RuntimeException("tentative d'aller dans une salle qui est un mur");
		}
		//////////////// ENTRE L'ENTRE ET SORTIE/////////
		actions.add(new TimedAction(300, (long time) -> {
			pre.update(time);
			pre.playerLeave(this.player);
			post.playerEnter(dir, this.player);
			post.update(time);
//			int nx = player.getHitbox().getX(), ny = player.getHitbox().getY();
//			
			// nx = dir.toXMultiplier();
			// ny = dir.toYMultiplier();
//			player.setPos(nx, ny);

			// this.player.doGuidedMove(200, npx, npy);
		}));

		/////////////// FIN////////////////////
		actions.add(new TimedAction(650, (long time) -> {
			this.doing = Doing.inroom;
			// this.player.setGuided(false);
		}));
		//////////////////////////////////////

		// this.todos.add(Doing.betweenroom, pre::go);
		// this.todos.add(Doing.betweenroom, post::go);
		this.todos.get(Doing.betweenroom).addAll(actions);
	}

	@Override
	public void go(long time) {
		for (ILoopable todo : this.todos.get(this.doing)) {
			todo.go(time);
		}
	}

	public void goInRoom(long time) {
		this.getCurrent().update(time);
		// this.player.think();
		/////////////////////////////////////////////////////////////
		if (scontext.getInputE().isPressed(Keys.space.value)) {
			PathfindingTester pft = new PathfindingTester(this);
			this.getCurrent().enter(Cardinal.north, pft);
			Log.log(this, "new tester");
		}
		/////////////////////////////////////////////////////////////
		if (scontext.getInputE().isPressed(Keys.tab.value)) {
			this.openMap();
		}
	}

	public void openMap() {
		this.doing = Doing.inmap;
		this.map.img.getPos().getPos().x = this.current.x * StageGenerator.cyclex + 30;
		this.map.img.getPos().getPos().y = this.current.y * StageGenerator.cycley + 30;
		this.map.img.setVisible(true);
		this.todos.add(Doing.inmap, (time) -> {
			if (!scontext.getInputE().isActive(Keys.tab.value)) {
				this.doing = Doing.inroom;
				this.map.img.setVisible(false);
			}
		});
	}

	public Room getCurrent() {
		return this.floor[current.x][current.y];
	}

	public Scene getCurrentScene() {
		return this.getCurrent().getScene();
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

}