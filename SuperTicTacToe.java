/**
 * Houses the main method
 *
 * @Authors Nate Tillery, Miles Conrad
 * @version 1.0
*/
import javax.swing.*;
import java.util.Locale;


public class SuperTicTacToe extends JFrame{

    /**
     * Makes the board and this game
     *
     * @param args any arguments to be passed into main
     */
    public static void main(String[] args){
        //Create a JFrame object
        JFrame SuperTicTacToe = new JFrame("Super Tic-Tac-Toe");
        SuperTicTacToe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // take input for board size
        int boardSize = 0;
        //Set up a loop to make sure board size is good before program moves on
        while (boardSize <= 2 || boardSize >= 15) {
            String input = JOptionPane.showInputDialog(null, "Enter in the size of the board (3 - 14): ");
            //If board size is set to null, exit program
            if (input == null) {
                System.exit(1);
            }

            try {
                //Set the board size... If it doesn't work then throw IAE
                boardSize = Integer.parseInt(input);
                if (boardSize <= 2 || boardSize >= 15) {
                    throw new IllegalArgumentException();
                }
            }
            //Let the user know that they need to retry
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Sorry, the value you entered was either not a number or did not meet the requirements. Please try again.");
            }
        }

        //Decide which player starts first
        boolean decidingTurn = true;
        String firstTurnDecision = null;
        //set up whileloop to ensure decision is fully made and properly set
        while(decidingTurn){
            String input = JOptionPane.showInputDialog(null, "Who starts first? X or O: ");
            //If user clicks cancel
            if (input == null) {
                System.exit(1);
            }
            try {
                firstTurnDecision = input;
                if(!(input.toLowerCase().equals("x") || input.toLowerCase().equals("o"))){
                    //If the user enters bad input, throw an IAE
                    throw new IllegalArgumentException();
                }
                else{
                    //If the code makes it past all possible errors, then we're good.
                    decidingTurn = false;
                }
            }
            //Explain that the user didn't enter something correctly (If needed)
            catch (Exception e){
                JOptionPane.showMessageDialog(null, "Sorry, the value you entered did not meet the requirements. Please try again.");
            }

        }
        //Finally set the first turn for the appropriate team
        Cell firstTurn = Cell.X;
        if (firstTurnDecision.toLowerCase().equals("o")) {
            firstTurn = Cell.O;
        }


        //Create a new SuperTicTacToe game object
        SuperTicTacToeGame game = new SuperTicTacToeGame(boardSize, firstTurn);

        //Creates a new TicTacToe panel and adds it to the gui
        SuperTicTacToePanel panel = new SuperTicTacToePanel(boardSize, game);
        SuperTicTacToe.getContentPane().add(panel);

        SuperTicTacToe.setSize(800, 800);
        SuperTicTacToe.setVisible(true);
    }
}
