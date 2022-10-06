public class MineSquare extends Square {
    // Mines aren't exactly complicated pieces
    /**
     * Returns if it's a mine or not (Spoilers: it is)
     * 
     * @return boolean it is, in fact, a mine.
     */
    public boolean isMine() {
        return true;
    }

    /**
     * Uncovers the square
     * 
     * @return boolean Probably meant to return whether the uncovery was successful,
     *         but the extra credit goes a different way.
     */
    public boolean uncover() {
        // I imagine the initial purpose of the boolean was to return whether the
        // uncover was successful in terms of flags, but since I'm doing the extra
        // credit it'll only return true (or throw an exception).
        if (!isFlagged()) {
            setElement("*");
            return true;
        } else
            // I didn't really know what exception to throw, this sounded about right and it
            // doesn't exactly matter too much in this case.
            throw new IllegalArgumentException("Cannot uncover a flagged piece.");
    }
}
