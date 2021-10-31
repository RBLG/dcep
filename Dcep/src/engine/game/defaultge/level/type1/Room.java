package engine.game.defaultge.level.type1;

import java.util.ArrayList;
import java.util.EnumMap;

import engine.game.defaultge.level.type1.RoomPool.DoorType;
import engine.game.defaultge.level.type1.RoomVisualGenerator.RoomVisual;
import engine.game.defaultge.level.type1.entity.EntityV1;
import engine.game.defaultge.level.type1.entity.PlayerEntity;
import engine.physic.basic2DInteraction.InteractionEngine;
import engine.physic.basic2DInteraction.InteractionEngine.InteractionLayer;
import engine.physic.basic2DInteraction.InteractionObserver;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.physic.basic2DvectorialV2.BasicPhysicEngineV2;
import engine.render.engine2d.Basic2DSub;
import engine.render.engine2d.Scene;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import engine.save.room.type1.RoomState.Door;
import my.util.CardinalDirection;
import my.util.geometry.IPoint.Point;

public class Room {
	//////////////////////////////////////////////////////////////
	public final static int rosizey = Basic2DSub.LDymax;
	public final static int rosizex = Basic2DSub.LDxmax;
	//////////////////////////////////////////////////////////////
	protected BasicPhysicEngineV2 physic;
	protected InteractionEngine inter;
	protected Scene scene;
	protected EnumMap<Side, Door> doors;
	protected ArrayList<RoomVisual> visuals = new ArrayList<>();
	protected RoomState state;
	protected PathFinder pathfinder;
	protected ArrayList<EntityV1> ents = new ArrayList<>();

	/**
	 * doors[] -> n,s,e,w
	 * 
	 * @param offsetx
	 * @param offsety
	 * @param doors
	 */
	public Room(int offsetx, int offsety, ArrayList<RoomState> pool, DoorType[] ndoors) {
		this.physic = new BasicPhysicEngineV2();
		this.inter = new InteractionEngine();
		this.scene = new Scene(offsetx, offsety);

		RoomGenerator.genRoom(this, pool, ndoors);
		RoomVisualGenerator.genVisual(this);

		pathfinder = new PathFinder(state.navmesh);

		for (RoomVisual vis : visuals) {
			this.scene.addRenderable(vis.rd, vis.lay);
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
	public void playerEnter(CardinalDirection dir, PlayerEntity ent) {
		ent.setScene(this.scene);
		ent.loadVisual();
		this.enter(dir, ent);

		this.physic.onStart();
		this.inter.add(InteractionLayer.action, ent.getActionBox());
		this.scene.setVisible(true);

	}

	public void enter(CardinalDirection dir, EntityV1 ent) {
		ents.add(ent);
		ent.load(this);
		this.physic.getEnts().add(ent.getHitbox()); // TODO changer (???)

		Point np = this.state.getDoorFront(dir, ent.getHitbox().getWidth(), ent.getHitbox().getHeight());
		ent.setPos(np.x, np.y);
	}

	public void playerLeave(PlayerEntity ent) {
		this.leave(ent);
		// this.scene.setVisible(false);
	}

	public void leave(EntityV1 ent) {
		this.ents.remove(ent);
		this.physic.getEnts().remove(ent.getHitbox());
	}

	public void go(long time) {
		for (EntityV1 ent : ents) {
			ent.think();
		}
		this.physic.go(time);
		this.inter.go();
	}

	public Scene getScene() {
		return this.scene;
	}

	public void doorObsTrigger(InteractionObserver e) {

	}

	public PathFinder getPathfinder() {
		return pathfinder;
	}

}
