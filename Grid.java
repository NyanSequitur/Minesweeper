import java.util.Random;

public class Grid {
    private int width, height, numMines, numSquaresUncovered;

    private Square[][] grid;

    /**
     * Constructor to generate the board
     * 
     * @param width    of the board board.
     * @param height   of the board.
     * @param numMines contained in the board.
     */
    Grid(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        grid = new Square[width][height];
        this.numMines = numMines;

        Random random = new Random();
        // My method of generating a board... differs slightly... from the way that it
        // was explained in class, but I had already programmed it by that point.
        // Start by creating an array of ints equal to the number of squares on the
        // board.
        int possibleLocations[] = new int[width * height];
        // Create another array with a length equal to the number of mines to be placed.
        int mineLocations[] = new int[numMines];
        int mineCount = 0;

        int currentSquare = 0;
        boolean notMineYet = true;

        // Fill the possible locations array of integers in ascending order.
        for (int i = 0; i < possibleLocations.length; i++) {
            possibleLocations[i] = i;
        }

        // Swap every number in possiblelocations with another random position,
        // scrambling it.
        for (int i = 0; i < possibleLocations.length; i++) {
            int indexToSwap = random.nextInt(possibleLocations.length);
            int temp = possibleLocations[indexToSwap];
            possibleLocations[indexToSwap] = possibleLocations[i];
            possibleLocations[i] = temp;
        }

        // Fill an array of the number of mines with the first number-of-mines values in
        // the list.
        for (int i = 0; i < mineLocations.length; i++) {
            mineLocations[i] = possibleLocations[i];
        }

        for (int row = 0; row < width; row++) {
            for (int column = 0; column < height; column++) {

                for (int mine : mineLocations) {
                    // I was having a lot of trouble where boards were being generated like so:
                    // 00000
                    // 00011
                    // 1001x
                    // 10011
                    // 10000
                    //
                    // quite literally an edge-case. An extra check had to be added to ensure that
                    // the mine was actually on the correct line.
                    // The mine detection would've been easier if I was allowed to have a setValue()
                    // method on number squares, but that's just how it is.
                    //
                    //

                    // If the mine location is between the counter -1 and the counter +1, and it's
                    // on the same row as the mine square, then it's a mine, increment the counter.
                    if (((currentSquare - 1 <= mine) && (mine <= currentSquare + 1) && row * height <= mine
                            && mine < ((row + 1) * height))

                            // If the mine location is between the counter -1 and the counter +1, minus the
                            // height value (to move up a row), and it's in the correct row, the it's a
                            // mine.
                            || ((currentSquare - height - 1 <= mine) && (mine <= currentSquare - height + 1)
                                    && (row - 1) * height <= mine && mine < (row * height))

                            // If the mine location is between the counter -1 and the counter +1, plus the
                            // height value (to move down a row), and it's in the correct row, the it's a
                            // mine.
                            || ((currentSquare + height - 1 <= mine) && (mine <= currentSquare + height + 1)
                                    && mine < (row + 2) * height && mine >= (row + 1) * height)

                    ) {
                        mineCount++;
                    }

                    // During board generation, for every square, increment a counter, and
                    // iterate over the number of mines list. If one of the numbers in the list of
                    // mines matches the current increment, that square is a mine. Also set
                    // notMineYet to be false, so that once it's a mine, it doesn't get replaced
                    // with a numbersquare because the counter doesn't match all the mines.
                    if (currentSquare == mine && notMineYet) {
                        grid[row][column] = new MineSquare();
                        notMineYet = false;
                    } else if (notMineYet) {
                        // Else, create a number square w/ the mine count created above.
                        grid[row][column] = new NumberSquare(mineCount, row, column);
                    }
                }
                // Reset values and increment the counter for the next square
                mineCount = 0;
                notMineYet = true;
                currentSquare++;
            }

        }
    }

    /**
     * Gets the number of squares next to a given tile.
     * 
     * @param row of the square being targeted.
     * @param col of the square being targeted.
     * @return int Number of mines.
     */
    public int getNeighbors(int row, int col) {
        NumberSquare targetSquare;
        targetSquare = (NumberSquare) grid[row][col];
        return targetSquare.getNeighborMines();
    }

    /**
     * Recursively reveals squares until hitting a square w/ at least one mine
     * neighbor.
     * 
     * @param row to start revealing at.
     * @param col to start revealing at.
     * @return Status Results of the reveal.
     */
    public Status uncoverSquare(int row, int col) {

        // Keep reveals on the board.
        if (row < 0 || row > width - 1 || col < 0 || col > height - 1)
            return Status.OK;

        // Return MINE if the square being selected is a mine.
        if (grid[row][col].isMine())
            return Status.MINE;

        // If the square being revealed has any neighbors, only uncover it.
        if (getNeighbors(row, col) != 0) {

            // Can't uncover a flagged square.
            try {
                grid[row][col].uncover();
                numSquaresUncovered++;
            } catch (IllegalArgumentException e) {
                return Status.OK;
            }
        }

        // if the square hasn't been uncovered already, and doesn't have any neighbors,
        // recurse.
        else if (!grid[row][col].isUncovered()) {

            // Can't uncover a flagged square.
            try {
                grid[row][col].uncover();
                numSquaresUncovered++;
            } catch (IllegalArgumentException e) {
                return Status.OK;
            }

            // Recurse in 4 directions.
            uncoverSquare(row - 1, col);
            uncoverSquare(row + 1, col);
            uncoverSquare(row, col - 1);
            uncoverSquare(row, col + 1);
        }

        // Check if the game has been won.
        if (numSquaresUncovered >= ((width * height) - numMines))
            return Status.WIN;
        return Status.OK;
    }

    /**
     * Reveals all mines on the board.
     * 
     */
    public void exposeMines() {
        // Iterate over all squares. If it's a mine, set the element to *.
        for (Square[] row : grid) {
            for (Square square : row) {
                if (square.isMine())
                    square.setElement("*");
            }
        }
    }

    /**
     * Puts a flag on a square.
     * 
     * @param row to put a flag on.
     * @param col to put a flag on.
     */
    public void flagSquare(int row, int col) {
        // I don't like this, but having a 'previous state' String in the square was not
        // in the UML diagram, so restoration's more work.

        // If it's being unflagged, figure out what the unflagged state of the square
        // *should* be.
        if (row >= 0 && row < width && col >= 0 && col < height) {

            if (grid[row][col].isFlagged()) {
                if (!grid[row][col].isUncovered())
                    grid[row][col].setElement("x");
                else if (getNeighbors(row, col) == 0)
                    grid[row][col].setElement("_");
                else
                    grid[row][col].setElement(String.format("%d", getNeighbors(row, col)));
            } else
                grid[row][col].setElement("F");
            // flagSquare() toggles on/off.
            grid[row][col].flagSquare();
        }
    }

    /**
     * Converts the grid to a printable string.
     * 
     * @return String grid in a viewable format.
     */
    @Override
    public String toString() {
        // Starts off w/ some spacing
        String output = "    ";

        // Creates top row
        for (int i = 0; i < grid[0].length; i++) {
            output += String.format("%4d", i);
        }
        output += "\n";

        // Row number, then row, loop over whole grid, adding the squares to the string.
        for (int row = 0; row < grid.length; row++) {
            output += String.format("%4d", row);
            for (int col = 0; col < grid[row].length; col++) {
                output += String.format("%4s", grid[row][col].toString());
            }
            output += "\n";
        }
        return output;
    }
}