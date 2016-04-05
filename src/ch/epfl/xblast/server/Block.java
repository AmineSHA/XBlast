package ch.epfl.xblast.server;

public enum Block {

	FREE, DESTRUCTIBLE_WALL, INDESTRUCTIBLE_WALL, CRUMBLING_WALL,
	BONUS_RANGE(Bonus.INC_RANGE),
	BONUS_BOMB(Bonus.INC_BOMB);


	
	
	 private Bonus maybeAssociatedBonus;
	 
	 private Block(){
		 maybeAssociatedBonus=null;
	 }
	 private Block(Bonus maybeAssociatedBonus){
		 this.maybeAssociatedBonus=maybeAssociatedBonus;
	 }
	 
	public boolean isFree() {
		return (this.equals(FREE));

	}

	public boolean canHostPlayer() {
		return (isFree() || isBonus());
	}

	public boolean castsShadow() {
		return this.name().contains("_WALL");
	}

	public boolean isBonus() {
		return this.name().contains("BONUS");
	}

	public Bonus associatedBonus() {
       return this.maybeAssociatedBonus;
	}

}
