package ch.epfl.xblast;

/**
 * 
 * @author Alban Favre (260025) / Amine Chaouachi (260709)
 *
 */
public enum Direction {

    N, E, S, W;

    /**
     * find the opposite direction
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
     * 
     * @return true if it is horizontal
     */
    public boolean isHorizontal() {
        return this == E || this == W;
    }

    /**
     *
     * @return true if it is parallel
     */
    public boolean isParallelTo(Direction that) {
        return this == that || this == that.opposite();
    }

}