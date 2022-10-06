// Joe Vassallo Final CS110 Minesweeper Project
// I got a bit carried away and did all the extra credits to be safe :)

/** EXTRA CREDIT CONSIDERATION
    I have added the following functionality, I would like to be evaluated for extra credit.
        ** First selection can’t be a mine. 
        ** Throw an exception when the user attempts to uncover a flagged square.
        ** Allow the user to select a level when they start.
        ** Allow the user to restart the game.
        ** Early submission.
        ** Use recursion and implement the true game of Minesweeper.
*/

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        boolean replay = true, invalidInput = true, gameGoing = true;
        Scanner keyboard = new Scanner(System.in);

        // Loop while the user wants to keep playing.
        while (replay) {
            Minesweeper mine = new Minesweeper();

            // Pregame settings.
            mine.setDifficulty();
            mine.generateBoard();

            // Initial print.
            System.out.println(mine.toString());

            // Main gameplay loop.
            while (gameGoing) {
                gameGoing = mine.makeMove();
                System.out.println(mine.toString());
                mine.setFirstMove();
            }
            
            // Play again logic.
            System.out.println("Would you like to play again? Y or N");
            while (invalidInput) {
                // I'm a really big fan of switch statements. They feel much better than using
                // tons of ifs.
                switch (keyboard.nextLine().toUpperCase()) {
                    case "Y":
                        invalidInput = false;
                        break;
                    case "N":
                        invalidInput = false;
                        replay = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                }
            }
            gameGoing = true;
            invalidInput = true;
        }
    }
}
