package engine.save.room.type1;

import my.util.CardinalDirection;

public enum Side {
	north, south, east, west;

	public CardinalDirection toCardinal() {
		switch (this) {
		case north:
			return CardinalDirection.north;
		case south:
			return CardinalDirection.south;
		case west:
			return CardinalDirection.west;
		case east:
			return CardinalDirection.east;
		default:
			return null; // en esperant que �a arrive jamais :')
		}
	}

	public Side toOposite() {
		switch (this) {
		case north:
			return south;
		case south:
			return north;
		case west:
			return east;
		case east:
			return west;
		default:
			return null;
		}
	}

	public boolean isHorizontal() {
		return (this == east || this == west);
	}

	public Side turnP90() {
		switch (this) {
		case north:
			return east;
		case south:
			return west;
		case west:
			return north;
		case east:
			return south;
		default:
			return null;
		}
	}

	public Side turnN90() {
		switch (this) {
		case north:
			return west;
		case south:
			return east;
		case west:
			return south;
		case east:
			return north;
		default:
			return null;
		}
	}

	public int toXMultiplier() {
		switch (this) {
		case north:
			return 0;
		case south:
			return 0;
		case west:
			return -1;
		case east:
			return 1;
		default:
			return 0;
		}
	}

	public int toYMultiplier() {
		switch (this) {
		case north:
			return -1;
		case south:
			return 1;
		case west:
			return 0;
		case east:
			return 0;
		default:
			return 0;
		}
	}

}