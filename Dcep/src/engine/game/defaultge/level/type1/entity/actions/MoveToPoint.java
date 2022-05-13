package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Consumer;
import java.util.function.Supplier;

import engine.entityfwp2.ai.action.IAction;
import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import my.util.Log;
import my.util.geometry.IPoint.Point;
import my.util.geometry.floats.IFloatPoint;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class MoveToPoint implements IAction {

	public MoveToPoint(Supplier<Room> nroom, MovingBox nbox, Consumer<FloatVector> noutput, Supplier<Point> ngoal) {
		box = nbox;
		output = noutput;
		room = nroom;
		goal = ngoal;
	}

	protected Supplier<Point> goal;
	protected Supplier<Room> room;
	protected MovingBox box;
	protected Path path;

	protected Consumer<FloatVector> output;

	@Override
	public void start() {

	}

	@Override
	public Status doOngoing() {
		box.applyMotion();
		IFloatPoint xy = box.getXY();
		if (path == null) {
			if (room == null) {
				Log.log(this, "chemin pas faisable");
				return Status.notdoable;
			}
			path = room.get().getPathfinder().getPathFromTo(box.getX2Y().toInt(), goal.get(), box.toInt());
		}
		if (path == null) {
			output.accept(new FloatVector(0, 0));
			Log.log(this, "chemin pas reussi");
			return Status.running;
		}
		path.checkStuckness(xy.toInt());
		path.MoveToNextStepIfDone(xy);
		if (path.isDone(box.toOutInt())) {
			this.path = null;
			output.accept(new FloatVector(0, 0));
			return Status.completed;
		}
		output.accept(path.getShortTermVector(xy, 3));
		return Status.running;

	}

}
