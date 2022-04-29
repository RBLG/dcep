package engine.game.defaultge.level.type1;

import java.util.ArrayList;

import my.util.Cardinal;

public class StagePath {

	protected ArrayList<Cardinal> path;
	protected int index = -1;

	public StagePath(ArrayList<Cardinal> npath) {
		path = npath;
	}

	public Cardinal next() {
		index++;
		if (index < path.size()) {
			return path.get(index);
		}
		return null;
	}

	public boolean isDone() {
		return index >= path.size();
	}

}
