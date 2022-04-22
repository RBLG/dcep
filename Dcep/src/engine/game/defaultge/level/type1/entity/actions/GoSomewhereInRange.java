package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Consumer;

import debug.PathVisualiser;
import engine.entityfwp2.ai.Blackboard;
import engine.entityfwp2.ai.action.IAction;
import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import my.util.geometry.floats.IFloatPoint;
import my.util.geometry.floats.IFloatVector;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class GoSomewhereInRange implements IAction {

	public GoSomewhereInRange(Blackboard nboard, int nrange, MovingBox nbox, Consumer<FloatVector> noutput) {
		board = nboard;
		range = nrange;
		box = nbox;
		output = noutput;
	}

	protected int range;

	protected Blackboard board;

	protected MovingBox box;

	protected Path path;

	protected Consumer<FloatVector> output;

	@Override
	public Status doOngoing() {
		box.applyMotion();
		Room room = board.getOrNull("room", Room.class);
		IFloatPoint xy = box.getXY();
		if (path == null) {
			if (room != null) {
				PathFinder pf = room.getPathfinder();
				path = pf.getPathToRandomPointInWalkRange(box.toOutInt(), range);
				board.computeIfThere("pathvisualiser", PathVisualiser.class, (pv) -> {
					pv.updatePath(path);
				});
			}
		} else {
			path.checkStuckness(xy.toInt());
			path.MoveToNextStepIfDone(xy);
			if (path.isDone(box.toOutInt())) {
				this.path = null;
				output.accept(new FloatVector(0, 0));
				return Status.completed;
			}
			IFloatVector vec = path.getShortTermVector(xy, 3);
			output.accept(new FloatVector(vec.getX(), vec.getY()));
			return Status.running;
		}
		output.accept(new FloatVector(0, 0));
		return Status.running;
	}

}
