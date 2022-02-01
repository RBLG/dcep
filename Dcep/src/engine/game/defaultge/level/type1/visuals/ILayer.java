package engine.game.defaultge.level.type1.visuals;

import engine.game.defaultge.level.type1.RoomVisualGenerator.CanvasImage;
import engine.save.room.type1.RoomState;

public interface ILayer {

	public void applyTo(CanvasImage canvas, RoomState state);
}
