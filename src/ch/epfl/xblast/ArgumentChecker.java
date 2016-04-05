package ch.epfl.xblast;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
 final public class ArgumentChecker {

    /**
     * 
     */
    private ArgumentChecker() {
    }

    /**
     * 
     * @param value
     * @return
     */
    public static int requireNonNegative(int value) {
        if (value < 0)
            throw new IllegalArgumentException();

        return value;

    }

}