package engine.save.room.type1;

@Deprecated
public interface IPathfindable {
	IPathfindable[] getNexts();

	default IPathfindable getNext(int lindex) {
		return this.getNexts()[lindex];
	}

	int getDistance(int lindex1, int lindex2);

	int getIndex();

	void setIndex(int nindex);

	// 1. PathFindable[] (liste des chemins et portes)
	// 2. int[] (connexions inter pf) (en fait non)
	//
	// X. int[1.len] (valeurs du sonar)

}
