package engine.game.defaultge.level.type1.entity;

import java.util.ArrayList;

import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.MovingBox;

public interface IEntityV2 {

	/***
	 * 
	 * @return la hitbox ou null si +
	 */
	public MovingBox getHitbox();

	/***
	 * 
	 * @return les hitboxs ou null si unique
	 */
	public ArrayList<MovingBox> getHitboxes();

	public void load(Room room); //TODO changer en roomcontext

	public void unload(Room room); //TODO pareil

	public void enterRoom(Room room, int nx, int ny);

	public void leaveRoom(Room room);

	public void think();

	public default void interact() {
		// TODO a definir plus
	}
}
