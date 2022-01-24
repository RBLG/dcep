package engine.game.defaultge.level.type1.entity;

import static my.util.ImageCache.getImages2;

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
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.INextMotionProvider;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionListener;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import main.events.DelayedEvent;
import my.util.Cardinal;
import my.util.geometry.IPoint;
import my.util.geometry.floats.IFloatVector;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class WandererTest implements IEntityV3, IHasVisuals, IHasCollidable, //
		IOnCollisionComputedListener, IOnCollisionListener, INextMotionProvider {

	protected MapGraphicEntity<PlayerVisualState> visual1;
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

		EnumMap<PlayerVisualState, I2DRenderable> e = new EnumMap<>(PlayerVisualState.class);
		e.put(PlayerVisualState.up_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/up_move")));
		e.put(PlayerVisualState.down_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/down_move")));
		e.put(PlayerVisualState.left_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/left_move")));
		e.put(PlayerVisualState.right_move, new LoopingAnimation(getImages2("stages/type1/player_redbox/right_move")));
		e.put(PlayerVisualState.up_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/up_stand")));
		e.put(PlayerVisualState.down_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/down_stand")));
		e.put(PlayerVisualState.left_stand, new LoopingAnimation(getImages2("stages/type1/player_redbox/left_stand")));
		e.put(PlayerVisualState.right_stand,
				new LoopingAnimation(getImages2("stages/type1/player_redbox/right_stand")));
		this.visual1 = new MapGraphicEntity<>(new java.awt.Point(0, -23), PlayerVisualState.down_stand, e);
		this.visual1.getPos().setModifier(mod);

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
		this.visual1.set(PlayerVisualState.concat(mov, this.lastdir));
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
		}else {
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
			IFloatVector vec = path.getShortTermVector(this.hitbox.toOutInt(),3);
			return new FloatVector(vec.getX(), vec.getY());
		}
		return new FloatVector(0, 0);
	}

}
