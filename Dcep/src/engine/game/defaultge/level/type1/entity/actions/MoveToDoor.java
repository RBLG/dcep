package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Consumer;
import java.util.function.Supplier;

import engine.entityfwp2.ai.action.IAction;
import engine.entityfwp2.ai.action.ScriptAction;
import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.save.room.type1.Side;
import my.util.Cardinal;
import my.util.Field;
import my.util.Log;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class MoveToDoor implements IAction {
	protected Supplier<Room> room;
	protected Supplier<Cardinal> dir;
	protected MovingBox box;

	public MoveToDoor(Supplier<Room> nroom, Supplier<Cardinal> ndir, MovingBox nbox, Consumer<FloatVector> noutput) {
		room = nroom;
		dir = ndir;
		box = nbox;
		output = noutput;

		this.genScript();
	}

	@Override
	public void start() {
		this.script.start();
	}

	protected Consumer<FloatVector> output;
	protected ScriptAction script;

	protected Field<Path> path = new Field<>();

	private void genScript() {
		script = ScriptAction.New()//
			.then(() -> {
				path.set(room.get().getPathToDoor(box.toOutInt(), Side.as(dir.get())));
				if (path.get() == null) {
					Log.log(this, "path null");
					return Status.notdoable;
				}
				return Status.completed;
			})//
			.then(new FollowPath(box, output, path))//
			.end();
	}

	@Override
	public Status doOngoing() {
		return script.doOngoing();
	}

}
