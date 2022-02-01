package engine.game.defaultge.level.type1.visuals;

import java.awt.AlphaComposite;
import java.util.List;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomVisualGenerator.CanvasImage;
import engine.save.room.type1.RoomState;

public class Visual {

	protected List<ILayer> layers;

	public Visual(List<ILayer> nlayers) {
		layers = nlayers;
	}

	public CanvasImage draw(RoomState state, boolean opaque) {
		CanvasImage canvas = new CanvasImage(Room.rosizex, Room.rosizey);
		if (!opaque) {
			canvas.g.setComposite(AlphaComposite.SrcOver.derive(1f));
		}
		drawOn(canvas, state);
		return canvas;
	}

	public void drawOn(CanvasImage canvas, RoomState state) {
		for (ILayer layer : layers) {
			layer.applyTo(canvas, state);
		}
	}
}
