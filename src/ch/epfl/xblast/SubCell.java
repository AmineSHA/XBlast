package ch.epfl.xblast;

/**
 * 
 * @author Amine Chaouachi (260709) / Alban Favre (260025)
 *
 */
public final class SubCell {

    private final int x;
    private final int y;
    /**
     * columns number
     */
    final static public int COLUMNS = 240;
    /**
     * rows number
     */
    final static public int ROWS = 208;
    /**
     * SIZE*SIZE=number of SubCells in a Cell
     */
    final static public int SIZE = 16;

    /**
     * Construct a SubCell with x and y coordinate
     * 
     * @param x
     *            the horizontal coordinate
     * @param y
     *            the vertical coordinate
     */
    public SubCell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Gives the central SubCell of a given Cell
     * 
     * @param cell
     *            A Cell
     * @return The central SubCell of a given Cell if there is no center in the
     *         SubCell, it will return the center nearest SubCell
     */
    public static SubCell centralSubCellOf(Cell cell) {
        int gapx = (int) Math.ceil((SIZE) / 2);
        int gapy = (int) Math.ceil((SIZE) / 2);
        return new SubCell(cell.x() * SIZE + gapx, cell.y() * SIZE + gapy);

    }

    /**
     * The x coordinate getter
     * 
     * @return the horizontal coordinate x
     */
    public int x() {
        return x;
    }

    /**
     * The y coordinate getter
     * 
     * @return the vertical coordinate y
     */
    public int y() {
        return y;
    }

    /**
     * Calculate the Manathan distance between this SubCell and the central
     * SubCell
     * 
     * @return the number of SubCell which have to be crossed to reach the
     *         central SubCell
     */
    public int distanceToCentral() {
        int absoluteX = Math.abs(
                this.x() - SubCell.centralSubCellOf(this.containingCell()).x());
        int absoluteY = Math.abs(
                this.y() - SubCell.centralSubCellOf(this.containingCell()).y());
        return absoluteX + absoluteY;
    }

    /**
     * Determine if the SubCell is the central SubCell
     * 
     * @return <b>true</b> if the SubCell is the central SubCell, <b>false</b>
     *         otherwise.
     */
    public boolean isCentral() {
        return (this.distanceToCentral() == 0);

    }

    /**
     * Gives the neighbor SubCell of a given cardinal direction (due to the
     * torus nature of the grid, a neighbor always exists)
     * 
     * @param d
     *            the cardinal direction that determine which neighbor will be
     *            returned
     * @return the neighbor SubCell that match the given direction
     */
    public SubCell neighbor(Direction d) {

        switch (d) {
        case N:
        default:
            return new SubCell(this.x, this.y - 1);
        case W:
            return new SubCell(this.x - 1, this.y);
        case S:
            return new SubCell(this.x, this.y + 1);
        case E:
            return new SubCell(this.x + 1, this.y);

        }
    }

    /**
     * Gives the Cell that contains the SubCell
     * 
     * @return The Cell containing the SubCell
     */
    public Cell containingCell() {
        int xvalue = (int) Math.floor((this.x) / SIZE);
        int yvalue = (int) Math.floor((this.y) / SIZE);
        return new Cell(xvalue, yvalue);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof SubCell && ((SubCell) that).x() == this.x()
                && ((SubCell) that).y() == this.y();
    }

    @Override
    public String toString() {

        return "( " + x + ", " + y + " )";
    }

    @Override
    public int hashCode() {

        return (Math.floorMod(this.x, COLUMNS)) + COLUMNS * y();
    }

}
