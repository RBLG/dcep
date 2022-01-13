package main.level;

import java.util.ArrayList;
import java.util.Arrays;

import engine.save.room.type1.RoomLoader;
import engine.save.room.type1.RoomState;
import my.util.Log;
import my.util.MyConfigParser;
import my.util.MyConfigParser.ConfigCollection;

public class RoomProcesserMain {

	public static void main(String[] args) {
		ConfigCollection tree = MyConfigParser.parse("res/raw/tree.dcepconf");
		Log.log("", "config tree chargée");
		tree.get().forEach((mgroup, mfields) -> {
			try {
				if (mgroup != "info") {
					// String type = mgroup;
					String output = mfields.get("out");
					String base = "res/raw/" + mfields.get("in") + "/";
					ConfigCollection conf = MyConfigParser.parse(base + "rooms.dcepconf");
					///
					ArrayList<RoomState> rooms = new ArrayList<RoomState>();
					///
					conf.get().forEach((group, fields) -> {// base + fields.get("hitbox")
						RoomProcesser imp = new RoomProcesser(base,group, fields);
						// TODO modif pour add les autres champs
						imp.importRoomImg();
						rooms.add(imp.get());
					});
					RoomProcesser.exportRooms("res/imported/" + output, rooms);
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		});

//		ArrayList<String> paths = ImageCache.checkDirSub("res/raw");
//		ArrayList<RoomState> rooms = new ArrayList<RoomState>();
//		for (String path : paths) {
//			RoomProcesser imp = new RoomProcesser(path);
//			imp.importRoomImg();
//			rooms.add(imp.get());
//
//		}
//		RoomProcesser.exportRooms("res/imported/rooms", rooms);

		ArrayList<RoomState> rooms2 = RoomLoader.importRooms("res/imported/type1.rooms");
		Log.log(null, "rs:" + rooms2.toString());
		for (RoomState room : rooms2) {
			Log.log(null, "r:" + Arrays.asList(room.wallslices));
		}
	}

}

/***
 * 1. recup la balise info 2. parcourir les balises de l'arborescence a.
 * processer la conf b. processer les rooms
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
