package engine.game.defaultge.level.type1.entity;

import java.util.EnumMap;
import java.util.Random;
import java.util.function.Consumer;

import engine.entityfw.IEntityV3;
import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasVisuals;
import engine.game.defaultge.level.type1.GameTick;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.StageType1;
import engine.physic.basic2Dattacks.IAttackable;
import engine.physic.basic2Dattacks.IHasAttackables;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.INextMotionProvider;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionListener;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import main.events.DelayedEvent;
import my.util.Cardinal;
import my.util.Gauge;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;
import my.util.geometry.floats.IFloatVector;
import my.util.geometry.floats.IFloatVector.FloatVector;
import res.visual.FolderVideos;

public class WandererTest implements IEntityV3, IHasVisuals, IHasCollidable, IHasAttackables, //
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
		e.put(PlayerVState.right_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_right.get(),layer));
		this.visual1 = new MapGraphicEntity<>(new Point(0, -23), PlayerVState.down_stand, e, layer);
		this.visual1.setModifier(mod);

	}

	@Override
	public void forEachCollidables(Consumer<MovingBox> task) {
		task.accept(hitbox);
	}

	@Override
	public void forEachVisuals(Consumer<I2DRenderable> task) {
		task.accept(visual1);
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

	protected Path path;

	protected INextMotionProvider mpstate = this::onMovingMotionProvider;

	@Override
	public FloatVector getNextMotionVector(MovingBox box) {
		return mpstate.getNextMotionVector(box);
	}

	public FloatVector onWaitingMotionProvider(MovingBox box) {

		return new FloatVector(0, 0);
	}

	public FloatVector onMovingMotionProvider(MovingBox box) {
		if (path == null) {
			if (this.room != null) {
				PathFinder pf = this.room.getPathfinder();
				path = pf.getPathToRandomPointInWalkRange(this.hitbox.toOutInt(), 200);
			}
		} else {
			path.checkStuckness(this.hitbox.toOutInt().getXY());
			path.MoveToNextStepIfDone(this.hitbox.toOutInt());
			if (path.isDone(this.hitbox.toOutInt())) {
				this.mpstate = this::onWaitingMotionProvider;
				//////////////////////////////////
				scontext.getGcontext().EventE.chaotic
						.add(new DelayedEvent(GameTick.fromMillis((new Random()).nextInt(4000)), (time) -> {
							this.mpstate = this::onMovingMotionProvider;
						}));
				/////////////////////////////////////
				this.path = null;
				return new FloatVector(0, 0);
			}
			IFloatVector vec = path.getShortTermVector(this.hitbox.toOutInt(), 3);
			return new FloatVector(vec.getX(), vec.getY());
		}
		return new FloatVector(0, 0);
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

}
