package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public enum Block {

    /**
     * A free block
     */
    FREE,
    /**
     * a destructible wall
     */
    DESTRUCTIBLE_WALL,
    /**
     * an indestructible wall
     */
    INDESTRUCTIBLE_WALL,
    /**
     * a crumbling wall
     */
    CRUMBLING_WALL,
    /**
     * a bomb incremantor bonus
     */
    BONUS_RANGE(Bonus.INC_RANGE),
    /**
     * a range incremantor bonus
     */
    BONUS_BOMB(Bonus.INC_BOMB);

    private Bonus maybeAssociatedBonus;

    private Block() {
        maybeAssociatedBonus = null;
    }

    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    /**
     * checks if it's free
     * @return true if it's free
     */
    public boolean isFree() {
        return (this.equals(FREE));

    }

    /**
     * checks if it can host a player
     * @return true if it can host player
     */

    public boolean canHostPlayer() {
        return (isFree() || isBonus());
    }

    /**
     * checks if it can cast shadow
     * @return true if it can cast shadows
     */
    public boolean castsShadow() {
        return this.name().contains("_WALL");
    }

    /**
     * Check's if it is a bonus
     * @return true if it's a bonus
     */
    public boolean isBonus() {
        return this.name().contains("BONUS");
    }

    /**
     * May associate a bonus to this
     * @return a bonus or nothing
     */
    public Bonus associatedBonus() {
        if(this.castsShadow()|| this.isFree())
            throw new NoSuchElementException();
        return this.maybeAssociatedBonus;
    }

}
