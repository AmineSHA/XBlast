package ch.epfl.xblast.server;

import javax.sql.rowset.CachedRowSet;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public enum Block {

	FREE, DESTRUCTIBLE_WALL, INDESTRUCTIBLE_WALL, CRUMBLING_WALL;

    /**
     * 
     * @return true if it's free
     */
	public boolean isFree() {
		return this.equals(FREE);

	}

	/**
	 * 
	 * @return true if it can host player
	 */
	//TODO add more stuff to is free
	public boolean canHostPlayer() {
		return isFree();
	}

	/**
	 * 
	 * @return true if it can cast shadows
	 */
	public boolean castsShadow() {
	    return this.name().contains("_WALL");
	}
	@Override
	
	public String toString(){
	    switch(this){
	    case FREE:
	        default:
	        return "o";
	    case DESTRUCTIBLE_WALL:
	        return "a";
	    case INDESTRUCTIBLE_WALL:
	        return "X";
	    case CRUMBLING_WALL:
	        return "C";
	    
	    }
	}

}
