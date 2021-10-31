package engine.save.room.type1;

import java.io.Serializable;
import java.util.ArrayList;

@Deprecated
public class Doorway implements IPathfindable, Serializable {
	private static final long serialVersionUID = -1145584520733770992L;

	protected Doorway.VariationList rightvar;
	protected Doorway.VariationList leftvar;
	protected ArrayList<Doorway.ChokePoint> chokes;
	protected ArrayList<Doorway.Node> nodes;
	protected int x, y, x2, y2;
	protected int index;

	public Doorway(Doorway.VariationList vright, Doorway.VariationList vleft, ArrayList<Doorway.ChokePoint> chokes) {
		this.rightvar = vright;
		this.leftvar = vleft;
		// TODO finir
	}

	public static class Node implements Serializable {
		private static final long serialVersionUID = -1479673749247851340L;

		public int pos;
		public int x, y, x2, y2;
		public Doorway way; // pathfindable?

		// TODO finir
	}

	public static class ChokePoint implements Serializable {
		private static final long serialVersionUID = -667314798640266990L;

		int pos;
		int size;

		// TODO finir
	}

	public static class VariationList implements Serializable {
		private static final long serialVersionUID = 7574725134248567995L;

		int[][] vars; // 0: it, 1:width, 2:margin(?)

		public VariationList(int[][] raw) {
			this.vars = new int[raw.length][0]; // extraire la longueur
			// TODO finir
		}

		public int[] getNext(int it, boolean isincrementing) {
			int index = isincrementing ? 0 : vars.length - 1;
			int step = isincrementing ? 1 : -1;
			boolean done = false;
			while (!done) {
				int[] va = vars[index];
				if ((va[0] - it) * step > 0) {
					// retourne: last width, new width, it
					return new int[] { vars[index - 1][1], va[1], it };
				}
				// TODO empecher les sortie d'array
				index += step;
			} // TODO si y a pas
			return null;
		}

	}

	@Override
	public IPathfindable[] getNexts() {
		ArrayList<IPathfindable> nexts = new ArrayList<IPathfindable>();
		// ajouter les cas en +
		for (Node node : nodes) {
			nexts.add(node.way);
		}
		return nexts.toArray(new IPathfindable[nexts.size()]);
	}

	@Override
	public IPathfindable getNext(int lindex) {
		return this.nodes.get(lindex).way;

	}

	@Override
	public int getDistance(int lindex1, int lindex2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public void setIndex(int nindex) {
		this.index = nindex;
	}
}