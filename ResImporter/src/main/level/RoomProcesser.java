package main.level;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.HorizontalSegment;
import engine.physic.basic2Dvectorial.ISegment;
import engine.physic.basic2Dvectorial.VerticalSegment;
import engine.physic.basic2Dvectorial.pathfinding.format.Tile;
import engine.save.room.type1.RoomState;
import engine.save.room.type1.Side;
import engine.save.room.type1.WallSlice;
import main.level.SlicerMiddleClasses.CaSegments;
import main.level.SlicerMiddleClasses.CoCaSegments;
import main.level.SlicerMiddleClasses.Segments;
import engine.save.room.type1.RoomState.Door;
import engine.save.room.type1.RoomType;
import my.util.Cardinal;
import my.util.ImageCache;
import my.util.Log;

public class RoomProcesser {

	protected String path;
	protected RoomState room;
	protected String name;
	protected HashMap<String, String> fields;
	protected float ratio;
	// private BufferedImage img2; // TODO replacer img local

	public RoomProcesser(String base, String nname, HashMap<String, String> nfields) {
		this.fields = nfields;
		this.name = nname;
		this.path = base + fields.get("hitbox");
	}

	public void importRoomImg() {
		BufferedImage img = (BufferedImage) ImageCache.getImageFullPath(path);
		if (img == null) {
			throw new RuntimeException("chemin invalide: " + path);
		} else if (640 % img.getWidth() != 0 || 360 % img.getHeight() != 0
				|| 640 / img.getWidth() != 360 / img.getHeight()) {
			throw new RuntimeException("taille invalide");
		}
		ratio = 640 / img.getWidth();
		ratio *= Room.simscale;
		// img = resize(img, ratio);

		// ArrayList<int[]> hitboxes = this.detectWalls(img);
		EnumMap<Side, Door> doors = this.detectDoors(img);
		CoCaSegments segs = this.detectBounds(img);

		RoomSlicer slicer = new RoomSlicer(segs);

		// TODO a voir si OrDefault est une bonne idée ou une exception est mieux
		RoomType type = RoomType.valueOf(fields.getOrDefault("type", "room"));
		String visconf = fields.getOrDefault("confvisual", "stages/type1/togen/default/");
		String slog = "\n   tiles:" + slicer.tiles.size() + " [";
		for (Tile tile : slicer.tiles) {
			slog += " |" + tile.ToString();
		}
		///////////// C'EST ICI /////////////////////////
		this.room = new RoomState(segs.toSuper(), doors, slicer.tiles, type, slicer.vslices.get(69), visconf);
		/////////////////////////////////////////////////

		String str = name;
		if (Boolean.TRUE) {
			str += "\nSEGMENTS: \n north :";
			for (ISegment seg : segs.get(69).get(Cardinal.north)) {
				str += " seg:" + seg.getX() + "-" + seg.getX2();
			}
			str += "\n south: ";
			for (ISegment seg : segs.get(69).get(Cardinal.south)) {
				str += " seg:" + seg.getX() + "-" + seg.getX2();
			}
		}

		if (Boolean.TRUE) {
			str += "\nSLICES:\n north :";
			for (WallSlice wall : slicer.vslices.get(69)) {
				str += " sli:" + wall.start + "-" + wall.end;
			}
			str += "\n south: ";
			for (WallSlice wall : slicer.vslices.get(69)) {
				str += " sli:" + wall.start + "-" + wall.end;
			}
		}
		Log.log(this, str);

		String e = segs.get(69).get(Cardinal.north).size() + "|" //
				+ segs.get(69).get(Cardinal.south).size() + "|" //
				+ segs.get(69).get(Cardinal.east).size() + "|" //
				+ segs.get(69).get(Cardinal.west).size();
		slog += "]\n   imported: slices:" + slicer.vslices.size() + " doors:" + doors.size() + " walls:" + e + "\n";
		Log.log(this, slog);
	}

//	protected ArrayList<int[]> detectWalls(BufferedImage img) {
//		ArrayList<Integer> colors = new ArrayList<Integer>();
//		ArrayList<int[]> hitboxes = new ArrayList<int[]>();
//
//		for (int ity1 = 0; ity1 < img.getHeight(); ity1++) {
//			for (int itx1 = 0; itx1 < img.getWidth(); itx1++) {
//				int color = img.getRGB(itx1, ity1);
//				if (color != -1) {
//					boolean contained = colors.stream().anyMatch((Integer col) -> col == color);
//					if (!contained) {
//						colors.add(color);
//						int itx2 = itx1;
//						while ((itx2 + 1 < img.getWidth()) && (color == img.getRGB(itx2 + 1, ity1))) {
//							itx2++;
//						}
//						int ity2 = ity1;
//						while ((ity2 + 1 < img.getHeight()) && (color == img.getRGB(itx2, ity2 + 1))) {
//							ity2++;
//						}
//						int x1, y1, x2, y2;
//						x1 = (int) (itx1 * ratio);
//						y1 = (int) (ity1 * ratio);
//						x2 = (int) (itx2 * ratio);
//						y2 = (int) (ity2 * ratio);
//						hitboxes.add(new int[] { x1, y1, x2 + 1, y2 + 1 });
//						// Log.log(this, "x1:" + x1 + " y1:" + y1 + " x2:" + x2 + " y2:" + y2);
//					}
//				}
//			}
//		}
//		return hitboxes;
//	}

	protected CoCaSegments detectBounds(BufferedImage img) {
		CoCaSegments segs = new CoCaSegments();
		// les tiles nw,ne,sw,se
		int nw, ne, sw, se;
		// si intersection en nord, sud, est et ouest
		boolean in, is, ie, iw;
		boolean stx, sty, stx2, sty2;
		for (int ity1 = -1; ity1 < img.getHeight() + 2; ity1++) {
			for (int itx1 = -1; itx1 < img.getWidth() + 2; itx1++) {
				nw = getRGBOrDefault(itx1 - 1, ity1 - 1, img);
				ne = getRGBOrDefault(itx1, ity1 - 1, img);
				sw = getRGBOrDefault(itx1 - 1, ity1, img);
				se = getRGBOrDefault(itx1, ity1, img);
				in = nw == ne;
				is = sw == se;
				ie = ne == se;
				iw = nw == sw;
				// si un seg part vers +x (dessus)
				stx = !ie && !(in && is);
				// (dessous)
				stx2 = !ie && !(in && is);
				// si un seg part vers +y (gauche)
				sty = !is && !(iw && ie);
				// (droite)
				sty2 = !is && !(iw && ie);
				if (stx) {
					int itx2 = itx1 + 1;
					int tn = ne, ts = se;
					while (tn == ne && ts != ne) {
						tn = getRGBOrDefault(itx2, ity1 - 1, img);
						ts = getRGBOrDefault(itx2, ity1, img);
						itx2++;
					} // si y a mur au nord donc repond aux collisions vers le nord

					segs.computeIfAbsent(ne, (e) -> (new CaSegments()));
					segs.get(ne).computeIfAbsent(Cardinal.north, (e) -> new Segments());
					float px1 = itx1 * ratio, //
							px2 = (itx2 - 2) * ratio, //
							py = (ity1 - 1) * ratio;
					ISegment nseg = new HorizontalSegment((int) px1, (int) px2, (int) py, ne);
					segs.get(ne).get(Cardinal.north).add(nseg);

				}
				if (stx2) {
					int itx2 = itx1 + 1, tn = ne, ts = se;
					while (tn != se && ts == se) {
						tn = getRGBOrDefault(itx2, ity1 - 1, img);
						ts = getRGBOrDefault(itx2, ity1, img);
						itx2++;
					}
					segs.computeIfAbsent(se, (e) -> (new CaSegments()));
					segs.get(se).computeIfAbsent(Cardinal.south, (e) -> new Segments());
					float px1 = (itx1) * ratio, //
							px2 = (itx2 - 2) * ratio, //
							py = (ity1) * ratio;
					ISegment nseg = new HorizontalSegment((int) px1, (int) px2, (int) py, se);
					segs.get(se).get(Cardinal.south).add(nseg);

				}
				if (sty) {
					int ity2 = ity1 + 1, tw = sw, te = se;
					while (tw == sw && te != sw) {
						tw = getRGBOrDefault(itx1 - 1, ity2, img);
						te = getRGBOrDefault(itx1, ity2, img);
						ity2++;
					}
					segs.computeIfAbsent(sw, (e) -> (new CaSegments()));
					segs.get(sw).computeIfAbsent(Cardinal.west, (e) -> new Segments());
					float px = (itx1 - 1) * ratio, //
							py1 = (ity1) * ratio, //
							py2 = (ity2 - 2) * ratio;
					ISegment nseg = new VerticalSegment((int) px, (int) py1, (int) py2, sw);
					segs.get(sw).get(Cardinal.west).add(nseg);

				}
				if (sty2) {
					int ity2 = ity1 + 1, tw = sw, te = se;
					while (tw != se && te == se) {
						tw = getRGBOrDefault(itx1 - 1, ity2, img);
						te = getRGBOrDefault(itx1, ity2, img);
						ity2++;
					}
					segs.computeIfAbsent(se, (e) -> (new CaSegments()));
					segs.get(se).computeIfAbsent(Cardinal.east, (e) -> new Segments());
					float px = (itx1) * ratio, //
							py1 = (ity1) * ratio, //
							py2 = (ity2 - 2) * ratio;
					ISegment nseg = new VerticalSegment((int) px, (int) py1, (int) py2, se);
					segs.get(se).get(Cardinal.east).add(nseg);

				}
			}
		}
		return segs;
	}

	protected int getRGBOrDefault(int itx, int ity, BufferedImage img) {
		if (itx < 0 || itx >= img.getWidth() || ity < 0 || ity >= img.getHeight()) {
			return 68;
		}
		if (img.getRGB(itx, ity) != -1) {// pour faire comme si la map était en noir et blanc
			return 69; // 69 c'est le noir trust me
		} else {
			return -1;
		}
		// return img.getRGB(itx, ity);
	}

	protected EnumMap<Side, Door> detectDoors(BufferedImage img) {
		EnumMap<Side, Door> doors = new EnumMap<>(Side.class);

		DetectionState state = new DetectionState();
		state.img = img;
		state.rtn = doors;

		detectDoorsLoop(state, true, 0, Side.north);
		detectDoorsLoop(state, true, img.getHeight() - 1, Side.south);
		detectDoorsLoop(state, false, 0, Side.west);
		detectDoorsLoop(state, false, img.getWidth() - 1, Side.east);

		return doors;
	}

	protected static class DetectionState {
		BufferedImage img;
		Side side;
		EnumMap<Side, Door> rtn;
		boolean reading = false;
		int beg = 0;
		int end = 0;
	}

	protected void detectDoorsLoop(DetectionState state, boolean onX, int pos2, Side side) {
		state.side = side;
		if (onX) {
			for (int it1 = 0; it1 < state.img.getWidth(); it1++) {
				detectDoorsSubSub(it1, pos2, it1, state);
			}
			state.end = state.img.getWidth() - 1 + 1; // +1 pour le passage du systeme 0-9 au 1-10
		} else {
			for (int it2 = 0; it2 < state.img.getHeight(); it2++) {
				detectDoorsSubSub(pos2, it2, it2, state);
			}
			state.end = state.img.getHeight() - 1 + 1;
		}
		if (state.reading) {
			state.reading = false;
			state.rtn.put(state.side,
					new Door(state.side, (int) (state.beg * ratio), (int) ((state.end - state.beg) * ratio)));
		}
	}

	protected void detectDoorsSubSub(int x, int y, int it, DetectionState stt) {
		// Log.log(this, "x:" + x + " y:" + y + " w:" + state.img.getWidth() + " h:" +
		// state.img.getHeight());
		if (stt.img.getRGB(x, y) == -1) {
			if (!stt.reading) { // nouvelle porte
				stt.reading = true;
				stt.beg = it;
			}
		} else {
			if (stt.reading) {
				stt.reading = false;
				stt.end = it - 1 + 1;
				stt.rtn.put(stt.side, new Door(stt.side, (int) (stt.beg * ratio), (int) ((stt.end - stt.beg) * ratio)));
			}

		}
	}

	public RoomState get() {
		return this.room;
	}

	public static void exportRooms(String path, ArrayList<RoomState> rooms) {
		try (//
				FileOutputStream fout = new FileOutputStream(path);
				ObjectOutputStream oos = new ObjectOutputStream(fout);//
		) {
			oos.writeObject(rooms);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage resize(BufferedImage img, float ratio) {
		float nwi = img.getWidth() * ratio, nhe = img.getHeight() * ratio;
		Image tmp = img.getScaledInstance((int) nwi, (int) nhe, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage((int) nwi, (int) nhe, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}
}
