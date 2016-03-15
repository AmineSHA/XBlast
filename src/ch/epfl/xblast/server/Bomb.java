package ch.epfl.xblast.server;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Direction;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public class Bomb {

    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;


    /**
     * The out of game bomb constructor
     * @param ownerId
     *          the bomb's ownerId
     * @param position
     *          the bomb's cell's position
     * @param fuseLengths
     *          the bomb's fuse length integer sequence
     * @param range
     *          How far the bomb can burst
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) {

        if (ownerId == null || position == null || fuseLengths == null) {
            throw new NullPointerException();
        }

        this.ownerId = ownerId;
        this.position = position;
        this.fuseLengths = fuseLengths;
        this.range = range;

    }

    
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) {

        this(ownerId, position, Sq.iterate(fuseLength, u -> u - 1), range);

    }

    /**
     * The playerID getter
     * @return the playerID
     */
    public PlayerID ownerId() {
        return ownerId;
    }

    /**
     * The position getter
     * @return the position
     */
    public Cell position() {
        return position;
    }

    /**
     * The range getter
     * @return the range
     */
    public int range() {
        return range;
    }
    
    /**
     * The fuseLengths sequence getter
     * @return the fuseLengths sequence
     */
    public Sq<Integer> fuseLengths(){
        return fuseLengths;
    }
    /**
     * The fuseLength getter
     * @return the fuseLength
     */
    public int fuseLength(){
        return fuseLengths.head();
    }

    /**
     * Takes care of the explosion, creating 4 arms of <Sq<Sq<Cell>> representing the exploding bomb
     * @return An unmodifiable cell sequence sequence
     */
    public List<Sq<Sq<Cell>>> explosion() {
        List<Sq<Sq<Cell>>> explosion = new ArrayList<>();
       for(Direction d : Direction.values()){
           explosion.add(explosionArmTowards(d));
       }
       
       return Collections.unmodifiableList(explosion);
        
    }

    /**
     * Takes care of an explosion arm, making it exist as long as necessary and progress in the intended direction
     * @param Dir
     *      the direction where the arm is going
     * @return the explosion arm in Sq<Sq<Cell>> form
     */
    private Sq<Sq<Cell>> explosionArmTowards(Direction Dir) {

        return Sq.repeat(Ticks.EXPLOSION_TICKS,Sq.iterate(new Cell(position.x(), position.y()),c -> c.neighbor(Dir)));

    }

}

