package ch.epfl.xblast.server;

import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;
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
    
    /**
     * Create the informations that will be send to clients
     * @param bp
     *          A boardPainter
     * @param gs
     *          the game state that will be sent
     * @return a byte list of the game state with the board painter applied and compressed
     */
    public static List<Byte>serialize(BoardPainter bp, GameState gs){
        
        List<Byte> ByteList = new LinkedList<>();
        List<Byte> temp = new LinkedList<>();
        
        for (Cell c : Cell.SPIRAL_ORDER) 
        temp.add(bp.byteForCell(gs.board(), c));

        ByteList.addAll(encoder(temp));

        temp.clear();

        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if(!gs.board().blockAt(c).isFree())
                temp.add(ExplosionPainter.BYTE_FOR_EMPTY);
            
            else if(gs.blastedCells().contains(c))
                temp.add(ExplosionPainter.byteForBlast(gs.blastedCells().contains(c.neighbor(Direction.N)), gs.blastedCells().contains(c.neighbor(Direction.E)), gs.blastedCells().contains(c.neighbor(Direction.S)), gs.blastedCells().contains(c.neighbor(Direction.W))));
            
            else if(gs.bombedCells().containsKey(c))
                temp.add(ExplosionPainter.byteForBomb(gs.bombedCells().get(c)));
            
            
            else
                temp.add(ExplosionPainter.BYTE_FOR_EMPTY);
            
        }
        
        ByteList.addAll(encoder(temp));

        temp.clear();
        
        for (Player p : gs.players()) {
            ByteList.add((byte) p.lives());
            ByteList.add((byte) p.position().x());
            ByteList.add((byte) p.position().y());
            ByteList.add(PlayerPainter.byteForPlayer(p, gs.ticks()));
        }
        
        ByteList.add((byte)Math.ceil(gs.remainingTime()/2));
        
//        return RunLengthEncoder.decode(ByteList);
        return ByteList;
        
    }
    
    /**
     * encode a byte list and put its size at index 0
     * @param temp
     *          the list to encode
     * @return an encoded list with it's size at index 0
     */
    private static List<Byte> encoder(List<Byte> temp){

        temp=RunLengthEncoder.encode(temp);
        temp.add(0, (byte)temp.size());
        return temp;
    }
}
