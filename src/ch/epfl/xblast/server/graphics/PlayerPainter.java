package ch.epfl.xblast.server.graphics;

import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class PlayerPainter {

    private PlayerPainter() {
    }

    public static byte byteForPlayer( Player player,int tick) {
        int tempValue = 0;
        
        //define the correct character sprite group
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

        //selects one of the 2 hit sprite
        if (!player.lifeState().canMove())
            tempValue += (player.lifeState().state().equals(State.DYING)) ? ((player.lives() <= 1)?13:12)
                    : 15;

            
        else {

            //select the correct direction sprite sub-group and select the correct one
            switch (player.direction()) {
            case N:
            default:
                tempValue+=walkingAnimation(player.position().y());
                break;
            case E:

                tempValue += 3+walkingAnimation(player.position().x());
                break;
            case S:

                tempValue += 6+walkingAnimation(player.position().y());
                break;
            case W:
                tempValue += 9+walkingAnimation(player.position().x());
                break;

            }
            
            //if the character is invulnerable and the tick is odd, the character sprite will be replaced by the white character
            if(player.lifeState().state().equals(State.INVULNERABLE)&&tick%2==1){
                tempValue=(tempValue%20)+80;
            }
            
            
        }
        return (byte)tempValue;
    }
    
    /**
     * It choooses the correct walking animation
     * @param value
     *          the value that entirely determines in which walking states the player is
     * @return if value mod 4 is 0 or 2, it returns 0, if it's 1, it returns 1 and if it's 3, it returns 2
     */
    private static int walkingAnimation(int value){
        return ((value%4)%2==0)?0:(value%4==1?1:2);
    }

}
