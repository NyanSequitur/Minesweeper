import java.util.*;

public class Minesweeper {
    private final int[] BEGINNER = { 8, 8, 8 }, INTERMEDIATE = { 10, 12, 10 }, ADVANCED = { 16, 20, 50 };
    private int[] size = new int[2];
    private boolean firstMove;
    private String chosenDifficulty;
    private Grid board;
    private Scanner keyboard = new Scanner(System.in);

    /**
     * Not much needs to happen in the constructor. Difficulty-setting is handled by
     * a method.
     */
    Minesweeper() {
        // I could've handled difficulty setting here, but it's more suited to its own
        // method, since it handles user input and setting. *technically* the
        // Minesweeper class I have doesn't even need a constructor, and the default one
        // would probably suffice if I assigned the starting values in their
        // declaration. Initially I had the gameplay loop in here, but that
        // didn't feel like proper OOP format, so I moved the loop into the driver. The
        // guidance said I was free to design the Minesweeper class any way I'd like
        // though, and this is what worked for me.

        // just a temporary difficulty assignment for the initial board generation.
        chosenDifficulty = "B";
        firstMove = true;
        generateBoard();
    }

    /**
     * Validates user input and returns an array containing the 3 input values
     * 
     * @return String[] Input values given by the user.
     */
    private String[] getMove() {
        String s = "";
        String[] parts = new String[3];
        boolean goodInput = false;
        System.out.println("(U)ncover r c, (F)lag r c, (Q)uit");
        // Loop while there isn't an acceptable move.
        while (!goodInput) {
            s = keyboard.nextLine().toUpperCase();
            if (!s.isEmpty()) {
                parts = s.split("\\s+");
                switch (s.charAt(0)) {
                    case 'Q':
                        // The code is expecting 2 integers in the list, it's easier to just send a list
                        // than rework that code.
                        board.exposeMines();
                        return new String[] { "Q", "0", "0" };

                    // 'U' falls through to the 'F' case.
                    case 'U':
                    case 'F': {
                        // Make sure the two numbers are integers less than/equal to the size of the
                        // board.
                        try {
                            Integer.parseInt(parts[1]);
                            Integer.parseInt(parts[2]);
                            goodInput = true;
                        } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Invalid input. Please try again.");
                        }
                        break;
                    }
                    default:
                        System.out.println("Invalid input. Please try again.");
                }
            } else
                System.out.println("Please make a choice.");

        }
        return parts;
    }

    /**
     * Handles square uncovering.
     * 
     * @param row of square being uncovered
     * @param col of square being uncovered
     * @return boolean whether the game can continue given the result of the square
     *         being uncovered.
     */
    private boolean uncover(int row, int col) {
        switch (board.uncoverSquare(row, col)) {
            case OK:
                return true;
            case WIN: {
                board.exposeMines();
                System.out.printf(board.toString());
                System.out.println("Congrats!");
                return false;
            }
            case MINE: {
                // If it's the first move, keep generating boards until a board is generated
                // that doesn't have a mine on the selected square.

                if (firstMove) {
                    generateBoard();
                    return uncover(row, col);
                }

                else {
                    board.exposeMines();
                    System.out.printf(board.toString());
                    System.out.println("Better luck next time!");
                    return false;
                }
            }
            default: {
                // IDE complained unless I had this in it. Was useful for debugging though.
                System.out.print("THIS SHOULD NEVER HAPPEN. EVERYTHING IS BROKEN. UNCOVER");
                return false;
            }
        }
    }

    /**
     * Generates a new random grid of a size correlating to the difficulty.
     * 
     * @return Grid New grid randomly generated.
     */
    public void generateBoard() {
        // Nothing too too complex going on here, bretty basic switch statement.
        switch (chosenDifficulty) {
            case "B":
                size[0] = BEGINNER[0];
                size[1] = BEGINNER[1];
                board = new Grid(BEGINNER[0], BEGINNER[1], BEGINNER[2]);
                break;
            case "I":
                size[0] = INTERMEDIATE[0];
                size[1] = INTERMEDIATE[1];
                board = new Grid(INTERMEDIATE[0], INTERMEDIATE[1], INTERMEDIATE[2]);
                break;
            case "A":
                size[0] = ADVANCED[0];
                size[1] = ADVANCED[1];
                board = new Grid(ADVANCED[0], ADVANCED[1], ADVANCED[2]);
                break;
            default:
                System.out.print("THIS SHOULD NEVER HAPPEN. EVERYTHING IS BROKEN. GENERATE");
                board = new Grid(1, 2, 1);
        }

    }

    /**
     * First move needs to be able to be set to false from outside.
     */
    public void setFirstMove() {
        firstMove = false;
    }

    /**
     * Handles the making of a move.
     * 
     * @return if the game can continue after the move is made.
     */
    public boolean makeMove() {
        String choice, move[];
        move = getMove();
        choice = move[0];
        int row = Integer.parseInt(move[1]);
        int col = Integer.parseInt(move[2]);

        // Takes the action based on the user's choice
        switch (choice) {
            case "U":
                return uncover(row, col);

            case "F":
                board.flagSquare(row, col);
                return true;

            case "Q":
                return false;
            default:
                return true;
        }
    }

    /**
     * Returns the board, formatted nicely into a string.
     * 
     * @return String The board as a string.
     */
    @Override
    public String toString() {
        // All the complicated stuff is handled in the board toString() method, this is
        // just passing it along.
        return board.toString();
    }

    /**
     * Handles setting the difficulty of the game.
     * 
     */
    public void setDifficulty() {

        boolean goodInput = false;
        System.out.println("Select a difficulty: (B)eginner, (I)ntermediate, or (A)dvanced:");
        // Input validation
        while (!goodInput) {
            chosenDifficulty = keyboard.nextLine().toUpperCase();

            // Make sure there is an input first.
            if (!chosenDifficulty.isEmpty()
                    && (chosenDifficulty.equals("B") || chosenDifficulty.equals("I") || chosenDifficulty.equals("A"))) {
                goodInput = true;
            } else
                System.out.println("Invalid input. Please try again.");
        }
    }
}
