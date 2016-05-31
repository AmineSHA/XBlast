package ch.epfl.xblast;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public enum PlayerAction {

    /**
     * join game
     */
    JOIN_GAME,
    /**
     * move up
     */
    MOVE_N,
    /**
     * move right
     */
    MOVE_E,
    /**
     * move down
     */
    MOVE_S,
    /**
     * move left
     */
    MOVE_W,
    /**
     * stop movement
     */
    STOP,
    /**
     * drop bomb
     */
    DROP_BOMB;

    /**
     * verify if it is a movement input (NESW or STOP)
     * 
     * @return true if it is
     */
    public boolean isMovementInput() {
        return (this.equals(MOVE_N) || this.equals(MOVE_E)
                || this.equals(MOVE_S) || this.equals(MOVE_W)
                || this.equals(STOP));
    }

    /**
     * select the corresponding direction from a player input (in player action
     * form) (if the player makes a STOP input, the corresponding direction is
     * none(null))
     * 
     * @return the corresponding direction
     */
    public Direction equivalentDirection() {

        if (!isMovementInput())
            throw new IllegalArgumentException();

        switch (this) {
        case MOVE_N:

            return Direction.N;

        case MOVE_S:
            return Direction.S;

        case MOVE_E:
            return Direction.E;

        case MOVE_W:
            return Direction.W;

        case STOP:
        default:
            return null;

        }
    }

}
