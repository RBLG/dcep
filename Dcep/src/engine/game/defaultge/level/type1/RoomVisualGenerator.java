package engine.game.defaultge.level.type1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.physic.basic2Dvectorial.pathfinding.format.Tile;
import engine.physic.basic2Dvectorial.pathfindingV2.ResizedNavigationMesh;
import engine.render.engine2d.DrawLayer;
import engine.render.engine2d.renderable.I2DRenderable;
import engine.render.engine2d.renderable.StillImage;
import engine.save.room.type1.WallSlice;
import my.util.ImageCache;
import my.util.Log;
import my.util.MyConfigParser;
import my.util.MyConfigParser.ConfigCollection;
import my.util.geometry.IRectangle;

public final class RoomVisualGenerator extends Room {
	private RoomVisualGenerator() {
		super(null, 0, 0, null, null);
	}

	public static int coloor = 0;

	public static void genVisual(Room room) {
		ConfigCollection visconf = MyConfigParser.parse("res/" + room.state.visconfpath + "visuals.dcepconf");
		ArrayList<WallSlice> slices = room.state.wallslices;

		visconf.get().forEach((gname, fields) -> {
			if (gname != "info") {
				// load les truc
			}
		});// ajout du sol
		PatternImage bg = new PatternImage(room.state.visconfpath + visconf.get("base", "background"));
		room.visuals.add(new RoomVisual(DrawLayer.Room_Floor, new StillImage(bg.img, 0, 0)));

		// layer des murs
		CanvasImage wallcv = new CanvasImage(Room.rosizex, Room.rosizey);
		// ombre du haut des murs
		CanvasImage shadcv = new CanvasImage(Room.rosizex, Room.rosizey);
		shadcv.g.setComposite(AlphaComposite.SrcOver.derive(1f));
		// image de mur qui est appliqué au bas des slices
		PatternImage wallpat = new PatternImage(room.state.visconfpath + visconf.get("base", "basewall"));
		// PatternImage lowwallpat = new PatternImage(room.state.visconfpath +
		// visconf.get("base", "lowwall"));

		int imgh = wallpat.img.getHeight();
		Log.e("\n Room:");
		// int wallcolor = 0xF000F000;
		for (WallSlice maxslice : slices) { // TODO a adapter pour gerer l'orientation des slices
			WallSlice slice = WallSlice.newScaled(maxslice, Room.invsimscale);
			//////////////////////////////////////////////////////////////////
			IRectangle mysli = slice.getZone();// .to010from09();
			Rectangle awtsli = mysli.toAwt();
			Log.e("x:" + maxslice.start + " y:" + maxslice.top.getY() + " x2:" + maxslice.end + " y2:" + maxslice.bottom.getY());
			Log.e(" pre:" + maxslice.ToString() + " scal:" + slice.ToString() + " mysli: " + mysli.ToString() + " awt:"
					+ awtsli.toString());
			wallcv.g.setColor(new Color(0x101010));
			// wallcolor += 666;
			// wallcv.g.setColor(new Color(wallcolor));
			wallcv.g.fill(awtsli); // fond noir

			shadcv.g.setColor(new Color(0x10, 0x10, 0x10, 100));
			// shadcv.g.setColor(new Color((0x101010 & 0xFF0000) >> 16, (0x101010 &
			// 0x00FF00) >> 8, (0x101010 & 0x0000FF), 100));
			// si c'est pas le mur du bas
			if (mysli.getY2() < Room.rosizey) {
				Graphics2D gra2 = (Graphics2D) wallcv.g.create(awtsli.x, awtsli.y, awtsli.width, awtsli.height);

				if (mysli.getHeight() < imgh + 1000) {
					Rectangle wa = new Rectangle(//
							slice.bottom.getX() - mysli.getX(), //
							mysli.getHeight() - imgh, //
							slice.bottom.getX2() + 1, //
							imgh//
					);
					shadcv.g.fillRect(awtsli.x, awtsli.y + 10 - imgh, awtsli.width, wa.height - 10); // ombre du mur
					wallpat.setItPaint(wa.x, wa.y, gra2);
					gra2.fill(wa);
				}
//				 
				gra2.finalize();
			} else {
				shadcv.g.fillRect(awtsli.x, awtsli.y + 10 - imgh, awtsli.width, imgh - 10);
			}

		}

		room.visuals.add(new RoomVisual(DrawLayer.Room_Walls, new StillImage(wallcv.img, 0, 0)));
		// room.visuals.add(new RoomVisual(DrawLayer.Room_Shaders, new
		// StillImage(shadcv.img, 0, 0)));

		if (Boolean.FALSE) {// TODO trud de test, a enlever
			/////////////////////////////////////////////////////////
			CanvasImage navtestcv = new CanvasImage(Room.rosizex, Room.rosizey);
			navtestcv.g.setComposite(AlphaComposite.SrcOver.derive(0.3f));
			int color = 0x8000F000;
			for (Tile maxtile : room.state.navmesh) {
				Tile tile = new Tile(maxtile, Room.invsimscale);
				color += 666;
				navtestcv.g.setColor(new Color(color));
				Rectangle tilre = new Rectangle(tile.x, tile.y, tile.x2 - tile.x, tile.y2 - tile.y);
				navtestcv.g.fill(tilre);
				navtestcv.g.setColor(new Color(0x00FFFFFF));
				navtestcv.g.draw(tilre);
			}
			room.visuals.add(new RoomVisual(DrawLayer.Room_Shaders, new StillImage(navtestcv.img, 0, 0)));
			/////////////////////////////////////////////////////////
			ResizedNavigationMesh test = room.pathfinder.getCache()
					.getFittingNavMesh(new IRectangle.Rectangle(0, 0, 20, 17));
			navtestcv = new CanvasImage(Room.rosizex, Room.rosizey);
			navtestcv.g.setComposite(AlphaComposite.SrcOver.derive(0.3f));
			color = 0x8000F000;
			for (Tile tile : test.getTiles()) {
				if (tile.x2 - tile.x < 0 || tile.y2 - tile.y < 0) {
					Log.log("roomvisgen", "tile negative waaa");
				}
				Log.log("rvisgen", "nbs:" + tile.neighbors.size());

				color += 666;
				navtestcv.g.setColor(new Color(color));
				Rectangle tilre = new Rectangle(tile.x, tile.y, tile.x2 - tile.x, tile.y2 - tile.y);
				navtestcv.g.fill(tilre);
				navtestcv.g.setColor(new Color(0x00FFFFFF));
				navtestcv.g.draw(tilre);
			}
			room.visuals.add(new RoomVisual(DrawLayer.Room_Shaders, new StillImage(navtestcv.img, 0, 0)));
		}

	}

	public static class RoomVisual {
		public final DrawLayer lay;
		public final I2DRenderable rd;

		public RoomVisual(DrawLayer nlay, StillImage nrd) {
			lay = nlay;
			rd = nrd;
		}
	}

	public static class PatternImage {
		BufferedImage img;

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
		BufferedImage img;
		Graphics2D g;

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
