package ch.epfl.xblast;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListsTest {

    @Test
    public void permutest1() {
        List<Integer> pommedterre = new ArrayList<Integer>();
        pommedterre.add(1);
        pommedterre.add(2);
        pommedterre.add(3);
        Lists.permutation(pommedterre);
    }
//    @Test
//    public void permutest2() {
//        List<Integer> pommedterre = new ArrayList<Integer>();
//        pommedterre.add(1);
//
//        Lists.permutation(pommedterre);
//    }
//    @Test
//    public void permutest3() {
//        List<Integer> pommedterre = new ArrayList<Integer>();
//
//        Lists.permutation(pommedterre);
//    }

}
