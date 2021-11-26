package engine.game.defaultge.level.type1;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.EnumMap;

import engine.entityfw.EntityWackSystem;
import engine.entityfw.components.IHasInteractables;
import engine.entityfw.subsystems.EntitySubscriber;
import engine.entityfw.subsystems.VisualESS;
import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.game.defaultge.level.type1.RoomVisualGenerator.RoomVisual;
import engine.game.defaultge.level.type1.entity.PlayerEntityV3;
import engine.game.defaultge.level.type1.entity.WandererTest;
import engine.game.defaultge.level.type1.interactions.RoomInteractableHaver;
import engine.game.defaultge.level.type1.entity.IRoomTraverserEntity;
import engine.physic.basic2DInteractionV3.InteractionESS;
import engine.physic.basic2Dvectorial.CollisionESS;
import engine.physic.basic2Dvectorial.ISegment;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.Basic2DSub;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.Scene;
import engine.render.engine2d.renderable.I2DRenderer;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import engine.save.room.type1.RoomState.Door;
import my.util.Cardinal;
import my.util.geometry.IRectangle;

public class Room {
	//////////////////////////////////////////////////////////////
	public final static int rosizey = Basic2DSub.LDymax;
	public final static int rosizex = Basic2DSub.LDxmax;
	//////////////////////////////////////////////////////////////
	public static final int simscale = 1000; //emplacement temporaire
	
	protected Scene scene;
	public RoomState state;
	public EnumMap<Side, Door> doors;
	public EnumMap<Cardinal, ArrayList<ISegment>> walls;
	protected ArrayList<RoomVisual> visuals = new ArrayList<>();
	protected EntityWackSystem ews;
	protected VisualESS visualess = new VisualESS();
	protected RoomInteractableHaver interactables = new RoomInteractableHaver(this);

	// TODO a rework
	protected PathFinder pathfinder;

	/**
	 * doors[] -> n,s,e,w
	 * 
	 * @param offsetx
	 * @param offsety
	 * @param doors
	 */
	public Room(StageType1 stage, int offsetx, int offsety, ArrayList<RoomState> pool, DoorType[] ndoors) {
		this.ews = new EntityWackSystem();
		this.scene = new Scene(offsetx, offsety);

		// TODO rework pour que les generators ne modifie pas directement Room, c'est
		// pas lisible
		RoomGenerator.genRoom(this, pool, ndoors);
		pathfinder = new PathFinder(state.navmesh);
		RoomVisualGenerator.genVisual(this);

		this.ews.add(new CollisionESS(walls));
		this.ews.add(visualess);
		this.scene.add(this.visualess, DrawLayer.Room_Entities);
		EntitySubscriber<IHasInteractables> interactables = new EntitySubscriber<>(IHasInteractables.class);
		this.ews.add(interactables);
		this.ews.add(new InteractionESS(interactables));

		// a la fin de l'instantiation des ess (au cas ou j'ai envie d'en ajouter plus)
		this.ews.add(this.interactables);
		this.ews.add(
				new WandererTest(stage, this, this.pathfinder.getRandomPoint(new IRectangle.Rectangle(0, 0, 20, 17))));

		for (RoomVisual vis : visuals) {
			this.scene.add(vis.rd, vis.lay);
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
		// ent.setScene(this.scene);
		// ent.loadVisual();
		this.enter(dir, ent);

		// this.ews.onStart();
		// this.inter.add(InteractionLayer.action, ent.getActionBox());
		this.scene.setVisible(true);

	}

	public void enter(Cardinal dir, IRoomTraverserEntity ent) {
		ent.enter(this, dir);
		this.ews.add(ent);

	}

	public void playerLeave(PlayerEntityV3 ent) {
		this.leave(ent);
		// this.scene.setVisible(false);
	}

	public void leave(IRoomTraverserEntity ent) {
		ent.leave(this);
		this.ews.remove(ent);
	}

	public void update(long time) {
		this.ews.run(time);
	}

	public Scene getScene() {
		return this.scene;
	}

	public PathFinder getPathfinder() {
		return pathfinder;
	}

	public void render(I2DRenderer r, Graphics g, long time, double scx, double scy) {
		// TODO Auto-generated method stub

	}

}
