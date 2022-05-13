package engine.game.defaultge.level.type1.entity;

import java.awt.Color;
import java.util.function.Consumer;

import engine.entityfw.IEntityV3;
import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasVisuals;
import engine.entityfwp2.ai.BehaviorCore;
import engine.entityfwp2.ai.Blackboard;
import engine.entityfwp2.ai.IHasBehaviours;
import engine.entityfwp2.ai.action.ScriptAction;
import engine.entityfwp2.ai.nodes.CheesyRepeatNode;
import engine.game.defaultge.level.type1.IBgModeNeeder;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.StageMap;
import engine.game.defaultge.level.type1.StagePath;
import engine.game.defaultge.level.type1.StageType1;
import engine.game.defaultge.level.type1.entity.actions.FollowStagePath;
import engine.game.defaultge.level.type1.entity.actions.GoSomewhereInRange;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.Rectangle;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.Field;
import my.util.Log;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class LeaverTester implements IEntityV3, IHasVisuals, IHasCollidable, IHasBehaviours, //
	IBgModeNeeder, IRoomTraverserEntity, IOnCollisionComputedListener {

	protected HitBoxBasedModifier mod;
	public StageContext scontext;
	protected BasicV2PlayerInput motprov;
	protected MovingBox hitbox;

	protected Rectangle visual = new Rectangle(0, 0, 20, 17, Color.YELLOW, DrawLayer.Room_Entities);

	public LeaverTester(StageType1 stage) {
		scontext = stage.scontext;

		this.hitbox = new MovingBox(0, 0, 20, 17, nextvec::get, null, this);
		mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);
		visual.setModifier(mod);
		hitbox.setChaotic(false);

		this.genBehaviours();
	}

	@Override
	public void onCollisionComputed(MovingBox box) {
		mod.resetBeginning();
	}

	public void spawn(Room nroom, IPoint npt) {
		this.room.set(nroom);
		hitbox.setXY(npt);
	}

	public void spawn(Room current) {
		this.spawn(current, current.getPathfinder().getRandomPoint(hitbox.toInt()));
	}

	@Override
	public void enter(Room nroom, Cardinal dir) {
		hitbox.applyMotion();
		room.set(nroom);
		Point newco = room.get().state.getEntryPointFromDir(dir, hitbox.toInt().getWH());
		hitbox.setXY(newco);
	}

	@Override
	public void leave(Room room) {

	}

	@Override
	public void forEachCollidables(Consumer<MovingBox> task) {
		task.accept(hitbox);
	}

	@Override
	public void forEachVisuals(Consumer<I2DRenderable> task) {
		task.accept(this.visual);
	}

	@Override
	public void think(Consumer<BehaviorCore> task) {
		behaviours.run(0);
	}

	protected Blackboard bboard = new Blackboard();
	protected BehaviorCore behaviours;
	protected Field<Room> room = new Field<>();
	protected Field<Cardinal> fdoor = new Field<>();
	protected Field<StagePath> spath = new Field<>();
	protected Field<FloatVector> nextvec = new Field<>(new FloatVector(0, 0));

	private void genBehaviours() {
		behaviours = new BehaviorCore(//
			new CheesyRepeatNode(//
				ScriptAction.New()//
					.then(() -> {
						Log.log("debut de la navigation");
						StageMap smap = scontext.getStageE().stagemap;
						IPoint start = room.get().pos;
						IPoint end = smap.getRandomRoom();
						spath.set(smap.getPath(start, end));
					})//
					.then(new FollowStagePath(spath, room, hitbox, nextvec, scontext, this))//
					.end(), //
				ScriptAction.New().then(() -> {
					Log.log("fin de la navigation");
				}).end(), //
				new GoSomewhereInRange(bboard, room, 200, hitbox, nextvec)//
			) //
		);
	}
}
