package engine.game.defaultge.level.type1.variants;

import java.util.ArrayList;
import java.util.EnumMap;

public abstract class IType1Skin {
	protected EnumMap<T1Res, String> map = new EnumMap<>(T1Res.class);

	public IType1Skin() {
	}

	public String get(T1Res name) {
		return this.map.get(name);
	}

	protected void gen(String path) {
		for (T1Res name : T1Res.values()) {
			map.put(name, path + "/" + name + ".png");
		}
	}

	public ArrayList<T1Res> checkmissing() { // TODO déplacer dans les tests
		ArrayList<T1Res> missings = new ArrayList<>();
		for (T1Res name : T1Res.values()) {
			if (map.get(name) == null) {
				missings.add(name);
			}
		}
		return missings;
	}

	public enum T1Res {
		player_idle_up, player_idle_left, player_idle_down, player_idle_right, //
		player_walk_up, player_walk_left, player_walk_down, player_walk_right, //
		player_strike_up, player_strike_left, player_strike_down, player_strike_right //
		;
	}

}
