package ch.epfl.xblast.server;

import ch.epfl.cs108.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.*;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */

public final class Board {
    private final List<Sq<Block>> blocks;
    private static final int EDGLESS_ROW = Cell.ROWS - 2;
    private static final int EDGLESS_COLUMNS = Cell.COLUMNS - 2;

    /**
     * Board constructor
     * 
     * @param blocks
     */
    public Board(List<Sq<Block>> blocks) {
        if (blocks.size() != Cell.COUNT)
            throw new IllegalArgumentException();

        this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));
    }

    /**
     * Method that check if the matrix has legal proportions if not it throws an
     * exception
     * 
     * @param matrix
     *            the matrix that will be checked
     * @param rows
     *            the expected rows number
     * @param columns
     *            the expected columns number
     */
    public static void checkBlockMatrix(List<List<Block>> matrix, int rows,
            int columns) {

        if (matrix == null)
            throw new NullPointerException();

        if (matrix.size() != rows)
            throw new IllegalArgumentException();

        else {
            for (int i = 0; i < matrix.size(); i++) {
                if (matrix.get(i).size() != columns)
                    throw new IllegalArgumentException();

            }
        }
    }

    /**
     * method that build a board with a list of list of block
     * 
     * @param rows
     *            A matrix containing all blocks in List<List<Block>> form
     * @return The game's Board
     */
    public static Board ofRows(List<List<Block>> rows) {

        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);

        List<Sq<Block>> listBoard = new ArrayList<Sq<Block>>();
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                listBoard.add(Sq.constant(rows.get(i).get(j)));

            }
        }
        return new Board(listBoard);

    }

    /**
     * A method that add the edges to edgless block matrixes
     * 
     * @param innerBlocks
     *            A block matrix
     * @return A Board with new edges
     */
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) {
        checkBlockMatrix(innerBlocks, (EDGLESS_ROW), (EDGLESS_COLUMNS));
        List<List<Block>> walled = new ArrayList<List<Block>>();

        walled.add(Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL));
        
        for (List<Block> l : innerBlocks) {
            List<Block>tmp = new ArrayList<Block>();
            tmp.add(Block.INDESTRUCTIBLE_WALL);
            tmp.addAll(new ArrayList<>(l));
            tmp.add(Block.INDESTRUCTIBLE_WALL);
            walled.add(tmp);
        }
       
        walled.add(Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL));

        return ofRows(walled);
    }



    /**
     * create a full board (with edge) from 1/4 edgeless board by mirroring it
     * twice
     * 
     * @param quadrantNWBlocks
     *            a List<List<Block>> Block matrix
     * @return A complete board
     */
    public static Board ofQuadrantNWBlocksWalled(
            List<List<Block>> quadrantNWBlocks) {


        checkBlockMatrix(quadrantNWBlocks, Cell.ROWS / 2,
                 Cell.COLUMNS / 2);
        List<List<Block>> NWQuadrant = new ArrayList<List<Block>>(
                quadrantNWBlocks);

        for (int i = 0; i < NWQuadrant.size(); i++)
            NWQuadrant.set(i, Lists.mirrored(NWQuadrant.get(i)));

        NWQuadrant = Lists.mirrored(NWQuadrant);

        return Board.ofInnerBlocksWalled(NWQuadrant);

    }

    /**
     * A method that get the Cell's block sequence
     * 
     * @param c
     *            the Cell which the block sequence is needed
     * @return the Cell's block sequence
     */
    public Sq<Block> blocksAt(Cell c) {

        return blocks.get(c.rowMajorIndex());
    }

    /**
     * A method that get the Cell's first block in it's block sequence
     * 
     * @param c
     *            the Cell which the block is needed
     * @return the Cell's first block in it's block sequence
     */

    public Block blockAt(Cell c) {

        return blocks.get(c.rowMajorIndex()).head();
    }

}
