package main.level;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import engine.physic.basic2Dvectorial.ISegment;
import engine.save.room.type1.WallSlice;
import my.util.Cardinal;

public class SlicerMiddleClasses {
	//////////////////////////////////////////////////////////////////////////
	public static class CoCaSegments extends HashMap<Integer, CaSegments> {
		private static final long serialVersionUID = -7606873992888755523L;

		public CoCaSegments() {
			super();
		}

		public HashMap<Integer, EnumMap<Cardinal, ArrayList<ISegment>>> toSuper() {
			HashMap<Integer, EnumMap<Cardinal, ArrayList<ISegment>>> rtn = new HashMap<>();
			for (Entry<Integer, CaSegments> entry1 : this.entrySet()) {
				EnumMap<Cardinal, ArrayList<ISegment>> nenum = new EnumMap<>(Cardinal.class);
				for (Entry<Cardinal, Segments> entry2 : entry1.getValue().entrySet()) {
					nenum.put(entry2.getKey(), new ArrayList<>(entry2.getValue()));
				}
				rtn.put(entry1.getKey(), nenum);
			}
			return rtn;
		}
	}

	public static class CaSegments extends EnumMap<Cardinal, Segments> {
		private static final long serialVersionUID = 1989243544258346078L;

		public CaSegments() {
			super(Cardinal.class);
		}
	}

	public static class Segments extends ArrayList<ISegment> {
		private static final long serialVersionUID = -5748249675447451608L;

		public Segments() {
			super();
		}
	}

	//////////////////////////////////////////////////////////////////////////
	public static class CoWSlices extends HashMap<Integer, ArrayList<WallSlice>> {
		private static final long serialVersionUID = 8346025363148795703L;

		public CoWSlices() {
			super();
		}

		public HashMap<Integer, ArrayList<WallSlice>> toSuper() {
			HashMap<Integer, ArrayList<WallSlice>> rtn = new HashMap<>();
			for (Entry<Integer, ArrayList<WallSlice>> entry : this.entrySet()) {
				rtn.put(entry.getKey(), entry.getValue());
			}
			return rtn;
		}
	}
}
