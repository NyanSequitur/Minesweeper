public abstract class Square {
    String element;
    boolean flagged;
    boolean uncovered;

    /**
     * No-input constructor.
     * 
     */
    Square() {
        element = "x";
        flagged = false;
        uncovered = false;
    }

    /**
     * Constructor that assigns values.
     * 
     * @param element   How the square displays.
     * @param flagged   Whether or not the square is flagged.
     * @param uncovered If the square's been uncovered.
     */
    Square(String element, boolean flagged, boolean uncovered) {
        this.element = element;
        this.flagged = flagged;
        this.uncovered = uncovered;
    }

    /**
     * Return whether the square is flagged.
     * 
     * @return boolean if the square is flagged or not.
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Return whether the square is uncovered.
     * 
     * @return boolean if the square is uncovered or not.
     */
    public boolean isUncovered() {
        return uncovered;
    }

    /**
     * Toggles flagged state, sets the element to F if it wasn't flagged before.
     * 
     */
    public void flagSquare() {
        flagged = !flagged;
        if (flagged)
            element = "F";
    }

    /**
     * Sets the square to be uncovered.
     * 
     */
    public void setUncovered() {
        uncovered = true;
    }

    /**
     * Changes the display element of a square.
     * 
     * @param element to change the display to.
     */
    public void setElement(String element) {
        this.element = element;
    }

    /**
     * returns the square's element as a string.
     * 
     * @return String The square's element.
     */
    @Override
    public String toString() {
        return element;
    }

    // Abstract classes to extend in subclasses.

    public abstract boolean isMine();

    public abstract boolean uncover();

}
