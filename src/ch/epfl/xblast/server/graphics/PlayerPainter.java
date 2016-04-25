package ch.epfl.xblast.server.graphics;


import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class PlayerPainter {
    
    /**
     * the player multiplier is the integer value that mark the jump between two players sprites group
     * for example the absolute value of the difference of player x and player y (abs(playerX-playerY))
     * will always be PLAYER_MULTIPLIER*n where n is a positive integer.
     */
    private static final int PLAYER_MULTIPLIER=20;
    private static final int PLAYER_WHITE=80;
    private static final int DEAD_SPRITE=13;
    private static final int DYING_SPRITE=12;
    private static final int INVALID_SPRITE=15;
    private static final int NORTHFACING_SPRITE=0;
    private static final int EASTFACING_SPRITE=3;
    private static final int SOUTHTHFACING_SPRITE=6;
    private static final int WESTFACING_SPRITE=9;
    private static final int FIRST_WALKING_SPRITE=0;  
    private static final int SECOND_WALKING_SPRITE=1;
    private static final int THIRD_WALKING_SPRITE=2;

    private PlayerPainter() {
    }

    /**
     * Define which sprite use for the player in the current tick
     * @param player
     *          the player that need an sprite
     * @param tick
     *          the current tick
     * @return the byte that correspond to the correct sprite
     */

    public static byte byteForPlayer( Player player,int tick) {
        
        
        //define the correct character sprite group
        int tempValue=player.id().ordinal()*PLAYER_MULTIPLIER;

        /*selects one of the 2 hit sprite
         * if the player is already dead, he won't have any sprite, making him invisible(it's the INVALID_SPRITE)
         */
        if (!player.lifeState().canMove())
            tempValue += (player.lifeState().state().equals(State.DYING)) ? ((player.lives() <= 1)?DEAD_SPRITE:DYING_SPRITE)
                    : INVALID_SPRITE;

            
        else {

            //select the correct direction sprite sub-group and select the correct one
            switch (player.direction()) {
            case N:
            default:
                tempValue+=NORTHFACING_SPRITE+walkingAnimation(player.position().y());
                break;
            case E:

                tempValue += EASTFACING_SPRITE+walkingAnimation(player.position().x());
                break;
            case S:

                tempValue += SOUTHTHFACING_SPRITE+walkingAnimation(player.position().y());
                break;
            case W:
                tempValue += WESTFACING_SPRITE+walkingAnimation(player.position().x());
                break;

            }
            
            //if the character is invulnerable and the tick is odd, the character sprite will be replaced by the white character
            if(player.lifeState().state().equals(State.INVULNERABLE)&&tick%2==1){
                tempValue=(tempValue%PLAYER_MULTIPLIER)+PLAYER_WHITE;
            }
            
            
        }
        return (byte)tempValue;
    }
    
    /**
     * It chooses the correct walking animation
     * @param value
     *          the value that entirely determines in which walking states the player is
     * @return if value mod 4 is 0 or 2, it returns FIRST_WALKING_SPRITE, if it's 1, it returns SECOND_WALKING_SPRITE and if it's 3, it returns THIRD_WALKING_SPRITE
     */
    private static int walkingAnimation(int value){
        return ((value%4)%2==0)?FIRST_WALKING_SPRITE:(value%4==1?SECOND_WALKING_SPRITE:THIRD_WALKING_SPRITE);
    }

}
