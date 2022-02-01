package engine.game.defaultge.level.type1.visuals.bunker1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.game.defaultge.level.type1.visuals.IVisualCollection;
import engine.game.defaultge.level.type1.visuals.ILayer;
import engine.game.defaultge.level.type1.visuals.Visual;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.StillImage;
import engine.save.room.type1.RoomState;
import engine.game.defaultge.level.type1.visuals.bunker1.WallTopLine.WallTopLineShader;

public class Bunker1Visual implements IVisualCollection {

	protected static Visual bgvisual = new Visual(
			list(new Ground(), new Walls(), new Lamps(), new Pipes(), new WallTopLine()));
	protected static Visual shaders = new Visual(list(new WallShader(), new WallTopLineShader()));

	@Override
	public ArrayList<I2DRenderable> getVisuals(RoomState state) {
		ArrayList<I2DRenderable> rtn = new ArrayList<>();
		rtn.add(new StillImage(bgvisual.draw(state, true).img, new java.awt.Point(0, 0)));
		rtn.add(new StillImage(shaders.draw(state, true).img, new java.awt.Point(0, 0)));
		return rtn;
	}

	public static List<ILayer> list(ILayer... layers) {
		return Arrays.asList(layers);
	}
}
