public class NumberSquare extends Square {
    int neighborMines = 0;
    int myRow = 0;
    int myCol = 0;

    /**
     * Creates a NumberSquare with a number of neighboring mines and a location.
     * 
     * @param neighborMines Number of mines neighboring the square.
     * @param myRow         Row the square is on.
     * @param myCol         Column the square is on.
     */
    NumberSquare(int neighborMines, int myRow, int myCol) {
        this.neighborMines = neighborMines;
        this.myRow = myRow;
        this.myCol = myCol;
    }

    /**
     * Returns whether or not it's a mine.
     * 
     * @return boolean if it's a mine or not.
     */
    public boolean isMine() {
        return false;
    }

    /**
     * Returns the number of mines neighboring the square.
     * 
     * @return int number of neighboring mines.
     */
    public int getNeighborMines() {
        return neighborMines;
    }

    /**
     * Uncovers the square
     * 
     * @return boolean Probably meant to return whether the uncovery was successful,
     *         but the extra credit goes a different way.
     */
    @Override
    public boolean uncover() {

        // Only reveal if it isn't flagged.
        if (!isFlagged()) {
            if (neighborMines > 0)
                setElement(String.format("%d", neighborMines));
            else
                setElement("_");
            setUncovered();
            return true;
        } else
            // Didn't really know what error to throw, but this one works fine.
            throw new IllegalArgumentException("Cannot uncover a flagged piece.");
    }

}
