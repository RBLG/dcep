package engine.game.defaultge.level.type1.entity;

import my.util.CardinalDirection;

public enum PlayerVisualState {
	up_move, down_move, left_move, right_move, up_stand, down_stand, left_stand, right_stand;

	public static PlayerVisualState concat(boolean mov, CardinalDirection dir) {
		switch (dir) {
		case north:
			return (mov) ? up_move : up_stand;
		case south:
			return (mov) ? down_move : down_stand;
		case east:
			return (mov) ? right_move : right_stand;
		case west:
			return (mov) ? left_move : left_stand;
		default:
			return down_stand;
		}
	}
}