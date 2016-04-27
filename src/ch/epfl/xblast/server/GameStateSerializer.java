package ch.epfl.xblast.server;

import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.server.graphics.BoardPainter;
import ch.epfl.xblast.server.graphics.ExplosionPainter;
import ch.epfl.xblast.server.graphics.PlayerPainter;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class GameStateSerializer {

    
    
    
    private GameStateSerializer() {}
    
    public static List<Byte>serialize(BoardPainter bp, GameState gs){
        LinkedList<Byte> ByteList = new LinkedList<>();
        
        for (Cell c : Cell.SPIRAL_ORDER) 
        ByteList.add(bp.byteForCell(gs.board(), c));
        
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if(!gs.board().blockAt(c).isFree())
                ByteList.add(ExplosionPainter.BYTE_FOR_EMPTY);
            
            else if(gs.blastedCells().contains(c))
                ByteList.add(ExplosionPainter.byteForBlast(gs.blastedCells().contains(c.neighbor(Direction.N)), gs.blastedCells().contains(c.neighbor(Direction.E)), gs.blastedCells().contains(c.neighbor(Direction.S)), gs.blastedCells().contains(c.neighbor(Direction.W))));
            
            else if(gs.bombedCells().containsKey(c))
                ByteList.add(ExplosionPainter.byteForBomb(gs.bombedCells().get(c)));
            
            
            else
                ByteList.add(ExplosionPainter.BYTE_FOR_EMPTY);
            
        }
        
        for (Player p : gs.players()) {
            ByteList.add((byte) p.lives());
            ByteList.add((byte) p.position().x());
            ByteList.add((byte) p.position().y());
            ByteList.add(PlayerPainter.byteForPlayer(p, gs.ticks()));
        }
        
        ByteList.add((byte)Math.ceil(gs.remainingTime()/2));
        
        return ByteList;
        
    }
}
