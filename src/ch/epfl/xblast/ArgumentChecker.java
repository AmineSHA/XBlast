package ch.epfl.xblast;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
 final public class ArgumentChecker {

    private ArgumentChecker() {
    }

    /**
     * verify that the value is positive, if not, well, you get an error
     * @param value
     *          the value that will be checked
     * @return the value that was tested
     */
    public static int requireNonNegative(int value) {
        if (value < 0)
            throw new IllegalArgumentException();

        return value;

    }

}