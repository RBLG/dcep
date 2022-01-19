package main.level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;

import javax.xml.ws.Holder;

import engine.game.defaultge.level.type1.Room;
import engine.physic.basic2Dvectorial.HorizontalSegment;
import engine.physic.basic2Dvectorial.ISegment;
import engine.physic.basic2Dvectorial.VerticalSegment;
import engine.physic.basic2Dvectorial.pathfinding.format.Junction;
import engine.physic.basic2Dvectorial.pathfinding.format.Neighbor;
import engine.physic.basic2Dvectorial.pathfinding.format.Tile;
import engine.save.room.type1.WallSlice;
import main.level.SlicerMiddleClasses.*;
import my.util.Cardinal;
import my.util.Log;
import my.util.geometry.IRectangle;
import my.util.geometry.IVector;

public class RoomSlicer {

	protected CoCaSegments segs;
	protected CoWSlices vslices, hslices;
	protected FabricMap fabric;
	protected ArrayList<Tile> tiles;

	public RoomSlicer(CoCaSegments nsegs) {
		segs = nsegs;
		vslices = this.sliceVertically(segs);
		hslices = this.sliceHorizontally(segs);
		fabric = this.weaveMap(vslices, hslices, 0xFFFFFFFF);
		tiles = makeNavMesh(fabric);

	}

	public CoWSlices sliceVertically(CoCaSegments allsegs) {
		// -10 pour etre tranquille
		ISegment hordef = new HorizontalSegment(0, Room.rosizex * Room.simscale, -10 * Room.simscale, 0);
		// verdef = new VerticalSegment(-10, 0, Room.rosizey, 0);
		CoWSlices rtn = new CoWSlices();

		for (Entry<Integer, CaSegments> entry : allsegs.entrySet()) {
			CaSegments segs = entry.getValue();
			ArrayList<WallSlice> slices = new ArrayList<>();
			for (ISegment base : segs.get(Cardinal.north)) {
				if (base.getY() <= 0) {
					continue;
				}
				int lstart = base.getX();
				ISegment topseg = hordef;
				ISegment lstopseg = hordef;
				for (int it1 = base.getX(); it1 <= base.getX2(); it1++) {//y avait un < au lieu de <=
					lstopseg = topseg;
					int height = Integer.MIN_VALUE;
					for (ISegment top : segs.get(Cardinal.south)) {
						if (top.getXX().isLooselyContaining(it1)) {
							if (height < top.getY() && top.getY() <= base.getY()) {
								height = top.getY();
								topseg = top;
							}
						}
					}
					if (!lstopseg.equals(topseg)) {
						if (it1 != base.getX()) {
							// -1 parce que it est a n+1 (ou est a n et la fin a n-1)
							slices.add(new WallSlice(lstopseg, base, lstart, it1 - 1, base.getColor()));
						}
						lstart = it1;
					}
				}
				slices.add(new WallSlice(topseg, base, lstart, base.getX2(), base.getColor()));
			}
			rtn.put(entry.getKey(), slices);
		}
		return rtn;
	}

	// TODO fix le duplicate
	public CoWSlices sliceHorizontally(CoCaSegments allsegs) {
		// -10 pour etre tranquille
		ISegment verdef = new VerticalSegment(-10 * Room.simscale, 0, Room.rosizey * Room.simscale, 0);
		CoWSlices rtn = new CoWSlices();

		for (Entry<Integer, CaSegments> entry : allsegs.entrySet()) {
			CaSegments segs = entry.getValue();
			ArrayList<WallSlice> slices = new ArrayList<>();
			for (ISegment base : segs.get(Cardinal.west)) {
				if (base.getX() <= 0) {
					continue;
				}
				int lstart = base.getY();
				ISegment topseg = verdef;
				ISegment lstopseg = verdef;
				for (int it1 = base.getY(); it1 <= base.getY2(); it1++) {
					lstopseg = topseg;
					int height = Integer.MIN_VALUE;
					for (ISegment top : segs.get(Cardinal.east)) {
						if (top.getYY().isLooselyContaining(it1)) {
							if (height < top.getX() && top.getX() < base.getX()) {
								height = top.getX();
								topseg = top;
							}
						}
					}
					if (!lstopseg.equals(topseg)) {
						if (it1 != base.getY()) {
							slices.add(new WallSlice(lstopseg, base, lstart, it1 - 1, base.getColor()));
						}
						lstart = it1;
					}
				}
				slices.add(new WallSlice(topseg, base, lstart, base.getY2(), base.getColor()));
			}
			rtn.put(entry.getKey(), slices);
		}
		return rtn;
	}

	public FabricMap weaveMap(CoWSlices versli, CoWSlices horsli, int color) {
		FabricMap rtn = initiateWeaving(versli, horsli, color);
		intertwine(rtn);
		sort(rtn);
		return rtn;
	}

	public FabricMap initiateWeaving(CoWSlices versli, CoWSlices horsli, int color) {
		FabricMap rtn = new FabricMap();
		for (WallSlice hs : horsli.get((Integer) color)) {
			int x, y, x2, y2;
			x = hs.top.getX();
			y = hs.start;
			x2 = hs.bottom.getX();
			y2 = hs.end;
			rtn.hor.add(new Slice(x, y, x2, y2));
		}
		for (WallSlice vs : versli.get((Integer) color)) {
			int x, y, x2, y2;
			x = vs.start;
			y = vs.top.getY();
			x2 = vs.end;
			y2 = vs.bottom.getY();
			rtn.ver.add(new Slice(x, y, x2, y2));
		}
		return rtn;
	}

	public void intertwine(FabricMap map) {
		for (Slice hori : map.hor) {
			for (Slice vert : map.ver) {
				if (!(hori.x > vert.x2 || vert.x > hori.x2 || hori.y > vert.y2 || vert.y > hori.y2)) {
					hori.crossing.add(vert);
					vert.crossing.add(hori);
				}
			}
		}
	}

	public void sort(FabricMap map) {
		for (Slice hori : map.hor) {
			hori.crossing.sort((sl, sl2) -> {
				return sl.x - sl2.x;
			});
		} // bah du coup ça a l'air de marcher
		for (Slice vert : map.ver) {
			vert.crossing.sort((sl, sl2) -> {
				return sl.y - sl2.y; // in doc we trust
			});
		}
	}

	public ArrayList<Tile> makeNavMesh(FabricMap map) {
		ArrayList<Tile> tiles = group(map);
		optimize(tiles);
		weaveGraph(tiles);
		linkIndex(tiles);
		return tiles;
	}

	public ArrayList<Tile> group(FabricMap map) {
		ArrayList<Tile> tiles = new ArrayList<>();
		for (Slice hoall : map.hor) {
			ArrayList<Range> ranges = groupGetRangesSub(hoall);
			tiles.addAll(groupRangeToTileSub(ranges, hoall));
		}
		return tiles;
	}

	public ArrayList<Range> groupGetRangesSub(Slice hoall) {
		ArrayList<Range> ranges = new ArrayList<>();
		for (Slice ve : hoall.crossing) {
			int orit = 0;
			int itho = 0;
			for (Slice ho : ve.crossing) {
				if (hoall.equals(ho)) {
					orit = itho;
				}
				itho++;
			}
			int start = 0 - orit;
			int end = ve.crossing.size() - orit;
			ranges.add(new Range(orit, start, end));
		}
		return ranges;
	}

	public ArrayList<Tile> groupRangeToTileSub(ArrayList<Range> ranges, Slice hoall) {

		ArrayList<PreTile> incompletes = new ArrayList<>();
		Range ls = ranges.get(0);
		int iter = 0;
		for (Range range : ranges) {
			if (iter == 0) {
				incompletes.add(new PreTile(range.start, range.end, 0, range.origin));
			} else if (range.start > ls.start || range.end < ls.end) {
				ArrayList<PreTile> reshapes = new ArrayList<>();
				for (PreTile pt : incompletes) {
					if (pt.length > iter) {
						PreTile rp = pt.getReshape(range.start, range.end);
						if (rp != null) {
							pt.limitLength(iter);
							reshapes.add(rp);
						}
					}
				}
				incompletes.addAll(reshapes);
			}
			if (range.start > ls.start || range.end > ls.end) {
				incompletes.add(new PreTile(range.start, range.end, iter, range.origin));
			}
			ls = range;
			iter++;
		}
		// Log.log(this, "it:" + iter + " ranges.size:" + ranges.size());
		for (PreTile pt : incompletes) {
			pt.limitLength(ranges.size());
		}
		//////////////////////////////////
		// deplacable dans une autre fonction
		ArrayList<Tile> tiles = new ArrayList<>();
		for (PreTile prt : incompletes) {
			int li = prt.length - 1;
			Range rg = ranges.get(li);

			int x, y, x2, y2;
			x = hoall.crossing.get(prt.beg).x;
			y = hoall.crossing.get(prt.beg).crossing.get(prt.start + prt.ori).y;
			x2 = hoall.crossing.get(li).x2;
			y2 = hoall.crossing.get(li).crossing.get(prt.end + rg.origin - 1).y2;
			tiles.add(new Tile(x, y, x2, y2));
		}

		return tiles;
	}

	public void optimize(ArrayList<Tile> ntiles) {
		Holder<Boolean> done = new Holder<>(false);
		Holder<Integer> iter = new Holder<>(0);
		while (!done.value) {
			done.value = true;
			compareAll(ntiles, (t1, t2) -> {
				if (t2.neighbors == null) {
					return 0;
				}
				if (isDuplicate(t1, t2)) {
					// marquage pour suppression
					t1.neighbors = null;
					done.value = false;
				}
				return 0;
			});
			ntiles.removeIf((tile) -> {
				return tile.neighbors == null;
			});
			compareAll(ntiles, (t1, t2) -> {
				done.value &= !tryToReduce(t1, t2, iter.value);
				return 0;
			});
			ArrayList<Tile> news = new ArrayList<>();
			compareAll(ntiles, (t1, t2) -> {
				Tile rtn = tryToCut(t1, t2, iter.value);
				if (rtn != null) {
					done.value = false;
					if (rtn.x >= rtn.x2 || rtn.y >= rtn.y2) {
						Log.log(this, "Tile invalide");
					}
					news.add(rtn);
				}
				return 0;
			});
			ntiles.addAll(news);
			iter.value++;
		}

	}

	// ps: le return du comparator est osef
	public void compareAll(ArrayList<Tile> ntiles, Comparator<Tile> comp) {
		for (Tile t1 : ntiles) {
			for (Tile t2 : ntiles) {
				comp.compare(t1, t2);
			}
		}
	}

	public boolean isDuplicate(Tile t1, Tile t2) {
		if (t1.equals(t2)) {
			return false;
		} // si toutes les co de t1 sont dans t2
		return (t1.x >= t2.x && t1.x2 <= t2.x2 && t1.y >= t2.y && t1.y2 <= t2.y2);

	}

	public boolean tryToReduce(Tile t1, Tile t2, int tries) {
		if (t1.equals(t2)) {
			return false;
		}
		// si t2 peut aussi etre reduit,
		// verifie que t1 est moins important que t2

		// test priorité
		if (t2.getSmallestSide() < t1.getSmallestSide()) {
			// test t2 encastre t1
			if (tries < 100) {
				return true;
			}
			if (t1.getXX().isLooselyContaining(t2.getXX()) && //
					(t2.getYY().isOverlappingAndBefore(t1.getYY()) || t2.getYY().isOverlappingAndAfter(t1.getYY()))) {
				return false;
			} else if (t1.getYY().isLooselyContaining(t2.getYY()) && //
					(t2.getXX().isOverlappingAndBefore(t1.getXX()) || t2.getXX().isOverlappingAndAfter(t1.getXX()))) {
				return false;
			}
		}

		// si aligné en x
		if (t2.getXX().isLooselyContaining(t1.getXX())) {
			// si y est hors du rectangle et y2 est dedans
			if (t1.getYY().isOverlappingAndBefore(t2.getYY())) {
				t1.y2 = t2.y;
				return true;
			} // si y2 est hors du rectangle et y est dedans
			if (t1.getYY().isOverlappingAndAfter(t2.getYY())) {
				t1.y = t2.y2;
				return true;
			}
			return false;
		}
		// si aligné en y
		if (t2.getYY().isLooselyContaining(t1.getYY())) {
			// si x est hors du rectangle et x2 est dedans
			if (t1.getXX().isOverlappingAndBefore(t2.getXX())) {
				t1.x2 = t2.x;
				return true;
			} // si x2 est hors du rectangle et x est dedans
			if (t1.getXX().isOverlappingAndAfter(t2.getXX())) {
				t1.x = t2.x2;
				return true;
			}
		}

		return false;
	}

	public Tile tryToCut(Tile t1, Tile t2, int tries) {
		if (t1.equals(t2)) {
			return null;
		}
		// verifie que t1 est moins important que t2

		// condition qualité (ratio)
		if (t2.getSmallestSide() < t1.getSmallestSide()) {
			if (tries < 150) {
				return null;
			}
			if (t2.isCrossing(t1)) {
				return null;

			}
		}

		Tile rtn = null;
		if (t1.isCrossingVertically(t2)) {
			rtn = new Tile(t1.x, t2.y2, t1.x2, t1.y2);
			t1.y2 = t2.y;
		} else if (t1.isCrossingHorizontally(t2)) {
			rtn = new Tile(t2.x2, t1.y, t1.x2, t1.y2);
			t1.x2 = t2.x;
		}
		return rtn;
	}

	public void printAverageRatio(ArrayList<Tile> ntiles) {
		int ratio = 0;
		for (Tile tile : ntiles) {
			ratio += tile.getSizeRatio();
		}
		ratio /= ntiles.size();
		Log.log(this, "average ratio:" + ratio);
	}

	public void weaveGraph(ArrayList<Tile> ntiles) {
		compareAll(ntiles, (t1, t2) -> {
			IRectangle cro = t1.getOverlappingArea(t2);
			if (cro != null && !cro.isAPoint()) {
				IVector dir1 = t1.getCenter().getVectorTo(t2.getCenter());
				dir1 = dir1.getSigns();
				Junction jc = new Junction(cro, dir1);
				t1.neighbors.add(new Neighbor(t1, t2, jc));
				t2.neighbors.add(new Neighbor(t2, t1, jc));
			}
			return 0;
		});
	}

	public void linkIndex(ArrayList<Tile> ntiles) {
		int index = 0;
		for (Tile tile : ntiles) {
			tile.index = index;
			index++;
		}
	}

	public static class PreTile {
		int start, end, beg, length = Integer.MAX_VALUE;
		int ori;

		public PreTile(int nstart, int nend, int nbeg, int nori) {
			start = nstart;
			end = nend;
			beg = nbeg;
			ori = nori;
		}

		public PreTile getReshape(int nstart, int nend) {
			if (nstart > end || nend < start) {
				return null;
			} else if (nstart < start && nend > end) {
				return null;
			}
			nstart = (nstart > start) ? nstart : start;
			nend = (nend < end) ? nend : end;
			return new PreTile(nstart, nend, beg, ori);
		}

		public void limitLength(int limit) {
			if (length > limit) {
				length = limit;
			}
		}
	}

	public static class Range {
		int origin, start, end;

		public Range(int norigin, int nstart, int nend) {
			origin = norigin;
			start = nstart;
			end = nend;
		}
	}

	public static class FabricMap {
		ArrayList<Slice> ver, hor;

		public FabricMap() {
			ver = new ArrayList<>();
			hor = new ArrayList<>();
		}
	}

	public static class Slice {
		int x, y, x2, y2;
		ArrayList<Slice> crossing;

		public Slice(int nx, int ny, int nx2, int ny2) {
			x = nx;
			y = ny;
			x2 = nx2;
			y2 = ny2;
			crossing = new ArrayList<>();
		}
	}

}
