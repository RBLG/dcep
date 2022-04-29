package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Consumer;
import engine.entityfwp2.ai.action.ParamAction;
import engine.entityfwp2.ai.action.ParamAction.ParamReceiver;
import engine.entityfwp2.ai.action.ParamAction.ParamSupplier;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import my.util.geometry.floats.IFloatVector;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class MoveToPoint implements ParamReceiver<Path> {

	protected MovingBox box;

	protected Path path;

	protected Consumer<FloatVector> output;

	public MoveToPoint(MovingBox nbox, Consumer<FloatVector> noutput) {
		box = nbox;
		output = noutput;
	}

	@Override
	public Status doOngoing() {
		if (path == null) {
			return Status.notdoable;
		}
		path.MoveToNextStepIfDone(box.getXY());
		if (path.isDone(box.toOutInt())) {
			this.path = null;
			output.accept(new FloatVector(0, 0));
			return Status.completed;
		}
		IFloatVector vec = path.getShortTermVector(box.getXY(), 3);
		output.accept(new FloatVector(vec.getX(), vec.getY()));
		return Status.running;
	}

	@Override
	public void receive(Path npath) {
		path = npath;
	}

	public ParamAction<Path> to(ParamSupplier<Path> suppl) {
		return suppl.then(this);
	}

}
