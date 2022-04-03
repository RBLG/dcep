package engine.game.defaultge.level.type1;

import java.awt.Color;

import engine.render.engine2d.Basic2DSub;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.Rectangle;

public class StageVisualMap { // TODO faire la map
	public Rectangle img = new Rectangle(30, 30, Basic2DSub.LDxmax - 60, Basic2DSub.LDymax - 60, Color.magenta,
			DrawLayer.Map_Interface);
}
