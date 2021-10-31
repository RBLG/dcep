package engine.game.defaultge.level.type1.entity;

import java.awt.Color;
import java.util.EnumMap;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.StageType1;
import engine.physic.basic2Dvectorial.FinalMotionVector;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.motionprovider.IMotionProvider;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.MapGraphicEntity;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.Rectangle;
import my.util.geometry.IPoint;
import my.util.geometry.IVector;

public class PathfindingTester extends BasicEntityV1<PathfindingTester.PTVisuals> {

	protected TesterBrain tbrain = new TesterBrain(this);
	protected Rectangle goal = new Rectangle(0, 0, 3, 3, Color.red);
	protected Rectangle nextonpath = new Rectangle(0, 0, 20, 20, new Color(255, 100, 0, 170));

	public PathfindingTester(StageType1 nstage) {
		EnumMap<PTVisuals, I2DRenderable> e = new EnumMap<>(PTVisuals.class);
		e.put(PTVisuals.zooming, new Rectangle(0, 0, 20, 20, Color.YELLOW));

		onCreate(nstage, //
				tbrain, //
				new MapGraphicEntity<>(new java.awt.Point(0, 0), PTVisuals.zooming, e), //
				new MovingBox(0, 0, 20, 20, new MotionVector(0, 0), tbrain));
		
		this.hitbox.setChaotic(false);
	}

	public void load(Room nroom) {
		super.load(nroom);
		IPoint pt = nroom.getPathfinder().getRandomAccessiblePoint(this.hitbox);
		this.hitbox.setX(pt.getX());
		this.hitbox.setY(pt.getY());
	}

	@Override
	public void loadVisual() {
		super.loadVisual();
		if (proom != null) {
			this.proom.getScene().addRenderable(nextonpath, DrawLayer.Room_Entities);
			this.proom.getScene().addRenderable(goal, DrawLayer.Room_Entities);
		}
	}

	public void setPos(int nx, int ny) {

	}

	
	public static class TesterBrain implements IMotionProvider, IBrain {
		PathfindingTester itself;
		protected Path path;

		public TesterBrain(PathfindingTester nitself) {
			itself = nitself;
		}

		@Override
		public FinalMotionVector getNextMove() {
			// Log.log(this, "path:" + path);
			if (path != null) {
				path.checkStuckness(itself.hitbox.getXY());
				path.MoveToNextStepIfDone(itself.getHitbox());

				IVector unlimited = path.getCurrentVector(itself.getHitbox());
				itself.nextonpath.getPos().getPos().move(itself.hitbox.getX(), itself.hitbox.getY());
				itself.nextonpath.getPos().getPos().translate(unlimited.getX(), unlimited.getY());
				IVector vec = path.getShortTermVector(itself.getHitbox(), 7);
				return new FinalMotionVector(vec.getX(), vec.getY());
			}
			return new FinalMotionVector(0, 0);
		}

		@Override
		public void think() {// TODO gerer le changement de salle

			// Log.log(this, "he thinkin");

			if (path == null || path.isDone(itself.hitbox)) {
				if (itself.proom != null) {
					PathFinder pf = itself.proom.getPathfinder();
					IPoint next = pf.getRandomPoint(itself.getHitbox());
					itself.goal.getPos().setPos(new java.awt.Point(next.getX(), next.getY()));
					path = pf.getPathFromTo(itself.hitbox.getCenter(), next, itself.hitbox);
				}
			}
		}

		@Override
		public void onMotionComputed(MovingBox movingBox) {
			//pas d'update a faire
		}

	}

	public static enum PTVisuals {
		zooming
	}

}
