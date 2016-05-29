package ch.epfl.xblast.client;
import java.awt.Image;

import java.util.*;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class GameState {

    
private final List<Player> players;
private final List<Image> boardImages;
private final List<Image> bombsAndExplosionsImages;
private final List<Image> scoreLine;
private final List<Image> timeLine;

/**
 * Client gameState constructor
 * @param players
 * @param boardImages
 * @param bombsAndExplosionsImages
 *          can contain null
 * @param scoreLine
 * @param timeLine
 */
public GameState(List<Player> players, List<Image> boardImages,
        List<Image> bombsAndExplosionsImages, List<Image> scoreLine,
        List<Image> timeLine) {

    this.players=new ArrayList<>(Objects.requireNonNull(players));
    this.boardImages=new ArrayList<>(Objects.requireNonNull(boardImages));
    this.bombsAndExplosionsImages=new ArrayList<>(bombsAndExplosionsImages);
    this.scoreLine=new ArrayList<>(Objects.requireNonNull(scoreLine));
    this.timeLine=new ArrayList<>(Objects.requireNonNull(timeLine));
}


/**
 * bombs And Explosions Image getter
 * @return the bombs And Explosions Image list
 */
public List<Image> bombsAndExplosionsImage(){
    return bombsAndExplosionsImages;
}


/**
 * the board image getter
 * @return the board image list
 */
public List<Image> boardImages(){
    return boardImages;
}

/**
 * the score line getter
 * @return the score line image list
 */
public List<Image> scoreLine(){
    return scoreLine;
}

/**
 * the time line getter
 * @return the time line image list
 */
public List<Image>  timeLine(){
    return timeLine;
}
/**
 * the players getter
 * @return the player list
 */
public List<Player> players(){
    return players;
}





    
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public static final class Player{
    private final PlayerID id;
    private final int lives;
    private final SubCell position;
    private final Image image;
    
    /**
     * client player constructor
     * @param id
     *          the player's id
     * @param lives
     *          the player's lives total
     * @param position
     *          the player's subcell
     * @param image
     *          the player's image
     */
    public Player(PlayerID id, int lives, SubCell position, Image image){
        this.id=id;
        this.lives=lives;
        this.position=position;
        this.image=image;
        
    }
    /**
     * the PlayerID getter
     * @return the PlayerID
     */
    public PlayerID  id(){
        return id;
    }
    /**
     * the lives getter
     * @return the live total
     */
    public int lives(){
        return lives;
    }
    /**
     * the position getter
     * @return the subCell position
     */
    public  SubCell position(){
        return position;
    }
    /**
     * the image getter
     * @return the image
     */
    public Image image(){
        return image;
    }
    
}
}