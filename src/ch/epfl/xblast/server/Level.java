package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.graphics.BlockImage;
import ch.epfl.xblast.server.graphics.BoardPainter;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class Level {

    private final BoardPainter bp;
    private final GameState gs;

    
    /**
     * default level
     */
    public final static Level DEFAULT_LEVEL=defaultLevel();

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

    private static Level defaultLevel() {
        Map<Block, BlockImage> palette = new HashMap<>();
        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        palette.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        palette.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        palette.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        palette.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        palette.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);
        
        
        
        Block XX=Block.INDESTRUCTIBLE_WALL;
        Block cc=Block.DESTRUCTIBLE_WALL;
        Block __=Block.FREE;
        Board b=Board.ofQuadrantNWBlocksWalled(Arrays.asList(

               Arrays.asList(__,__,__,__,__,cc,__),
               Arrays.asList(__,XX,cc,XX,cc,XX,cc),
               Arrays.asList(__,cc,__,__,__,cc,__),
               Arrays.asList(cc,XX,__,XX,XX,XX,XX),
               Arrays.asList(__,cc,__,cc,__,__,__),
               Arrays.asList(cc,XX,cc,XX,cc,XX,__)
               ));
        
        
        
        List<Player> p=new ArrayList<Player>();
        p.add(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3));
        p.add(new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3));
        p.add(new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 2, 3));
        p.add(new Player(PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3));
        
        return new Level(new BoardPainter(palette, BlockImage.IRON_FLOOR_S), new GameState(b, p));

        
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
