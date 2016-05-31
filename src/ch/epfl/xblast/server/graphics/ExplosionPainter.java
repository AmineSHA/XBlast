package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.server.*;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */

public final class ExplosionPainter {

    /**
     * The byte for when it's empty
     */
    public static final byte BYTE_FOR_EMPTY = (byte) 16;
    private static final byte BOMB_SPRITE = (byte) 20;
    private static final byte WHITE_BOMB_SPRITE = (byte) 21;

    private ExplosionPainter() {
    }

    /**
     * Define which bomb sprite is needed
     * 
     * @param bomb
     *            the bomb that needs a sprite
     * @return the byte that correspond to the correct sprite
     */
    public static byte byteForBomb(Bomb bomb) {

        return (Integer.bitCount(bomb.fuseLength()) == 1) ? WHITE_BOMB_SPRITE
                : BOMB_SPRITE;
    }

    /**
     * Chooses the corresponding explosion sprite
     * 
     * @param N
     *            north connection boolean
     * @param E
     *            east connection boolean
     * @param S
     *            south connection boolean
     * @param W
     *            west connection boolean
     * @return the byte that correspond to the correct sprite
     */
    static public byte byteForBlast(boolean N, boolean E, boolean S,
            boolean W) {
        return (byte) ((N ? (1 << 3) : 0) | (E ? (1 << 2) : 0)
                | (S ? (1 << 1) : 0) | (W ? (1 << 0) : 0));
    }

}