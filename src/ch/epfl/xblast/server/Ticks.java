package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public interface Ticks {

    /**
     * the Player dying ticks
     */
    public static int PLAYER_DYING_TICKS = 8;

    /**
     * the player invulnerable ticks
     */
    public static int PLAYER_INVULNERABLE_TICKS = 64;
    /**
     * the bomb fuse ticks
     */
    public static int BOMB_FUSE_TICKS = 100;
    /**
     * the explosion ticks
     */
    public static int EXPLOSION_TICKS = 30;
    /**
     * the wall crumbling ticks
     */
    public static int WALL_CRUMBLING_TICKS = EXPLOSION_TICKS;
    /**
     * the bonus disappearing ticks
     */
    public static int BONUS_DISAPPEARING_TICKS = EXPLOSION_TICKS;
    /**
     * the ticks per second
     */
    public static int TICKS_PER_SECOND = 20;
    /**
     * the ticks per nanosecond
     */
    public static int TICK_NANOSECOND_DURATION = (Time.NS_PER_S)/TICKS_PER_SECOND;
    /**
     * the ticks total
     */
    public static int TOTAL_TICKS= 120*TICKS_PER_SECOND;

}
