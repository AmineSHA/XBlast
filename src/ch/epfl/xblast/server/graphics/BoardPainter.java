package ch.epfl.xblast.server.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class BoardPainter {
    
    private final byte shadowBlock;
    private final Map<Block, Byte> graphics;

    /**
     * BoardPainter constructor
     * @param palette
     *      A map of blocks and their corresponding image
     * @param shadowBlock 
     *      the image for a free block with shadow
     */
    public BoardPainter(Map<Block, BlockImage> palette, BlockImage shadowBlock){
        if(palette.isEmpty()||palette.size()!=BlockImage.values().length-1)
            throw new IllegalArgumentException();
        
       
        Map<Block, Byte> temp=new HashMap<>();
        for (Block b : palette.keySet()) {
            temp.put(Objects.requireNonNull(b), (byte) Objects.requireNonNull(palette).get(b).ordinal());
        }
        this.graphics=new HashMap<>(temp);
        this.shadowBlock=(byte)shadowBlock.ordinal();
    }

    
    /**
     * Find the corresponding image for a cell (in byte form)
     * @param board 
     *          The board
     * @param cell 
     *          the cell which image is needed
     * @return the byte corresponding to the block on the cell
     */
    public byte byteForCell(Board board, Cell cell){
        
            if(board.blockAt(cell).isFree()&&board.blockAt(cell.neighbor(Direction.W)).castsShadow())
                return shadowBlock;
            
            return graphics.get(board.blockAt(cell));

    }
    
}
