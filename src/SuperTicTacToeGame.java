import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/******************************************
 * Houses the game board as an ArrayList
 * @Authors Nate Tillery, Miles Conrad
 * @version 1.0
 ******************************************/
public class SuperTicTacToeGame {
    private Random rng = new Random();
    private Cell[][] board;
    private GameStatus status;
    private Cell currentTurn;
    private int boardSize;

    public Cell firstTurn;
    public ArrayList<Point> moves;


    /**
     * SuperTicTacToeGame class constructor
     * @param boardSize parameter for size of square board
     * @param firstTurn team that gets to go first
     */
    public SuperTicTacToeGame(int boardSize, Cell firstTurn) {
        this.boardSize = boardSize;
        this.currentTurn = firstTurn;
        this.firstTurn = firstTurn;
        status = GameStatus.IN_PROGRESS;
        moves = new ArrayList<Point>();

        board = new Cell[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
    }

    /**
     * Selects a spot on this board
     * @param row row to be selected
     * @param col column to be selected
     */
    public void select(int row, int col) {
        if (currentTurn == Cell.X) {
            board[row][col] = Cell.X;
            currentTurn = Cell.O;
        }
        else if (currentTurn == Cell.O) {
            board[row][col] = Cell.O;
            currentTurn = Cell.X;
        }

        moves.add(new Point(row, col));
    }

    /**
     * Resets the board
     */
    public void reset() {
        status = GameStatus.IN_PROGRESS;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
        currentTurn = firstTurn;
        if (currentTurn == Cell.O) {
            makeComputerMove();
        }
    }


    /**
     * Undoes the player's last move
     */
    public void undoMove() {
        // moves are undone twice to undo the computer's most recent move as well as the player's
        for (int i = 0; i < 2; i++) {
            int lastIndex = moves.size() - 1;
            Point move = moves.get(lastIndex);
            board[move.x][move.y] = Cell.EMPTY;
            moves.remove(lastIndex);
        }
    }

    // direction is a number 1-3, representing upper right, right, lower right, and down respectively (we do not need to search upwards or left because a match in that direction would already be found)
    // consecutive represents the number of matching consecutive cells we have found along a line

    /**
     * Returns a boolean description of if a team has won
     * @param row
     * @param col
     * @param direction
     * @param consecutive
     * @return isWinning whether this team won
     */
    private boolean isWinning(int row, int col, int direction, int consecutive) {
        if (consecutive >= 3) {
            return true;
        }

        switch (direction) {
            case 1:
                if (row - 1 < 0 || col + 1 == boardSize) {
                    return false;
                }
                else if (board[row][col] == board[row - 1][col + 1]) {
                    return isWinning(row - 1, col + 1, direction, consecutive + 1);
                }
                break;
            case 2:
                if (col + 1 == boardSize) {
                    return false;
                }
                else if (board[row][col] == board[row][col + 1]) {
                    return isWinning(row, col + 1, direction, consecutive + 1);
                }
                break;
            case 3:
                if (row + 1 == boardSize || col + 1 == boardSize) {
                    return false;
                }
                else if (board[row][col] == board[row + 1][col + 1]) {
                    return isWinning(row + 1, col + 1, direction, consecutive + 1);
                }
                break;
            case 4:
                if (row + 1 == boardSize) {
                    return false;
                }
                else if (board[row][col] == board[row + 1][col]) {
                    return isWinning(row + 1, col, direction, consecutive + 1);
                }
                break;
        }

        return false;
    }

    /**
     * Makes a move for the computer
     */
    public void makeComputerMove() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != Cell.EMPTY) {
                    continue;
                }

                board[i][j] = Cell.O;
                for (int k = 1; k <= 4; k++) {
                    if (isWinning(i, j, k, 1)) {
                        select(i, j);
                        return;
                    }
                }
                board[i][j] = Cell.EMPTY;
            }
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != Cell.EMPTY) {
                    continue;
                }

                board[i][j] = Cell.X;
                for (int k = 1; k <= 4; k++) {
                    if (isWinning(i, j, k, 1)) {
                        select(i, j);
                        return;
                    }
                }
                board[i][j] = Cell.EMPTY;
            }
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == Cell.O) {
                    for (int k = i - 2; k <= i + 2; k += 2) {
                        for (int l = j - 2; l <= j + 2; l += 2) {
                            if ((k < boardSize && k >= 0) && (l < boardSize && l >= 0) && board[k][l] == Cell.EMPTY) {
                                select(k, l);
                                return;
                            }
                        }
                    }
                }
            }
        }

        int row = rng.nextInt(board.length);
        int col = rng.nextInt(board.length);
        while (board[row][col] != Cell.EMPTY) {
            row = rng.nextInt(board.length);
            col = rng.nextInt(board.length);
        }
        select(row, col);
    }

    /**
     * Returns the game's status
     * @return hasEmptySpace boolean that tells whether the game has a space available for play
     */
    public GameStatus getGameStatus() {
        boolean hasEmptySpace = false;

        //Checks each board space and if the spot is empty, sets hasEmptySpace to true before leaving the loop
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == Cell.EMPTY) {
                    hasEmptySpace = true;
                    continue;
                }

                // k represents the direction we are searching
                for (int k = 1; k <= 4; k++) {
                    if (isWinning(i, j, k, 1)) {
                        if (board[i][j] == Cell.X) {
                            status = GameStatus.X_WON;
                        }
                        else {
                            status = GameStatus.O_WON;
                        }
                        return status;
                    }
                }
            }
        }
        //If there are no empty spaces, then the game is ended in a draw
        if (!hasEmptySpace) {
            status = GameStatus.CATS;
        }

        return status;
    }

    /**
     * Returns this game's board
     * @return board this game's board
     */
    public Cell[][] getBoard() {
        return board;
    }
}
