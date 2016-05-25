package ch.epfl.xblast;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public interface Time {

    /**
     * Seconds per minute
     */
    public static int S_PER_MIN = 60;
    /**
     * Milliseconds per second
     */
    public static int MS_PER_S = 1000;
    /**
     * Microseconds per second
     */
    public static int US_PER_S = MS_PER_S*1000;
    /**
     * nanoseconds per second
     */
    public static int NS_PER_S = US_PER_S*1000;

}
