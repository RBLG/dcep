package engine.game.defaultge.level.type1;

import java.util.ArrayList;

import my.util.Cardinal;
import my.util.Log;

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
		Log.log("chemin fini mais toujours utilisé en" + index + " sur " + path.size());
		return null;
	}

	public boolean isDone() {
		return path.size() - 1 <= index;
	}
}