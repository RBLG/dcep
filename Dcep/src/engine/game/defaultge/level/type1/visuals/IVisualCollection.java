package engine.game.defaultge.level.type1.visuals;

import java.util.ArrayList;

import engine.render.engine2d.renderable.I2DRenderable;
import engine.save.room.type1.RoomState;

public interface IVisualCollection {
	
	ArrayList<I2DRenderable> getVisuals(RoomState state);
}
