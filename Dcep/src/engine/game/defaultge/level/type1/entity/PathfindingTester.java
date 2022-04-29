package engine.game.defaultge.level.type1.entity;

import java.awt.Color;
import java.util.function.Consumer;

import debug.PathVisualiser;
import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasVisuals;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageType1;
import engine.game.defaultge.level.type1.interactions.IRoomTraverserEntity;
import engine.physic.basic2Dattacks.IAttackable;
import engine.physic.basic2Dattacks.IHasAttackables;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionComputedListener;
import engine.physic.basic2Dvectorial.MovingBox.IOnCollisionListener;
import engine.physic.basic2Dvectorial.motionprovider.IMotionProvider;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.Rectangle;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.Gauge;
import my.util.geometry.IPoint;
import my.util.geometry.IRectangle;
import my.util.geometry.floats.IFloatVector;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class PathfindingTester implements IRoomTraverserEntity, IHasVisuals, IHasCollidable, //
		IOnCollisionComputedListener, IOnCollisionListener, IMotionProvider, //
		IHasAttackables, IAttackable {

	protected MovingBox hitbox;
	protected Room proom;
	protected HitBoxBasedModifier mod;
	protected StageType1 stage;
	protected Rectangle visual = new Rectangle(0, 0, 20, 17, Color.YELLOW, DrawLayer.Room_Entities);
	protected Rectangle goal = new Rectangle(0, 0, 3, 3, Color.red, DrawLayer.Room_Entities);
	protected Rectangle nextonpath = new Rectangle(0, 0, 20, 17, new Color(255, 100, 0, 170), DrawLayer.Room_Entities);
	protected PathVisualiser pathvis = new PathVisualiser(this.hitbox);

	public PathfindingTester(StageType1 nstage) {

		stage = nstage;
		hitbox = new MovingBox(0, 0, 20, 17, this, this, this);
		mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);
		this.visual.setModifier(mod);

		this.hitbox.setChaotic(false);
	}

	protected Path path;
	protected boolean done = false;

	@Override
	public FloatVector getNextMotionVector(MovingBox box) {
		IRectangle intrec = hitbox.toInt();
		if (done) {
			return new FloatVector(0, 0);
		}
		this.think();
		// Log.log(this, "path:" + path);
		if (path != null) {
			path.checkStuckness(intrec.getXY());
			path.MoveToNextStepIfDone(this.hitbox.getXY());
			if (path.abort) {
				this.visual.setColor(Color.BLACK);
				this.done = true;
				return new FloatVector(0, 0);
			}
			IFloatVector unlimited = path.getCurrentVector(this.hitbox.getXY());
			this.nextonpath.setPos((int) (intrec.getX() + unlimited.getX()), (int) (intrec.getY() + unlimited.getY()));
			IFloatVector vec = path.getShortTermVector(this.hitbox.getXY(), 7);
			return new FloatVector(vec.getX(), vec.getY());
		}
		return new FloatVector(0, 0);
	}

	public void think() {
		if (path == null || path.isDone(this.hitbox.toOutInt())) {
			if (this.proom != null) {
				PathFinder pf = this.proom.getPathfinder();
				IPoint next = pf.getRandomPoint(this.hitbox.toInt());
				this.goal.setPos(next.getX(), next.getY());
				path = pf.getPathFromTo(this.hitbox.toInt().getXY(), next, this.hitbox.toInt());
				this.pathvis.updatePath(path);
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
		IPoint pt = room.getPathfinder().getRandomPoint(this.hitbox.toInt());
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
		task.accept(pathvis);
	}

	@Override
	public void forEachAttackables(Consumer<IAttackable> task) {
		task.accept(this);
	}

	@Override
	public IRectangle getZone() {
		return this.hitbox.toInt();
	}

	protected Gauge hp = new Gauge(0, 50, 100);

	@Override
	public void receiveAttack(Enum<?> group, int dmg, Object effect) {
		hp.substract(dmg);
		if (hp.get() <= 0) {
			this.die();
		}
	}

	protected void die() {
		stage.scontext.getGcontext().EventE.cleanup.add((time) -> {
			proom.getEWS().remove(this);
		});
	}

}
