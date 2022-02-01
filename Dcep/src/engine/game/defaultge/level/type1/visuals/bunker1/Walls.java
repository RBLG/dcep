package engine.game.defaultge.level.type1.visuals.bunker1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import engine.game.defaultge.level.type1.Room;
import engine.game.defaultge.level.type1.RoomVisualGenerator.CanvasImage;
import engine.game.defaultge.level.type1.RoomVisualGenerator.PatternImage;
import engine.game.defaultge.level.type1.visuals.ILayer;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.WallSlice;
import res.visual.Images;

public class Walls implements ILayer {

	protected static PatternImage pattern = new PatternImage((BufferedImage) Images.stage_bunker1_wall1.get());
	public static int wallh = pattern.img.getHeight();

	@Override
	public void applyTo(CanvasImage canvas, RoomState state) {
		for (WallSlice slice : state.wallslices) {
			Rectangle sli = slice.getZone().toAwt();
			// fond noir ///////////////
			canvas.g.setColor(new Color(0x2b2b2b));
			canvas.g.fill(sli);
			////////////////////////////

			// si mur du bas
			if ((slice.bottom.getY() >= Room.rosizey)) {
				continue;
			}
			// si le mur est assez grand
			if (sli.height > 41) {
				Graphics2D gra2 = (Graphics2D) canvas.g.create(sli.x, sli.y, sli.width, sli.height);
				Rectangle wa = new Rectangle(//
						slice.bottom.getX() - sli.x, //
						slice.bottom.getY() - wallh - sli.y, //
						slice.bottom.getX2() + 1, //
						wallh + 1//
				);
				pattern.setItPaint(wa.x, wa.y, gra2);
				gra2.fill(wa);
				gra2.finalize();
			}
		}
	}

}
