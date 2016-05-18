package ch.epfl.xblast;

import java.util.Objects;

import org.hamcrest.core.IsNull;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public enum PlayerAction {

    JOIN_GAME,
    MOVE_N,
    MOVE_E,
    MOVE_S,
    MOVE_W,
    STOP,
    DROP_BOMB;
    
    public boolean isMovementInput(){
        return (this.equals(MOVE_N)||this.equals(MOVE_E)||this.equals(MOVE_S)||this.equals(MOVE_W)||this.equals(STOP));
    }
    
    public Direction equivalentDirection(){
        
        if(!isMovementInput())
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
