package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.server.*;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */


public final class ExplosionPainter {
    
    public static byte BYTE_FOR_EMPTY = (byte)16;
    
    private ExplosionPainter(){}
    
    public static byte byteForBomb(Bomb bomb){
     
        return  (Integer.bitCount(bomb.fuseLength())== 1)?(byte)21:(byte)20;
    }
    
    static public byte byteForBlast(boolean N, boolean E, boolean S, boolean W){
     return (byte) (
             (N ?(1<<3):0)|
             (E ?(1<<2):0)|
             (S ?(1<<1):0)|
             (W ?(1<<0):0))
             ;
    }
    
    
    
}