package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {

    private PlayerPainter() {
    }

    public static byte byteForPlayer(int tick, Player player) {
        int tempValue = 0;
        
        switch (player.id().ordinal()) {
        case 0:
        default:
            break;
        case 1:
            tempValue += 20;
            break;
        case 2:
            tempValue += 40;
            break;
        case 3:
            tempValue += 60;
            break;
        }

        if (!player.lifeState().canMove())
            tempValue += (player.lifeState().state().equals(State.DEAD)) ? 13
                    : 12;

        else {
            int sprite=0;
            switch (player.direction()) {
            case N:
            default:
                sprite=player.position().y()%4;
                break;
            case E:
                sprite=player.position().x()%4;
                tempValue += 3;
                break;
            case S:
                sprite=player.position().y()%4;
                tempValue += 6;
                break;
            case W:
                sprite=player.position().x()%4;
                tempValue += 9;
                break;

            }
            
            if(){
                
            }
            
            
        }
        return 0;
    }

}
