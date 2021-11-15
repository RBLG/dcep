package engine.game.defaultge.level.type1;

import engine.save.Constants;

public class GameTick {

	public static long fromMillis(long millis) {
		return millis / Constants.millis_between_game_ticks;
	}

}
