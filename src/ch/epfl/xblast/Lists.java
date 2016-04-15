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

    /**
     * this method fill a list with lists of all possible permutations of
     * objects of a given list, for example, the list[1,2,3] will create
     * [[1,2,3],[2,1,3],[2,3,1],[1,3,2],[3,1,2],[3,2,1]]
     * 
     * @param l
     *            the list that will be used as basis for permutations
     * @return the list of lists of all possible permutation
     */
    public static <T> List<List<T>> permutations(List<T> l) {
        List<List<T>> end = new ArrayList<List<T>>();
        switch (l.size()) {
        case 0:
            end.add(Collections.emptyList());
            break;
        case 1:
            end.add(new ArrayList<T>(l));
            break;
        default:
            List<List<T>> ll = permutations(l.subList(1, l.size()));
            for (List<T> k : ll) {

                for (int i = 0; i < k.size() + 1; i++) {
                    List<T> temp1 = new ArrayList<>(k);
                    temp1.add(i, l.get(0));
                    end.add(temp1);

                }
            }
        }

        return Collections.unmodifiableList(new ArrayList<List<T>>(end));
    }

}
