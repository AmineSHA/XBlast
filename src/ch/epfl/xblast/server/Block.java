package ch.epfl.xblast.server;

public enum Block {

	FREE, DESTRUCTIBLE_WALL, INDESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB, BONUS_RANGE;

	public boolean isFree() {
		return (this.equals(FREE));

	}

	public boolean canHostPlayer() {
		return (isFree() || isBonus());
	}

	public boolean castsShadow() {
		return (this == DESTRUCTIBLE_WALL || this == INDESTRUCTIBLE_WALL || this == CRUMBLING_WALL);
	}

	public boolean isBonus() {
		return (this.equals(BONUS_BOMB) || this.equals(BONUS_RANGE));
	}

	public Bonus associatedBonus() {
       
	}

}
