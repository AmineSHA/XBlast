package ch.epfl.xblast;

import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.*;
/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class Cell {

	final static public int COLUMNS = 15;
	final static public int ROWS = 13;
	final static public int COUNT = COLUMNS*ROWS;
	final private int x;
	final private int y;

	public static final List<Cell> ROW_MAJOR_ORDER = Collections
			.unmodifiableList(rowMajorOrder());

	public static final List<Cell> SPIRAL_ORDER = Collections
			.unmodifiableList(SpiralMajorOrder());

	/**
     * Construct a Cell with x and y coordinate
     * @param x
     *          the horizontal coordinate
     * @param y
     *          the vertical coordinate
     */
	public Cell(int x, int y) {

		this.x = Math.floorMod(x, COLUMNS);
		this.y = Math.floorMod(y, ROWS);
		
	}

	
	/**
     * The x coordinate getter
     * @return the horizontal coordinate x
     */
	public int x() {
		return this.x;
	}

	/**
     * The y coordinate getter
     * @return the vertical coordinate y
     */
	public int y() {
		return this.y;
	}

	/**
     * The ROW_MAJOR_ORDER coordinate getter
     * @return The ROW_MAJOR_ORDER coordinate
     */
	public int rowMajorIndex() {
		return ROW_MAJOR_ORDER.indexOf(this);

	}

	/**
	 *reorganizes Cells in row major order
	 * @returns an ArrayList of Cell organized in row major order
	 */
	
	private static ArrayList<Cell> rowMajorOrder() {

		ArrayList<Cell> tableToReturn = new ArrayList<Cell>();

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				tableToReturn.add(new Cell(j, i));
			}
		}
		return tableToReturn;

	}

	/**
     * Gives the neighbor Cell of a given cardinal direction
     * (due to the torus nature of the grid, a neighbor always exists)
     * @param dir
     *          the cardinal direction that determine which neighbor will be returned
     * @return the neighbor Cell that match the given direction
     */
	public Cell neighbor(Direction dir) {

		switch (dir) {

		case N:
		default:
			return new Cell(this.x, this.y - 1);
		case W:
			return new Cell(this.x - 1, this.y);
		case S:
			return new Cell(this.x, this.y + 1);
		case E:

			return new Cell(this.x + 1, this.y);

		}

	}
    /**
     * reorganizes Cells in spiral major order
     * @returns an ArrayList of Cell organized in spiral major order
     */
	public static ArrayList<Cell> SpiralMajorOrder() {
		ArrayList<Cell> spiralOrder = new ArrayList<Cell>();
		List<Integer> ix = new ArrayList<Integer>();
		List<Integer> iy = new ArrayList<Integer>();
		List<Integer> i1 = new ArrayList<Integer>();
		List<Integer> i2 = new ArrayList<Integer>();
		int c2;
		boolean horizontal = true;

		for (int i = 0; i < COLUMNS; i++) {
			ix.add(i);
		}
		for (int i = 0; i < ROWS; i++) {
			iy.add(i);
		}

		while (!ix.isEmpty() && !iy.isEmpty()) {
			if (horizontal) {
				i1 = ix;
				i2 = iy;
			} else {
				i1 = iy;
				i2 = ix;
			}

			c2 = i2.get(0);
			i2.remove(0);
			for (int c1 : i1) {
				if (horizontal) {
					spiralOrder.add(new Cell(c1, c2));
				} else {
					spiralOrder.add(new Cell(c2, c1));
				}
			}

			Collections.reverse(i1);
			horizontal = !horizontal;
		}
		return spiralOrder;
	}
	@Override
	public String toString() {
		return "( " + x + ", " + y + " )";
	}
	@Override
	public boolean equals(Object that) {
		return that instanceof Cell && ((Cell) that).x() == this.x()
				&& ((Cell) that).y() == this.y();
	}

	@Override 
	public int hashCode(){
		return rowMajorIndex();
	}
}