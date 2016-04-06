package ch.epfl.xblast;

/**
 * 
 * @author Alban Favre (260025) / Amine Chaouachi (260709)
 *
 */
public enum Direction {

    /**
     * North
     */
    N, 
    /**
     * East
     */
    E, 
    /**
     * South
     */
    S, 
    /**
     * West
     */
    W;

    /**
     * find the opposite direction of this
     * 
     * @return the opposite direction
     */
    public Direction opposite() {
        switch (this) {
        case N:
        default:
            return S;

        case E:
            return W;

        case S:
            return N;

        case W:
            return E;
        }
    }

    /**
     * Checks if the direction is E or W
     * @return true if it is horizontal
     */
    public boolean isHorizontal() {
        return this == E || this == W;
    }

    /**
     * Checks if this is parallel to the parameter
     * @param that
     *       The direction which will be compared to this
     * @return true if it is parallel to that
     */
    public boolean isParallelTo(Direction that) {
        return this == that || this == that.opposite();
    }

}