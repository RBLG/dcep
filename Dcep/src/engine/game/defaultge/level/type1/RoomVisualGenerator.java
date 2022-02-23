package engine.game.defaultge.level.type1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.game.defaultge.level.type1.visuals.bunker1.Bunker1Visual;
import engine.physic.basic2Dvectorial.pathfinding.ResizedNavigationMesh;
import engine.physic.basic2Dvectorial.pathfinding.format.Junction;
import engine.physic.basic2Dvectorial.pathfinding.format.Tile;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.BasicText;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.StillImage;
import engine.render.engine2d.renderable.dev.DevHollowRectangle;
import engine.render.engine2d.renderable.dev.DevSegment;
import my.util.ImageCache;
import my.util.Log;
import my.util.geometry.IPoint.Point;
import my.util.geometry.IRectangle;

public final class RoomVisualGenerator extends Room {
	private RoomVisualGenerator() {
		super(null, 0, 0, null, null);
	}

	public static int coloor = 0;

	public static void genVisual(Room room) {

		Bunker1Visual vis = new Bunker1Visual();
		ArrayList<I2DRenderable> list = vis.getVisuals(room.state);
		room.visuals.add(list.get(0));
		room.visuals.add(list.get(1));

		// TODO trud de test, a enlever
		/////////////////////////////////////////////////////////
		CanvasImage navtestcv = new CanvasImage(Room.rosizex, Room.rosizey);
		navtestcv.g.setComposite(AlphaComposite.SrcOver.derive(0.3f));
		int color = 0x8000F000;
		if (Boolean.FALSE) {
			for (Tile tile : room.state.navmesh) {
				color += 666;
				navtestcv.g.setColor(new Color(color));
				Rectangle tilre = new Rectangle(tile.x, tile.y, tile.x2 - tile.x + 1, tile.y2 - tile.y + 1);
				navtestcv.g.fill(tilre);
				navtestcv.g.setColor(new Color(0x00FFFFFF));
				// navtestcv.g.draw(tilre);
				room.visuals.add(new DevHollowRectangle(tilre, Color.white));
			}
			room.visuals.add(new StillImage(navtestcv.img, 0, 0, DrawLayer.Room_Shaders));
		}

		/////////////////////////////////////////////////////////
		ResizedNavigationMesh test = room.pathfinder.getCache()
				.getFittingNavMesh(new IRectangle.Rectangle(0, 0, 21, 18));
		navtestcv = new CanvasImage(Room.rosizex, Room.rosizey);
		navtestcv.g.setComposite(AlphaComposite.SrcOver.derive(0.3f));
		color = 0x8000F000;
		if (Boolean.FALSE) {
			for (Tile tile : test.getTiles()) {
				if (tile.x2 - tile.x < 0 || tile.y2 - tile.y < 0) {
					Log.log("roomvisgen", "tile negative waaa");
				}
				// Log.log("rvisgen", "nbs:" + tile.neighbors.size());

				color += 666;
				navtestcv.g.setColor(new Color(color));
				Rectangle tilre = new Rectangle(tile.x, tile.y, tile.x2 - tile.x + 1, tile.y2 - tile.y + 1);
				navtestcv.g.fill(tilre);
				// navtestcv.g.setColor(new Color(0x00FFFFFF));
				// navtestcv.g.draw(tilre);
				room.visuals.add(new DevHollowRectangle(tilre, Color.yellow));
			}
		}
		if (Boolean.FALSE) {
			for (Tile tile : test.getTiles()) {
				for (Junction jc : tile.junctions) {
					room.visuals.add(new DevSegment(tile.getCenter(), jc.getCenter(), new Color(255, 0, 255)));
				}
			}
		}

		if (Boolean.FALSE) {
			room.visuals.add(new StillImage(navtestcv.img, 0, 0, DrawLayer.Room_Shaders));
		}
		if (Boolean.TRUE) {
			for (Junction jc : test.getJunctions()) {
				Rectangle jcrec = new Rectangle(jc.x - 1, jc.y - 1, jc.x2 - jc.x + 1 + 1, jc.y2 - jc.y + 1 + 1);
				room.visuals.add(new DevHollowRectangle(jcrec, Color.orange));
				room.visuals
						.add(new BasicText(new Point(jcrec.x, jcrec.y), "" + jc.index, Color.black, DrawLayer.debug));
				if (Boolean.FALSE) {
					for (Junction li : jc.linkeds) {
						room.visuals.add(new DevSegment(jc.getCenter(), li.getCenter(), Color.red));
					}
				}
			}
		}

	}

	public static class PatternImage {
		public BufferedImage img;

		public PatternImage(BufferedImage nimg) {
			img = nimg;
		}

		public PatternImage(String path) {
			this(ImageCache.toBufferedImage(ImageCache.getImage(path)));
		}

		public void setItPaint(int x, int y, Graphics2D g) {
			Rectangle texsize = new Rectangle(x, y, img.getWidth(), img.getHeight());
			TexturePaint pt = new TexturePaint(img, texsize);
			g.setPaint(pt);
		}
	}

	public static class CanvasImage {
		public BufferedImage img;
		public Graphics2D g;

		public CanvasImage(BufferedImage nimg) {
			img = nimg;
			g = (Graphics2D) img.createGraphics();
		}

		public CanvasImage(String path) {
			this(ImageCache.toBufferedImage(ImageCache.getImage(path)));
		}

		public CanvasImage(int sx, int sy) {
			this(new BufferedImage(sx, sy, BufferedImage.TYPE_INT_ARGB));
		}
	}
}
