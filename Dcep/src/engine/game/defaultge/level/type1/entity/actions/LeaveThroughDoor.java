package engine.game.defaultge.level.type1.entity.actions;

import java.util.function.Supplier;

import engine.entityfwp2.ai.action.IAction;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.entity.IRoomTraverserEntity;
import my.util.Cardinal;
import my.util.Field;
import my.util.Log;
import my.util.geometry.IPoint;

public class LeaveThroughDoor implements IAction {
	protected Supplier<Room> room;
	protected Field<Cardinal> dir;
	protected StageContext scontext;
	protected IRoomTraverserEntity ent;

	public LeaveThroughDoor(Supplier<Room> nroom, Field<Cardinal> ndir, StageContext nscontext,
		IRoomTraverserEntity nent) {
		room = nroom;
		dir = ndir;
		scontext = nscontext;
		ent = nent;
	}

	@Override
	public Status doOngoing() { // TODO rework code puant
		Cardinal dir = this.dir.get();
		Room room = this.room.get();
//		if (room.doors2[Side.as(dir).ordinal()] == DoorType.wall) {
//			Log.log(this, "mangeage de mur normal");
//			this.dir.set(null);
//			return Status.notdoable;
//		}
		if (dir == null) {
			Log.log(this, "dir null");
			return Status.notdoable;
		}
//		
		IPoint pt = room.pos;
		int ptx = pt.getX() + dir.toXMultiplier();
		int pty = pt.getY() + dir.toYMultiplier();
		Room next = scontext.getStageE().floor[ptx][pty];
		if (next == null) {
			Log.log("dir erronée");
			this.dir.set(null);
			return Status.notdoable;
		}
		scontext.getGcontext().EventE.cleanup.add((t) -> {
			room.leave(ent);
			next.enter(dir, ent);
			room.visualupdate(t); // TODO fix pour pas a avoir a le faire ici
			next.visualupdate(t);
		});
		this.dir.set(null);
		return Status.completed;
	}

}
