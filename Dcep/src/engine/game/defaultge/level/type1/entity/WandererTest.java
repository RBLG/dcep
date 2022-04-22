package engine.game.defaultge.level.type1.entity;

import java.util.EnumMap;
import java.util.function.Consumer;

import debug.PathVisualiser;
import engine.entityfw.IEntityV3;
import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasVisuals;
import engine.entityfwp2.ai.BehaviorCore;
import engine.entityfwp2.ai.Blackboard;
import engine.entityfwp2.ai.ConditionNode;
import engine.entityfwp2.ai.IHasBehaviours;
import engine.entityfwp2.ai.SequenceNode;
import engine.entityfwp2.ai.action.IAction.Status;
import engine.entityfwp2.ai.condition.DoForRandomDuration;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.StageType1;
import engine.game.defaultge.level.type1.entity.actions.GoSomewhereInRange;
import engine.physic.basic2Dattacks.IAttackable;
import engine.physic.basic2Dattacks.IHasAttackables;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.INextMotionProvider;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionListener;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.Gauge;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;
import my.util.geometry.floats.IFloatVector;
import my.util.geometry.floats.IFloatVector.FloatVector;
import res.visual.FolderVideos;

public class WandererTest implements IEntityV3, IHasVisuals, IHasCollidable, IHasAttackables, IHasBehaviours, //
	IAttackable, //
	IOnCollisionComputedListener, IOnCollisionListener, INextMotionProvider {

	protected MapGraphicEntity<PlayerVState> visual1;
	protected HitBoxBasedModifier mod;
	public StageContext scontext;
	protected BasicV2PlayerInput motprov;
	protected MovingBox hitbox;
	protected Room room;

	public WandererTest(StageType1 stage, Room nroom, IPoint npt) {
		room = nroom;
		scontext = stage.scontext;

		this.hitbox = new MovingBox(npt.getX(), npt.getY(), 20, 17, this, this, this);
		this.mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);

		EnumMap<PlayerVState, I2DRenderable> e = new EnumMap<>(PlayerVState.class);
		DrawLayer layer = DrawLayer.Room_Entities;
		e.put(PlayerVState.up_move, new LoopingAnimation(FolderVideos.player_redbox_move_up.get(), layer));
		e.put(PlayerVState.down_move, new LoopingAnimation(FolderVideos.player_redbox_move_down.get(), layer));
		e.put(PlayerVState.left_move, new LoopingAnimation(FolderVideos.player_redbox_move_left.get(), layer));
		e.put(PlayerVState.right_move, new LoopingAnimation(FolderVideos.player_redbox_move_right.get(), layer));
		e.put(PlayerVState.up_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_up.get(), layer));
		e.put(PlayerVState.down_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_down.get(), layer));
		e.put(PlayerVState.left_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_left.get(), layer));
		e.put(PlayerVState.right_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_right.get(), layer));
		this.visual1 = new MapGraphicEntity<>(new Point(0, -23), PlayerVState.down_stand, e, layer);
		this.visual1.setModifier(mod);

		bboard.add("room", nroom);
		bboard.add("idle", false);
		bboard.add("pathvisualiser", this.pathvis);
		behaviours = new BehaviorCore(//
			new SequenceNode(//
				new ConditionNode(() -> bboard.isTrue("idle", true), //
					new DoForRandomDuration(bboard, () -> {

						return Status.running;
					}, 0, 4000)), //
				new GoSomewhereInRange(bboard, 200, hitbox, this::setNextVec))//
		);

	}

	@Override
	public void forEachCollidables(Consumer<MovingBox> task) {
		task.accept(hitbox);
	}

	@Override
	public void forEachVisuals(Consumer<I2DRenderable> task) {
		task.accept(visual1);
		task.accept(pathvis);
	}

	@Override
	public void onCollision(MovingBox box) {
		return;
	}

	protected Cardinal lastdir = Cardinal.south;

	public void onCollisionComputed(MovingBox box) {
		this.mod.resetBeginning();
		IFloatVector vec = this.hitbox.getVec();
		boolean mov = false;
		if (Math.abs(vec.getY()) > Math.abs(vec.getX())) {
			if (vec.getY() != 0) {
				mov = true;
				this.lastdir = (vec.getY() > 0) ? Cardinal.south : Cardinal.north;
			}
		} else {
			if (vec.getX() != 0) {
				mov = true;
				this.lastdir = (vec.getX() > 0) ? Cardinal.east : Cardinal.west;
			}
		}
		this.visual1.set(PlayerVState.concat(mov, this.lastdir));
	}

	// protected Path path;

	protected PathVisualiser pathvis = new PathVisualiser(this.hitbox);

	protected FloatVector nextvec = new FloatVector(0, 0);

	protected void setNextVec(FloatVector nvec) {
		nextvec = nvec;
	}

	@Override
	public FloatVector getNextMotionVector(MovingBox box) {
		return nextvec;
	}

	@Override
	public void forEachAttackables(Consumer<IAttackable> task) {
		task.accept(this);
	}

	@Override
	public IRectangle getZone() {
		return this.hitbox.toInt();
	}

	protected Gauge hp = new Gauge(0, 100, 100);

	@Override
	public void receiveAttack(Enum<?> group, int dmg, Object effect) {
		hp.substract(dmg);
		if (hp.get() <= 0) {
			this.die();
		}
	}

	protected void die() {
		scontext.getGcontext().EventE.cleanup.add((time) -> {
			room.getEWS().remove(this);
		});
	}

	protected Blackboard bboard = new Blackboard();

	protected BehaviorCore behaviours;

	@Override
	public void think(Consumer<BehaviorCore> task) {
		this.behaviours.run(0);
	}

}
