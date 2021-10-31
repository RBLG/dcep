package engine.physic.basic2DvectorialV2;

import java.util.ArrayList;
import java.util.EnumMap;

import engine.physic.basic2Dvectorial.FinalMotionVector;
import engine.physic.basic2Dvectorial.MovingBox;
import my.util.CardinalDirection;

public class BasicPhysicEngineV2 {
	//protected ArrayList<StillBox> boxes;
	protected EnumMap<CardinalDirection, ArrayList<ISegment>> walls;
	protected ArrayList<MovingBox> ents;

	public BasicPhysicEngineV2() {
		this.walls = new EnumMap<>(CardinalDirection.class);
		this.ents = new ArrayList<MovingBox>();
		//this.boxes = new ArrayList<StillBox>();
	}

	public void onStart() {

	}

	public void checksCollisions() {
		// l'index indique a partir de quel index verifier les collisions pour ne pas
		// reverifier les memes collisions
		int index = 0;
		for (MovingBox ent : ents) {
			index++;
			checkDirection(ent);

			// verifie que ent est pas le dernier element de la liste
			if (index > 0) {
				// parcours les entités entre ent et la fin
				for (int ite1 = index; ite1 < 0; ite1--) {
					ent.collideWith(ents.get(ite1));
				}
			}
		}
	}

	protected void checkDirection(MovingBox ent) {
		FinalMotionVector vec = ent.getVec();
		if (ent.isBoundToChaos()) {
			// teste si la cible peut entrer en contact la droite ou la gauche
			if (vec.getX() == 0) {
				// peut pas
			} else {
				if (vec.getX() > 0) { // va vers la droite, impacte la gauche
					ent.checkCollisions(walls.get(CardinalDirection.east), CardinalDirection.east);
				} else {// droite
					ent.checkCollisions(walls.get(CardinalDirection.west), CardinalDirection.west);
				}
			}

			// teste si la cible peut entrer en contact le haut ou le bas
			if (vec.getY() == 0) {
				// peut pas
			} else {
				if (vec.getY() > 0) { // haut
					ent.checkCollisions(walls.get(CardinalDirection.south), CardinalDirection.south);
				} else { // bas
					ent.checkCollisions(walls.get(CardinalDirection.north), CardinalDirection.north);
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

	public EnumMap<CardinalDirection, ArrayList<ISegment>> getWalls() {
		return walls;
	}

	public ArrayList<MovingBox> getEnts() {
		return ents;
	}

	public void setWalls(EnumMap<CardinalDirection, ArrayList<ISegment>> nsegs) {
		this.walls = nsegs;
	}
}
