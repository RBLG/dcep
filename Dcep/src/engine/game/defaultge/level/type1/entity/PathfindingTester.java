package engine.game.defaultge.level.type1.entity;

import java.awt.Color;
import java.util.function.Consumer;

import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasVisuals;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageType1;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionListener;
import engine.physic.basic2Dvectorial.motionprovider.IMotionProvider;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.Rectangle;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.geometry.IPoint;
import my.util.geometry.IVector;

public class PathfindingTester implements IRoomTraverserEntity, IHasVisuals, IHasCollidable, //
		IOnCollisionComputedListener, IOnCollisionListener, IMotionProvider {

	protected MovingBox hitbox;
	protected Room proom;
	protected HitBoxBasedModifier mod;
	protected StageType1 stage;
	protected Rectangle visual= new Rectangle(0, 0, 20, 20, Color.YELLOW);
	protected Rectangle goal = new Rectangle(0, 0, 3, 3, Color.red);
	protected Rectangle nextonpath = new Rectangle(0, 0, 20, 20, new Color(255, 100, 0, 170));

	public PathfindingTester(StageType1 nstage) {

		stage = nstage;
		hitbox = new MovingBox(0, 0, 20, 20, this, this, this);
		mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);
		this.visual.getPos().setModifier(mod);

		this.hitbox.setChaotic(false);
	}

	protected Path path;

	@Override
	public MotionVector getNextMotionVector(MovingBox box) {
		this.think();
		// Log.log(this, "path:" + path);
		if (path != null) {
			path.checkStuckness(this.hitbox.getXY());
			path.MoveToNextStepIfDone(this.hitbox);

			IVector unlimited = path.getCurrentVector(this.hitbox);
			this.nextonpath.getPos().getPos().move(this.hitbox.getX(), this.hitbox.getY());
			this.nextonpath.getPos().getPos().translate(unlimited.getX(), unlimited.getY());
			IVector vec = path.getShortTermVector(this.hitbox, 7);
			return new MotionVector(vec.getX(), vec.getY());
		}
		return new MotionVector(0, 0);
	}

	public void think() {
		if (path == null || path.isDone(this.hitbox)) {
			if (this.proom != null) {
				PathFinder pf = this.proom.getPathfinder();
				IPoint next = pf.getRandomPoint(this.hitbox);
				this.goal.getPos().setPos(new java.awt.Point(next.getX(), next.getY()));
				path = pf.getPathFromTo(this.hitbox.getCenter(), next, this.hitbox);
			}
		}
	}

	@Override
	public void onCollision(MovingBox box) {

	}

	@Override
	public void onCollisionComputed(MovingBox box) {
		this.mod.resetBeginning();

	}

	@Override
	public void enter(Room room, Cardinal dir) {
		this.proom = room;
		IPoint pt = room.getPathfinder().getRandomAccessiblePoint(this.hitbox);
		this.hitbox.setX(pt.getX());
		this.hitbox.setY(pt.getY());
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
		task.accept(this.goal);
	}

}
