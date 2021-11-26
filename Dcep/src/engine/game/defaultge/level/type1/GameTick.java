package engine.game.defaultge.level.type1;

import engine.save.Constants;

public class GameTick {

	public static long fromMillis(long millis) {
		return millis / Constants.game_tick_per_second;
	}

	public static long fromSeconds(long seconds) {
		return seconds * 1000 / Constants.game_tick_per_second;
	}

}
