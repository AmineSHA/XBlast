package ch.epfl.xblast;

import java.util.Collections;

import java.util.List;
import java.util.ArrayList;
import ch.epfl.xblast.server.Block;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class Lists {

    private Lists() {
    }

    /**
     * This method extends list like this: the list 123 become 12321
     * 
     * @param l
     *            list that will be extended
     * @return a symetrical list
     */
    public static <T> List<T> mirrored(List<T> l) {
        if (l.isEmpty())
            throw new IllegalArgumentException();
        if (l.size() == 1)
            return l;
        List<T> tableToChange = new ArrayList<T>(l);
        List<T> basicTable = new ArrayList<T>(l);
        Collections.reverse(tableToChange);
        tableToChange.remove(0);
        basicTable.addAll(tableToChange);
        return basicTable;
    }

    public static <T> List<List<T>> permutation(List<T> l) {
        List<List<T>> end = new ArrayList<List<T>>();
        switch (l.size()) {
        case 0:
            end.add(Collections.emptyList());
            break;
        case 1:
            end.add(new ArrayList<T>(l));
            break;
        default:
            List<List<T>> ll = permutation(l.subList(1, l.size()));
            for (List<T> k : ll) {

                for (int i = 0; i < k.size() + 1; i++) {
                    List<T> temp1 = new ArrayList<>(k);
                    temp1.add(i, l.get(0));
                    end.add(temp1);

                }
            }
        }
        System.out.println(end);
        return Collections.unmodifiableList(end);
    }

}
