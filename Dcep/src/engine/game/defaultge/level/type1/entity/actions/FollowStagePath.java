package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Consumer;
import java.util.function.Supplier;

import engine.entityfwp2.ai.action.IAction;
import engine.entityfwp2.ai.action.ScriptAction;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.StagePath;
import engine.game.defaultge.level.type1.entity.IRoomTraverserEntity;
import engine.physic.basic2Dvectorial.MovingBox;
import my.util.Cardinal;
import my.util.Field;
import my.util.Log;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class FollowStagePath implements IAction {

	Field<Cardinal> dir = new Field<>();

	public FollowStagePath(Field<StagePath> spath, Supplier<Room> room, MovingBox box, Consumer<FloatVector> output,
		StageContext scontext, IRoomTraverserEntity ent) {

		script = ScriptAction.New()//
			.then(() -> {
				if (spath.is(null)) {
					Log.log("path null");
					return Status.notdoable;
				}
				return Status.completed;
			})//
			.While(() -> spath.get().isDone(), (e) -> {
				e.then(() -> {
					dir.set(spath.get().next());
					Log.log("step suivante");
				})//
					.then(new MoveToDoor(room, dir, box, output))//
					.then(new LeaveThroughDoor(room, dir, scontext, ent));
			})//
			.then(() -> {
				spath.set(null);
			})//
			.end();
	}

	protected ScriptAction script;

	@Override
	public Status doOngoing() {
		return script.doOngoing();
	}

	@Override
	public void start() {
		dir.set(null);
		script.start();
	}

}
