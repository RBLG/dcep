package engine.game.defaultge.level.type1.visuals.bunker1;

import java.awt.image.BufferedImage;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomVisualGenerator.CanvasImage;
import engine.game.defaultge.level.type1.RoomVisualGenerator.PatternImage;
import engine.game.defaultge.level.type1.visuals.ILayer;
import engine.save.room.type1.RoomState;

import java.awt.Rectangle;
import res.visual.Images;

public class Ground implements ILayer {

	protected PatternImage bg = new PatternImage((BufferedImage) Images.stage_bunker1_ground.get());
	protected Rectangle tofill = new Rectangle(0, 0, Room.rosizex, Room.rosizey);

	@Override
	public void applyTo(CanvasImage canvas, RoomState state) {
		bg.setItPaint(0, 0, canvas.g);
		canvas.g.fill(tofill);
	}

}
