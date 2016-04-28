package ch.epfl.xblast.server;

import java.util.Objects;

import ch.epfl.xblast.server.graphics.BoardPainter;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class Level {

    private final BoardPainter bp;
    private final GameState gs;
    //TODO
    final static Level DEFAULT_LEVEL=null;

    /**
     * Construct a Level with a BoardPainter and a GameState
     * 
     * @param bp
     *            a BoardPainter
     * @param gs
     *            a GameState
     */
    public Level(BoardPainter bp, GameState gs) {
        this.bp = Objects.requireNonNull(bp);
        this.gs = Objects.requireNonNull(gs);
    }

    /**
     * boardPainter getter
     * @return the boardPainter
     */
    public BoardPainter boardPainter() {
        return this.bp;
    }

    /**
     * gameState getter
     * @return the gameState
     */
    public GameState gameState() {
        return this.gs;
    }

}
