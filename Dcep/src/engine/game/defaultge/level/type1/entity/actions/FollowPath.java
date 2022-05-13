package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Consumer;
import engine.entityfwp2.ai.action.IAction;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import my.util.Field;
import my.util.Log;
import my.util.geometry.floats.IFloatPoint;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class FollowPath implements IAction {

	public FollowPath(MovingBox nbox, Consumer<FloatVector> noutput, Field<Path> npath) {
		box = nbox;
		output = noutput;
		path = npath;
	}

	protected MovingBox box;
	protected Field<Path> path;
	protected Consumer<FloatVector> output;

	@Override
	public void start() {
		
	}

	@Override
	public Status doOngoing() {
		box.applyMotion();
		Path path = this.path.get();
		if (path == null) {
			Log.log(this, "wot");
			output.accept(new FloatVector(0, 0));
			this.path.set(null);
			return Status.notdoable;
		}
		IFloatPoint xy = box.getXY();
		path.MoveToNextStepIfDone(xy);
		if (path.isDone(box.toOutInt())) {
			output.accept(new FloatVector(0, 0));
			this.path.set(null);
			return Status.completed;
		}
		output.accept(path.getShortTermVector(xy, 3));
		return Status.running;
	}
}
