package engine.save.room.type1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class RoomLoader {
	@SuppressWarnings("unchecked")
	public static ArrayList<RoomState> importRooms(String path) {
		try (//
				FileInputStream fis = new FileInputStream(path);
				ObjectInputStream ois = new ObjectInputStream(fis);//
		) {

			ArrayList<RoomState> results = new ArrayList<RoomState>();
			Object obj = ois.readObject();
			if (obj instanceof ArrayList<?>) {
				results = (ArrayList<RoomState>) obj;
			}
			return results;
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			e.printStackTrace();
		}
		return null;
	}
}
