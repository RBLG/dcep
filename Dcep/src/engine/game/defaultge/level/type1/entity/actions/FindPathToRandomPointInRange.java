package engine.game.defaultge.level.type1.entity.actions;

import debug.PathVisualiser;
import engine.entityfwp2.ai.Blackboard;
import engine.entityfwp2.ai.action.ParamAction.ParamSupplier;
import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.MovingBox;
import engine.physic.basic2Dvectorial.pathfinding.Path;
import engine.physic.basic2Dvectorial.pathfinding.PathFinder;

public class FindPathToRandomPointInRange implements ParamSupplier<Path> {

	protected PathFinder pf;
	protected MovingBox box;
	protected int range;
	protected Blackboard board;

	public FindPathToRandomPointInRange(Blackboard nboard, Room nroom, MovingBox nbox, int nrange) {
		pf = nroom.getPathfinder();
		box = nbox;
		range = nrange;
		board = nboard;
	}

	@Override
	public Path get() {
		Path npath = pf.getPathToRandomPointInWalkRange(box.toOutInt(), range);
		if (npath != null) {
			board.computeIfThere("pathvisualiser", PathVisualiser.class, (pv) -> {
				pv.updatePath(npath);
			});
		}
		return npath;
	}
}
