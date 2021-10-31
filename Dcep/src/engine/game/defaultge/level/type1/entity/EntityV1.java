package engine.game.defaultge.level.type1.entity;

import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.MotionVector;
import engine.physic.basic2Dvectorial.MovingBox;

public abstract class EntityV1 {

	protected MovingBox hitbox;

	public MotionVector getVector() {
		return this.hitbox.getVec();
	}

	public MovingBox getHitbox() { // TODO hitbox multiboite?
		return this.hitbox;
	}

	public abstract void loadVisual();
	
	public abstract void load(Room room);

	/***
	 * set l'entité en entrée de salle
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void setPos(int x, int y);

	public abstract void think();

}
