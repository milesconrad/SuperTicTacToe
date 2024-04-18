import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/******************************************
 * Houses the TicTacToe Panel
 *
 * @Authors Miles Conrad, Nate Tillery
 * @version 1.0
 ******************************************/
public class SuperTicTacToePanel extends JPanel {
    private JButton[][] jButtonsBoard;
    private Cell[][] iBoard;
    private int boardSize;
    private JButton quitButton;
    private JButton undoButton;
    private ImageIcon xIcon;
    private ImageIcon oIcon;
    private ImageIcon emptyIcon;
    private SuperTicTacToeGame game;

    public SuperTicTacToePanel(int boardSize, SuperTicTacToeGame game) {
        super();
        this.boardSize = boardSize;
        this.jButtonsBoard = new JButton[boardSize][boardSize];
        iBoard = game.getBoard();
        this.game = game;

        setLayout(new GridLayout(2, 1));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        //Make the quit button
        quitButton = new JButton("Quit Game");
        buttonPanel.add(quitButton);
        quitButton.addActionListener(new ButtonListener());

        //Make the undo button
        undoButton = new JButton("Undo Move");
        buttonPanel.add(undoButton);
        undoButton.addActionListener(new ButtonListener());

        //Create variables for the icons
        xIcon = new ImageIcon("src/LetterX.png");
        oIcon = new ImageIcon("src/LetterO.png");
        emptyIcon = new ImageIcon();

        //Make the JPanel for the boardPanel
        JPanel boardPanel = new JPanel();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                jButtonsBoard[i][j] = new JButton();
                jButtonsBoard[i][j].addActionListener(new ButtonListener());
                boardPanel.add(jButtonsBoard[i][j]);
            }
        }
        boardPanel.setLayout(new GridLayout(boardSize, boardSize));

        this.add(buttonPanel);
        this.add(boardPanel);

        if (game.firstTurn == Cell.O) {
            game.makeComputerMove();
            displayBoard();
        }
    }
    //Within one of the panels within SuperTicTacToePanel,
    // create a listener for every JButton in the 2D array variable that represents the board

    /******************************************
     * Method to set up the board
     ******************************************/
    private void displayBoard() {
        iBoard = game.getBoard();

        //Set appropriate icon to JButtons within GUI
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (iBoard[i][j] == Cell.O) {
                    jButtonsBoard[i][j].setIcon(oIcon);
                }
                else if (iBoard[i][j] == Cell.X) {
                    jButtonsBoard[i][j].setIcon(xIcon);
                }
                else {
                    jButtonsBoard[i][j].setIcon(emptyIcon);
                }
            }
        }
    }

    /******************************************
     * ButtonListener private inner class that houses actionPerformed
     * implements ActionListener
     ******************************************/
    private class ButtonListener implements ActionListener {
        @Override
        /******************************************
         * Action handler to handle actions
         *
         * @parameter e ActionEvent
         ******************************************/
        public void actionPerformed(ActionEvent e) {
            //quit the game
            if (quitButton == e.getSource()) {
                System.exit(1);
            }
            else if (undoButton == e.getSource()) {
                game.undoMove();
                displayBoard();
                return;
            }

            //Call the game.select method when user clicks JButton on board and change Icon to correct one.
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (jButtonsBoard[i][j] == e.getSource()) {
                        if (iBoard[i][j] != Cell.EMPTY) {
                            return;
                        }
                        game.select(i, j);
                    }
                }
            }
            displayBoard();

            GameStatus status = game.getGameStatus();
            //If the game is done, then say so and end the game
            if (status != GameStatus.IN_PROGRESS) {
                if (status == GameStatus.X_WON) {
                    JOptionPane.showMessageDialog(null, "X won and O lost!\n The game will reset");
                }
                else if (status == GameStatus.CATS) {
                    JOptionPane.showMessageDialog(null, "Cat's game!\n The game will reset");
                }

                game.reset();
                displayBoard();
                return;
            }

            game.makeComputerMove();
            displayBoard();
            status = game.getGameStatus();
            //If the game is done, then say so and end the game
            if (status != GameStatus.IN_PROGRESS) {
                if (status == GameStatus.O_WON) {
                    JOptionPane.showMessageDialog(null, "O won and X lost!\n The game will reset");
                }
                else if (status == GameStatus.CATS) {
                    JOptionPane.showMessageDialog(null, "Cat's game!\n The game will reset");
                }

                //Reset the game once it's done
                game.reset();
                displayBoard();
            }
        }
    }
}
