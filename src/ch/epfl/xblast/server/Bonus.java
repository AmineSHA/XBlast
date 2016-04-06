package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player;

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

            if (player.maxBombs() < 9) {
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs() + 1,
                        player.bombRange());
            } else {
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs(),
                        player.bombRange());
            }
        }

    },
    /**
     * range incrementor
     */
    INC_RANGE {
        public Player applyTo(Player player) {
            if (player.bombRange() < 9) {
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs(),
                        player.bombRange()+1);
            } else {
                return new Player(player.id(), player.lifeStates(),
                        player.directedPositions(), player.maxBombs(),
                        player.bombRange());
            }
        }
    };
    /**
     * @param player
     * @return the player with the applied bonus
     */
    abstract public Player applyTo(Player player);
}
