package engine.game.defaultge.level.type1.entity.actions;

import engine.entityfwp2.ai.Blackboard;
import engine.entityfwp2.ai.action.ParamAction.ParamReceiver;
import engine.entityfwp2.ai.action.ScriptAction;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.StagePath;
import engine.game.defaultge.level.type1.StageType1;
import engine.game.defaultge.level.type1.interactions.IStageWalker;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.save.room.type1.Side;
import my.util.Cardinal;
import my.util.Field;
import my.util.geometry.IPoint.Point;
import my.util.geometry.floats.IFloatVector.FloatVector;

public class MoveOutOfRoom implements ParamReceiver<StagePath> {

	public MoveOutOfRoom(WalkBehaviorPack npack) {
		pack = npack;
		genBehavior();
		genBehavior2();
	}

	protected WalkBehaviorPack pack;

	protected ScriptAction behav;

	private void genBehavior() {
		behav = ScriptAction.New()//
			.then(new MoveToPoint(pack.box, (vec) -> pack.vec.set(vec))//
				.to(() -> {
					Cardinal dir = pack.spath.get().next();
					return pack.getRoom().getPathToDoor(pack.box.toOutInt(), Side.fromCardinal(dir).toOposite());
				}))//
			.then(() -> {
				pack.getRoom().leave(pack.entity);
				pack.getStage().otherents.enter(pack.entity);
				return Status.completed;
			})//
			.end();
	}

	protected ScriptAction behav2;

	private void genBehavior2() {
		behav = ScriptAction.New()//
			.waitFor(1000)//
			.then(() -> {
				Cardinal dir = pack.spath.get().next();
				Point pt = pack.room.get();
				pt.x += dir.toXMultiplier();
				pt.y += dir.toYMultiplier();
				return Status.completed;
			})//
			.end();
	}

	@Override
	public Status doOngoing() {
		if (pack.getRoom() != null) {
			return behav.doOngoing();
		} else {
			return behav.doOngoing();
		}
	}

	@Override
	public void start() {
		behav.start();
	}

	@Override
	public void receive(StagePath nspath) {
		pack.spath.set(nspath);
	}

	public static class WalkBehaviorPack {

		public final Field<FloatVector> vec = new Field<>(new FloatVector(0, 0));
		public final Field<StagePath> spath = new Field<>(null);
		public final Field<Point> room;
		public final MovingBox box;
		public final StageContext context;
		public final IStageWalker entity;
		public final Blackboard bboard;

		public WalkBehaviorPack(Field<Point> nroom, MovingBox hitbox, StageContext ncontext, IStageWalker nentity,
			Blackboard nbboard) {
			room = nroom;
			box = hitbox;
			context = ncontext;
			entity = nentity;
			bboard = nbboard;
		}

		public StageType1 getStage() {
			return context.getStageE();
		}

		public Room getRoom() {
			return context.getStageE().stagemap.get(room.get());
		}
	}

}
