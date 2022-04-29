package engine.game.defaultge.level.type1.entity;

import java.util.EnumMap;
import java.util.function.Consumer;

import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasVisuals;
import engine.entityfwp2.ai.BehaviorCore;
import engine.entityfwp2.ai.Blackboard;
import engine.entityfwp2.ai.IHasBehaviours;
import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageContext;
import engine.game.defaultge.level.type1.StagePath;
import engine.game.defaultge.level.type1.StageType1;
import engine.game.defaultge.level.type1.entity.actions.MoveToRoom;
import engine.game.defaultge.level.type1.entity.actions.MoveToRoom.WalkBehaviorPack;
import engine.game.defaultge.level.type1.interactions.IRoomTraverserEntity;
import engine.game.defaultge.level.type1.interactions.IStageWalker;
import engine.physic.basic2Dattacks.IAttackable;
import engine.physic.basic2Dattacks.IHasAttackables;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.motionprovider.BasicV2PlayerInput;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.LoopingAnimation;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.misc.HitBoxBasedModifier;
import my.util.Cardinal;
import my.util.Field;
import my.util.geometry.IPoint;
import my.util.geometry.IPoint.Point;
import my.util.geometry.floats.IFloatVector.FloatVector;
import res.visual.FolderVideos;

public class LeaverTester
	implements IRoomTraverserEntity, IHasVisuals, IHasCollidable, IHasAttackables, IHasBehaviours, IStageWalker {
	protected MapGraphicEntity<PlayerVState> visual1;
	protected HitBoxBasedModifier mod;
	public StageContext scontext;
	protected BasicV2PlayerInput motprov;
	protected MovingBox hitbox;
	protected Room room;

	public LeaverTester(StageType1 stage, Room nroom, IPoint npt) {
		room = nroom;
		scontext = stage.scontext;

		this.hitbox = new MovingBox(npt.getX(), npt.getY(), 20, 17, (e) -> nextvec.get(), null, null);
		this.mod = new HitBoxBasedModifier(this.hitbox, new IPoint.Point(0, 0), 0);

		EnumMap<PlayerVState, I2DRenderable> e = new EnumMap<>(PlayerVState.class);
		DrawLayer layer = DrawLayer.Room_Entities;
		e.put(PlayerVState.up_move, new LoopingAnimation(FolderVideos.player_redbox_move_up.get(), layer));
		e.put(PlayerVState.down_move, new LoopingAnimation(FolderVideos.player_redbox_move_down.get(), layer));
		e.put(PlayerVState.left_move, new LoopingAnimation(FolderVideos.player_redbox_move_left.get(), layer));
		e.put(PlayerVState.right_move, new LoopingAnimation(FolderVideos.player_redbox_move_right.get(), layer));
		e.put(PlayerVState.up_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_up.get(), layer));
		e.put(PlayerVState.down_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_down.get(), layer));
		e.put(PlayerVState.left_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_left.get(), layer));
		e.put(PlayerVState.right_stand, new LoopingAnimation(FolderVideos.player_redbox_stand_right.get(), layer));
		this.visual1 = new MapGraphicEntity<>(new Point(0, -23), PlayerVState.down_stand, e, layer);
		this.visual1.setModifier(mod);

		pack = new WalkBehaviorPack(roompos, hitbox, scontext, this, bboard);

		this.genBehaviours();
	}

	@Override
	public void forEachAttackables(Consumer<IAttackable> task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void forEachCollidables(Consumer<MovingBox> task) {
		task.accept(hitbox);
	}

	@Override
	public void forEachVisuals(Consumer<I2DRenderable> task) {
		task.accept(visual1);
	}

	@Override
	public void think(Consumer<BehaviorCore> task) {
		this.behaviours.run(0);
	}

	protected Field<Point> roompos = new Field<>(new Point(0, 0));

	protected Blackboard bboard = new Blackboard();
	protected BehaviorCore behaviours;

	StagePath roomgoal;

	private void genBehaviours() {
		behaviours = new BehaviorCore(new MoveToRoom(pack));
	}

	protected WalkBehaviorPack pack;

	protected Field<FloatVector> nextvec = new Field<>(new FloatVector(0, 0));

	@Override
	public void enter(Room room, Cardinal dir) {
		this.room = room;
		pack.room.set(scontext.getStageE().stagemap.getPos(room));
	}

	@Override
	public void leave(Room room) {
		this.room = null;
		pack.room.set(null);
	}

	@Override
	public void outsideThink() {
		behaviours.run(0);
	}

	@Override
	public void returnToRoomExistence(Room room) {
		this.room = room;
		pack.room.set(scontext.getStageE().stagemap.getPos(room));
	}
}
