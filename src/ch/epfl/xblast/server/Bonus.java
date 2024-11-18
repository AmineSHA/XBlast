package ch.epfl.xblast.server;



/**
 * 
 * @author Alban Favre (260025) / Amine Chaouachi (260709)
 *
 */
public enum Bonus {

    /**
     * Bomb incrementor
     */
    INC_BOMB {
        public Player applyTo(Player player) {

            if (player.maxBombs() < BONUS_MAX)
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs() + 1,
                        player.bombRange());
            else
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs(),
                        player.bombRange());

        }

    },
    /**
     * range incrementor
     */
    INC_RANGE {
        public Player applyTo(Player player) {
            if (player.bombRange() < BONUS_MAX) {
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs(),
                        player.bombRange() + 1);
            } else {
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs(),
                        player.bombRange());
            }
        }
    };
    static private final int BONUS_MAX = 9;

    /**
     * @param player
     * @return the player with the applied bonus
     */
    abstract public Player applyTo(Player player);

}
