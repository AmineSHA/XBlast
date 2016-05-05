package ch.epfl.xblast.client;
import java.awt.Image;
import java.util.*;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
public class GameState {

    
private final List<Player> players;
private final List<Image> boardImages;
private final List<Image> bombsAndExplosionsImages;
private final List<Image> scoreLine;
private final List<Image> timeLine;

    
    
public static final class Player{
    private final PlayerID id;
    private final int lives;
    private final SubCell position;
    private final Image image;
    
    public Player(PlayerID id, int lives, SubCell position, Image image){
        this.id=id;
        this.lives=lives;
        this.position=position;
        this.image=image;
        
    }
    
    @Override
    public String toString() {
        return id+" "+lives+" "+position+" ("+image.getWidth(null)+" "+image.getHeight(null)+")";
    }
    
    
}
}