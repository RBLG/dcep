package engine.game.defaultge.level.type1;

import java.util.ArrayList;
import java.util.EnumMap;

import engine.entityfw.EntityWackSystem;
import engine.entityfw.components.IHasInteractables;
import engine.entityfw.subsystems.EntitySubscriber;
import engine.entityfw.subsystems.IEntitySubworker;
import engine.entityfw.subsystems.VisualESS;
import engine.entityfwp2.ai.BehavioursESS;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.game.defaultge.level.type1.entity.WandererTest;
import engine.game.defaultge.level.type1.interactions.RoomInteractableHaver;
import engine.game.defaultge.level.type1.entity.IRoomTraverserEntity;
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
import my.util.geometry.IPoint;
import my.util.geometry.IRectangle;

public class Room implements ITreeNodeRenderable {
	//////////////////////////////////////////////////////////////
	public final static int rosizey = Basic2DSub.LDymax;
	public final static int rosizex = Basic2DSub.LDxmax;
	//////////////////////////////////////////////////////////////

	protected RenderableList scene;
	public RoomState state;
	public EnumMap<Side, Door> doors;
	// TODO rework pour que ça soit plus clean
	public DoorType[] doors2;
	public EnumMap<Cardinal, ArrayList<ISegment>> walls;
	protected ArrayList<I2DRenderable> visuals = new ArrayList<>();
	protected EntityWackSystem<Modes> ews;
	protected VisualESS visualess = new VisualESS();
	protected RoomInteractableHaver interactables = new RoomInteractableHaver(this);
	protected EntitySubscriber<IBgModeNeeder> bgmsubscriber = new EntitySubscriber<>(IBgModeNeeder.class);
	public IPoint pos;

	protected PathFinder pathfinder;
	public int index; // index pour le sonar

	public static enum Modes {
		active, background, visual;
	}

	/**
	 * doors[] -> n,s,e,w
	 * 
	 * @param offsetx
	 * @param offsety
	 * @param point
	 * @param doors
	 */
	public Room(StageType1 stage, int offsetx, int offsety, ArrayList<RoomState> pool, DoorType[] ndoors, IPoint npos) {
		scene = new RenderableList(offsetx, offsety);
		pos = npos;

		doors2 = ndoors;
		// TODO rework pour que les generators ne modifie pas directement Room, c'est
		// pas lisible
		RoomGenerator.genRoom(this, pool, ndoors);
		pathfinder = new PathFinder(state.navmesh, state.navmjunctions);
		RoomVisualGenerator.genVisual(this);

		
		// initialisation du mode principal
		ews = new EntityWackSystem<>(Modes.active);
		BehavioursESS behav = new BehavioursESS();
		ews.add(behav);
		CollisionESS coli = new CollisionESS(walls);
		ews.add(coli);
		scene.add(visualess);
		ews.add(visualess);
		EntitySubscriber<IHasInteractables> interactables = new EntitySubscriber<>(IHasInteractables.class);
		ews.add(interactables);
		ews.add(new InteractionESS(interactables));
		EntitySubscriber<IHasAttackables> attackables = new EntitySubscriber<>(IHasAttackables.class);
		ews.add(attackables);
		ews.add(new AttackESS(attackables));

		// initiation du mode background
		ArrayList<IEntitySubworker> alt = new ArrayList<>();
		alt.add(behav);
		alt.add(coli);
		ews.addAltMode(Modes.background, alt);

		// initilisation du mode update visuel
		alt = new ArrayList<>();
		alt.add(visualess);
		ews.addAltMode(Modes.visual, alt);

		// le trick pour savoir si besoin de run room en mode background
		ews.add(this.bgmsubscriber.asSubsc());

		/// !entités a partir d'ici!
		ews.add(this.interactables);// techniquement c'est une entité
		ews.add(new WandererTest(stage, this, this.pathfinder.getRandomPoint(new IRectangle.Rectangle(0, 0, 20, 17))));

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
		enter(dir, ent);
		scene.setVisible(true);

	}

	public void enter(Cardinal dir, IRoomTraverserEntity ent) {
		ent.enter(this, dir);
		ews.add(ent);

	}

	public void playerLeave(PlayerEntityV3 ent) {
		this.leave(ent);
		this.scene.setVisible(false);
	}

	public void leave(IRoomTraverserEntity ent) {
		ent.leave(this);
		ews.remove(ent);
	}

	public void update(long time) {
		ews.run(time);
	}

	public PathFinder getPathfinder() {
		return pathfinder;
	}

	public Path getPathToDoor(IRectangle rec, Side side) {
		// Point dpt = new Point(doors.get(side).getFront(rec.getWH()));
		// dpt.x = Integer.max(0, dpt.x - rec.getWidth());
		// dpt.y = Integer.max(0, dpt.y - rec.getHeight());
		return pathfinder.getPathFromTo(rec.getXY(), doors.get(side).getFront(rec.getWH()), rec);
	}

	public EntityWackSystem<?> getEWS() {
		return ews;
	}

	@Override
	public void prepare(IWaitlist wt, int res, long time, double px, double py, double vx, double vy) {
		this.scene.prepare(wt, res, time, px, py, vx, vy);
	}

	public boolean doesContainBgNeeders() {
		return this.bgmsubscriber.getComponents().size() != 0;
	}

	public void bgUpdateIfNeeded(long time) {
		if (doesContainBgNeeders()) {
			// ews.run(time);
			ews.run(time, Modes.background);
		}
	}

	public void visualupdate(long time) {
		ews.run(time, Modes.visual);
	}

}
