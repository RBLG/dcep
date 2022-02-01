package engine.game.defaultge.level.type1.visuals.bunker1;

import java.awt.Color;
import java.awt.Rectangle;

import engine.game.defaultge.level.type1.RoomVisualGenerator.CanvasImage;
import engine.game.defaultge.level.type1.visuals.ILayer;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.WallSlice;

public class WallShader implements ILayer {

	public static int shadowh = 35;
	// max=255
	static int alpha1 = 40; // marge minimale
	static int alpha2 = 20; // marge maximale
	static int alpha3 = 255 - alpha1 - alpha2;// variance

	@Override
	public void applyTo(CanvasImage canvas, RoomState state) {
		for (WallSlice slice : state.wallslices) {
			Rectangle sli = slice.getZone().toAwt();
			for (int it = 0; it < shadowh; it++) {
				canvas.g.setColor(new Color(0x2b, 0x2b, 0x2b, ((it * alpha3) / shadowh) + alpha1));
				canvas.g.fillRect(sli.x, sli.y - shadowh + it, sli.width, 1);
				canvas.g.finalize();
			}

		}
	}

}
