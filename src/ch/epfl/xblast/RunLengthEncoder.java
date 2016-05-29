package ch.epfl.xblast;

import java.util.*;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */

public final class RunLengthEncoder {

    static private final int MAXIMUM_COUNTER = 130;

    private RunLengthEncoder() {
    }

    /**
     * compress a byte list
     * 
     * @param b
     *            the byte list that will be compressed
     * @return a compressed byte list
     */
    public static List<Byte> encode(List<Byte> b) {

        int a = 0;
        List<Byte> encodedList = new ArrayList<>();
        List<Byte> bCopy = new ArrayList<>(b);
        bCopy.add(null);
        for (int i = 0; i < b.size(); i++) {
            if (b.get(i) < 0)
                throw new IllegalArgumentException();
            a++;

            if (!(b.get(i).equals(bCopy.get(i + 1)))) {
                if (a > 2) {
                    if (a > MAXIMUM_COUNTER) {
                        encodedList.add((byte) (2 - MAXIMUM_COUNTER));
                        encodedList.add(b.get(i));

                    }
                    encodedList.add((byte) (2 - (a % MAXIMUM_COUNTER)));
                    encodedList.add(b.get(i));

                } else {

                    for (int j = 0; j < a; j++)
                        encodedList.add(b.get(i));

                }
                a = 0;
            }

        }
        return encodedList;
    }

    /**
     * uncompress a byte list
     * 
     * @param c
     *            the byte list that will be uncompressed
     * @return a uncompressed byte list
     */
    public static List<Byte> decode(List<Byte> c) {
        List<Byte> decoded = new LinkedList<>();
        for (int i = 0; i < c.size(); i++) {

            if (c.get(i) < 0) {
                if (i == c.size())
                    throw new IllegalArgumentException();

                for (int j = 0; j < (2 - c.get(i)); j++)
                    decoded.add(c.get(i + 1));

                ++i;
            }

            else
                decoded.add(c.get(i));

        }
        return decoded;
    }

}