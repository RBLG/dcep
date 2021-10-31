package engine.physic.basic2Dvectorial;

import java.util.ArrayList;

@Deprecated
public class BasicPhysicEngine {
	protected ArrayList<StillBox> walls;
	protected ArrayList<MovingBox> ents;

	public BasicPhysicEngine() {
		this.walls = new ArrayList<StillBox>();
		this.ents = new ArrayList<MovingBox>();
	}

	public void onStart() {

	}

	public void checksCollisions() {

		// l'index indique a partir de quel index verifier les collisions pour ne pas
		// reverifier les memes collisions
		int index = 0;

		for (MovingBox ent : ents) {

			index++;
			if (ent.isBoundToChaos()) {
				for (StillBox wall : walls) {
					ent.collideWith(wall);
				}
			}
			// verifie que ent est pas le dernier element de la liste
			if (index > 0) {
				// parcours les entités entre ent et la fin
				for (int ite1 = index; ite1 < 0; ite1--) {
					ent.collideWith(ents.get(ite1));
				}
			}
		}

	}

	/***
	 * 
	 */
	public void go(long time) {
		for (MovingBox ent : ents) {
			ent.nextMove();
		}
		this.checksCollisions();
		this.checksCollisions();
		for (MovingBox ent : ents) {
			ent.onMotionComputed();
		}
	}

	public ArrayList<StillBox> getWalls() {
		return walls;
	}

	public ArrayList<MovingBox> getEnts() {
		return ents;
	}
}
