package engine.game.defaultge.level.type1;

import java.util.ArrayList;
import java.util.EnumMap;

import engine.entityfw.EntityWackSystem;
import engine.entityfw.components.IHasCollidable;
import engine.entityfw.components.IHasInteractables;
import engine.entityfw.subsystems.EntitySubSystem;
import engine.entityfw.subsystems.EntitySubscriber;
import engine.entityfw.subsystems.VisualESS;
import engine.entityfwp2.ai.BehavioursESS;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.game.defaultge.level.type1.entity.WandererTest;
import engine.game.defaultge.level.type1.interactions.IRoomTraverserEntity;
import engine.game.defaultge.level.type1.interactions.RoomInteractableHaver;
import engine.physic.basic2DInteractionV3.InteractionESS;
import engine.physic.basic2Dattacks.AttackESS;
import engine.physic.basic2Dattacks.IHasAttackables;
import engine.physic.basic2Dvectorial.CollisionESS;
import engine.physic.basic2Dvectorial.ISegment;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.Basic2DSub;
import engine.render.engine2d.RenderableList;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.temp.ITreeNodeRenderable;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import engine.save.room.type1.RoomState.Door;
import my.util.Cardinal;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;

public class Room implements ITreeNodeRenderable {
	//////////////////////////////////////////////////////////////
	public final static int rosizey = Basic2DSub.LDymax;
	public final static int rosizex = Basic2DSub.LDxmax;
	//////////////////////////////////////////////////////////////

	protected RenderableList scene;
	public RoomState state;
	public EnumMap<Side, Door> doors;
	public EnumMap<Cardinal, ArrayList<ISegment>> walls;
	protected ArrayList<I2DRenderable> visuals = new ArrayList<>();
	protected EntityWackSystem ews;
	protected VisualESS visualess = new VisualESS();
	protected RoomInteractableHaver interactables = new RoomInteractableHaver(this);

	protected PathFinder pathfinder;
	public int index; // index pour le sonar

	/**
	 * doors[] -> n,s,e,w
	 * 
	 * @param offsetx
	 * @param offsety
	 * @param doors
	 */
	public Room(StageType1 stage, int offsetx, int offsety, ArrayList<RoomState> pool, DoorType[] ndoors) {
		this.ews = new EntityWackSystem();
		this.scene = new RenderableList(offsetx, offsety);

		// TODO rework pour que les generators ne modifie pas directement Room, c'est
		// pas lisible
		RoomGenerator.genRoom(this, pool, ndoors);
		pathfinder = new PathFinder(state.navmesh, state.navmjunctions);
		RoomVisualGenerator.genVisual(this);

		this.ews.add(new EntitySubSystem<IHasCollidable>(IHasCollidable.class) {
			@Override
			public void update() {
				this.components.forEach((comp) -> comp.forEachCollidables((col) -> col.applyMotion()));
			}
		});

		this.ews.add(new BehavioursESS());
		this.ews.add(new CollisionESS(walls));
		this.ews.add(visualess);
		this.scene.add(this.visualess);
		EntitySubscriber<IHasInteractables> interactables = new EntitySubscriber<>(IHasInteractables.class);
		this.ews.add(interactables);
		this.ews.add(new InteractionESS(interactables));
		EntitySubscriber<IHasAttackables> attackables = new EntitySubscriber<>(IHasAttackables.class);
		this.ews.add(attackables);
		this.ews.add(new AttackESS(attackables));

		// a la fin de l'instantiation des ess (au cas ou j'ai envie d'en ajouter plus)
		this.ews.add(this.interactables);
		this.ews
			.add(new WandererTest(stage, this, this.pathfinder.getRandomPoint(new IRectangle.Rectangle(0, 0, 20, 17))));

		scene.setVisible(false);
		// TODO clean les artifacts de RoomVisuals
		for (I2DRenderable vis : visuals) {
			this.scene.add(vis);
		}
//		for (ArrayList<ISegment> segs : state.walls.values()) {
//			for (ISegment seg : segs) {
//				engine.render.engine2d.renderable.Rectangle rec = new engine.render.engine2d.renderable.Rectangle(
//						seg.getX(), seg.getY(), seg.getX2() - seg.getX() + 1, seg.getY2() - seg.getY() + 1, Color.red);
//				scene.addRenderable(rec, DrawLayer.Room_Walls);
//			}
//		}
	}

	/***
	 * load la salle
	 * 
	 * @param dir
	 */
	public void playerEnter(Cardinal dir, PlayerEntityV3 ent) {
		this.enter(dir, ent);
		this.scene.setVisible(true);

	}

	public void enter(Cardinal dir, IRoomTraverserEntity ent) {
		ent.enter(this, dir);
		this.ews.add(ent);

	}

	public void playerLeave(PlayerEntityV3 ent) {
		this.leave(ent);
		this.scene.setVisible(false);
	}

	public void leave(IRoomTraverserEntity ent) {
		ent.leave(this);
		this.ews.remove(ent);
	}

	public void update(long time) {
		this.ews.run(time);
	}

	public PathFinder getPathfinder() {
		return pathfinder;
	}

	public Path getPathToDoor(IRectangle rec, Side side) {
		Point dpt = new Point(doors.get(side).getZone().getCenter());
		dpt.x = Integer.max(0, dpt.x - rec.getWidth());
		dpt.y = Integer.max(0, dpt.y - rec.getHeight());
		return pathfinder.getPathFromTo(rec.getXY(), doors.get(side).getZone().getCenter(), rec);
	}

	public EntityWackSystem getEWS() {
		return this.ews;
	}

	@Override
	public void prepare(IWaitlist wt, int res, long time, double px, double py, double vx, double vy) {
		this.scene.prepare(wt, res, time, px, py, vx, vy);
	}

}
